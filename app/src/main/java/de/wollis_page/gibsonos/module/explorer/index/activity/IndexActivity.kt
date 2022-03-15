package de.wollis_page.gibsonos.module.explorer.index.activity

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.ArrayMap
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.mediarouter.app.MediaRouteChooserDialog
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.AppActivityInterface
import de.wollis_page.gibsonos.activity.ListActivity
import de.wollis_page.gibsonos.dto.DialogItem
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.exception.ResponseException
import de.wollis_page.gibsonos.helper.AlertDialog
import de.wollis_page.gibsonos.helper.Config
import de.wollis_page.gibsonos.helper.toHumanReadableByte
import de.wollis_page.gibsonos.module.explorer.index.dto.Dir
import de.wollis_page.gibsonos.module.explorer.index.dto.Html5Status
import de.wollis_page.gibsonos.module.explorer.index.dto.Item
import de.wollis_page.gibsonos.module.explorer.task.DirTask
import de.wollis_page.gibsonos.module.explorer.task.FileTask
import de.wollis_page.gibsonos.module.explorer.task.Html5Task

class IndexActivity: ListActivity(), AppActivityInterface {
    override fun getListRessource() = R.layout.explorer_index_list_item
    private lateinit var loadedDir: Dir
    private var loadDir: String? = null
    private var images = HashMap<String, ArrayMap<String, Bitmap>>()
    private val imageQueue = ArrayMap<ImageView, Item>()
    private var imagesLoading = false
    private var imageWidth: Int? = null

    companion object {
        const val DIRECTORY_KEY = "directory"
        const val IMAGE_WIDTH_KEY = "image_width"
        const val IMAGES_KEY = "images"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            this.loadedDir = savedInstanceState.getParcelable(DIRECTORY_KEY)!!
            this.loadDir = loadedDir.dir
        }

        super.onCreate(savedInstanceState)

        this.addSearch()

//        this.findViewById<TextView>(android.R.id.title).setOnClickListener {
//            this.runTask {
//                val dirList = DirTask.dirList(this, this.loadedDir.dir)
//
//                this.runOnUiThread {
//                    val alertDialog = AlertDialog.Builder(this)
//                        .setTitle("Test")
//                    alertDialog.create().show()
//                }
//            }
//        }

        if (savedInstanceState == null) {
            this.loadList((this.getItem().params?.get("dir") ?: "").toString())

            return
        }

        this.images = savedInstanceState.getSerializable(IMAGES_KEY) as HashMap<String, ArrayMap<String, Bitmap>>
        this.imageWidth = savedInstanceState.getInt(IMAGE_WIDTH_KEY)

        if (this.imageWidth == 0) {
            this.imageWidth = null
        }
    }

    override fun loadList(start: Long, limit: Long) {
        if (this.loadDir == null) {
            this.loadDir = (this.getItem().params?.get("dir") ?: "").toString()
        }

        this.loadList(this.loadDir)
    }

    private fun loadList(directory: String? = "") = this.load {
        Log.i(Config.LOG_TAG, "Read dir $directory")
        this.loadedDir = DirTask.read(this, directory ?: "")
        this.loadDir = loadedDir.dir
        this.listAdapter.items = this.loadedDir.data.toMutableList()
        this.setTitle(this.loadDir.toString())
    }

    override fun onClick(item: ListItemInterface) {
        if (item !is Item) {
            return
        }

        if (item.type == "dir") {
            this.loadList(item.path + "/" + item.name)

            return
        }

        val options = ArrayList<DialogItem>()
        var html5Item = DialogItem("Für HTML5 konvertieren")
        html5Item.icon = R.drawable.ic_html5

        if (item.html5VideoStatus == Html5Status.GENERATED) {
            html5Item = DialogItem("An Chromecast senden")
            html5Item.icon = R.drawable.ic_chromecast
            html5Item.onClick = {
                MediaRouteChooserDialog(this).show();
            }
        }

        options.add(html5Item)

        AlertDialog(this, item.name, options).show()
    }

    override fun onBackPressed() {
        Log.i(Config.LOG_TAG, "Loaded dir " + this.loadedDir.dir)
        val dirs = this.loadedDir.dir.split("/").toMutableList()

        if (dirs.last().isEmpty()) {
            dirs.removeLast()
        }

        dirs.removeLast()

        val newDir = dirs.joinToString("/") + "/"
        Log.i(Config.LOG_TAG, "New dir $newDir")

        if (newDir.length < this.loadedDir.homePath.length || newDir == this.loadedDir.dir) {
            super.onBackPressed()

            return
        }

        this.loadList(newDir)
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Item) {
            return
        }

        val imageView = view.findViewById<ImageView>(R.id.icon)

        if (item.type == "dir") {
            imageView.setImageResource(R.drawable.ic_folder)
        } else {
            imageView.setImageResource(R.drawable.ic_file)

            if (item.thumbAvailable) {
                var imagePath = this.images[this.loadedDir.dir]

                if (imagePath == null) {
                    imagePath = ArrayMap()
                    this.images[this.loadedDir.dir] = imagePath
                }

                if (imagePath[item.name] == null) {
                    this.imageQueue[imageView] = item
                    this.loadImages()
                } else {
                    imageView.setImageBitmap(imagePath[item.name])
                }
            }
        }

        val html5VideoStatus = item.html5VideoStatus
        val html5ImageView = view.findViewById<View>(R.id.html5) as ImageView
        var color = Color.rgb(233, 98, 40)
        val progressBar = view.findViewById(R.id.position) as ProgressBar
        progressBar.max = 1
        progressBar.progress = 0
        progressBar.visibility = View.INVISIBLE
        html5ImageView.visibility = View.INVISIBLE

        if (html5VideoStatus != null) {
            html5ImageView.visibility = View.VISIBLE

            when (html5VideoStatus) {
                Html5Status.WAIT -> {
                    color = Color.rgb(218, 218, 218)
                }
                Html5Status.ERROR -> {
                    color = Color.rgb(255, 0, 0)
                }
                Html5Status.GENERATE -> {
                    color = Color.rgb(0, 0, 255)

                    this.runTask({
//                        while (html5VideoStatus == Html5Status.GENERATE) {
                            val convertStatus = Html5Task.convertStatus(
                                this,
                                item.html5VideoToken ?: ""
                            )

                            this.runOnUiThread {
                                progressBar.max = convertStatus.frames
                                progressBar.progress = convertStatus.frame
                                progressBar.visibility = View.VISIBLE
                            }

//                            Thread.sleep(1000)
//                        }
                    })
                }
                else -> {}
            }

            html5ImageView.setColorFilter(color)
        }

        (view.findViewById<View>(R.id.name) as TextView).text = item.name
        (view.findViewById<View>(R.id.size) as TextView).text = item.size.toHumanReadableByte()

        progressBar.progressTintList = ColorStateList.valueOf(color)

        if (
            item.metaInfos !== null &&
            item.position !== null &&
            item.metaInfos.containsKey("duration")
        ) {
            progressBar.max = item.metaInfos["duration"].toString().toFloat().toInt()
            progressBar.progress = item.position
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun loadImages() {
        if (this.imagesLoading) {
            return
        }

        this.imagesLoading = true

        this.runTask({
            while (this.imageQueue.size != 0) {
                val imageView = this.imageQueue.keyAt(0)
                val item = this.imageQueue[imageView] ?: continue
                this.imageQueue.remove(imageView)

                if (this.imageWidth == null) {
                    this.imageWidth = imageView.width
                }

                var imagePath = this.images[this.loadedDir.dir]

                if (imagePath == null) {
                    imagePath = ArrayMap()
                    this.images[this.loadedDir.dir] = imagePath
                }

                try {
                    if (imagePath[item.name] == null) {
                        imagePath[item.name] = FileTask.image(
                            this.getAccount(),
                            this.loadedDir.dir,
                            item.name,
                            this.imageWidth
                        )
                    }

                    this.runOnUiThread { imageView.setImageBitmap(imagePath[item.name]) }
                } catch (exception: ResponseException) {}
            }

            this.imagesLoading = false
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(IMAGE_WIDTH_KEY, this.imageWidth ?: 0)
        outState.putParcelable(DIRECTORY_KEY, this.loadedDir)
        outState.putSerializable(IMAGES_KEY, this.images)
    }

    override fun getAppIcon() = R.drawable.ic_folder
}