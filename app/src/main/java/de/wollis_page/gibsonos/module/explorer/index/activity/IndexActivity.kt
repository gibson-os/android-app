package de.wollis_page.gibsonos.module.explorer.index.activity

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.os.Bundle
import android.util.ArrayMap
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.mediarouter.app.MediaRouteChooserDialog
import androidx.mediarouter.media.MediaControlIntent
import androidx.mediarouter.media.MediaRouteSelector
import com.google.android.gms.cast.CastMediaControlIntent
import com.google.android.gms.cast.framework.CastContext
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.AppActivityInterface
import de.wollis_page.gibsonos.activity.ListActivity
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.helper.Chromecast
import de.wollis_page.gibsonos.helper.Config
import de.wollis_page.gibsonos.helper.toHumanReadableByte
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.module.explorer.index.dialog.DirListDialog
import de.wollis_page.gibsonos.module.explorer.index.dialog.ItemDialog
import de.wollis_page.gibsonos.module.explorer.index.dto.Dir
import de.wollis_page.gibsonos.module.explorer.index.dto.Html5Status
import de.wollis_page.gibsonos.module.explorer.index.dto.Item
import de.wollis_page.gibsonos.module.explorer.task.DirTask
import de.wollis_page.gibsonos.module.explorer.task.FileTask
import de.wollis_page.gibsonos.module.explorer.task.Html5Task

class IndexActivity: ListActivity(), AppActivityInterface {
    lateinit var loadedDir: Dir
    lateinit var castContext: CastContext
    lateinit var mediaRouteChooserDialog: MediaRouteChooserDialog
    lateinit var playerLauncher: ActivityResultLauncher<Intent>
    private lateinit var itemDialogBuilder: ItemDialog
    private var loadDir: String? = null
    private var images = HashMap<String, ArrayMap<String, Bitmap>>()
    private var imageQueue = ArrayMap<ImageView, Item>()
    private var imagesLoading = false
    private var imageWidth: Int? = null

    override fun getListRessource() = R.layout.explorer_index_list_item

    companion object {
        const val DIRECTORY_KEY = "directory"
        const val IMAGE_WIDTH_KEY = "image_width"
        const val IMAGES_KEY = "images"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        this.castContext = CastContext.getSharedInstance(this)

        this.castContext.sessionManager.addSessionManagerListener(Chromecast())
//        this.castContext.sessionManager.currentCastSession?.sendMessage(
//            "urn:x-cast:net.itronom.gibson",
//        )
        val mediaRouteSelector = MediaRouteSelector.Builder()
            .addControlCategory(MediaControlIntent.CATEGORY_REMOTE_PLAYBACK)
            .addControlCategory(CastMediaControlIntent.categoryForCast(Config.CHROMECAST_RECEIVER_APPLICATION_ID))
            .build()
        this.mediaRouteChooserDialog = MediaRouteChooserDialog(this)
        this.mediaRouteChooserDialog.routeSelector = mediaRouteSelector

        if (savedInstanceState != null) {
            this.loadedDir = savedInstanceState.getParcelable(DIRECTORY_KEY)!!
            this.loadDir = loadedDir.dir
        }

        super.onCreate(savedInstanceState)

        this.addSearch { it, searchTerm ->
            val item = it as Item

            item.name.lowercase().contains(searchTerm.lowercase())
        }

        val dirListDialogBuilder = DirListDialog(this)

        this.findViewById<TextView>(android.R.id.title).setOnClickListener {
            this.runTask({
                val dirListDialog = dirListDialogBuilder.build(
                    DirTask.dirList(this, this.loadedDir.dir)
                )

                this.runOnUiThread {
                    dirListDialog.show()
                }
            })
        }

        this.playerLauncher = this.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode != Activity.RESULT_OK) {
                return@registerForActivityResult
            }

            val position = it.data?.getIntExtra("position", 0)
            val token = it.data?.getStringExtra("token")
            val item = (this.listAdapter.items as ArrayList<Item>).find {
                it.html5VideoToken == token
            } ?: return@registerForActivityResult

            Log.d(Config.LOG_TAG, "Set Position " + position + " to " + item.name)

            item.position = position
            this.listAdapter.notifyItemChanged(this.getItemIndex(item))
        }

        this.itemDialogBuilder = ItemDialog(this)

        if (savedInstanceState == null) {
            this.loadList((this.getShortcut()?.params?.get("dir") ?: "").toString())

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
            this.loadDir = (this.getShortcut()?.params?.get("dir") ?: "").toString()
        }

        this.loadList(this.loadDir)
    }

    fun loadList(directory: String? = "") = this.load {
        Log.i(Config.LOG_TAG, "Read dir $directory")
        this.loadedDir = DirTask.read(this, directory ?: "")
        this.loadDir = loadedDir.dir
        this.listAdapter.items = this.loadedDir.data as ArrayList<ListItemInterface>
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

        this.itemDialogBuilder.build(item).show()
    }

    override fun onBackPressed() {
        Log.i(Config.LOG_TAG, "Loaded dir " + this.loadedDir.dir)
        this.imageQueue = ArrayMap<ImageView, Item>()
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

//            if (item.thumbAvailable) {
//                var imagePath = this.images[this.loadedDir.dir]
//
//                if (imagePath == null) {
//                    imagePath = ArrayMap()
//                    this.images[this.loadedDir.dir] = imagePath
//                }
//
//                if (imagePath[item.name] == null) {
//                    this.imageQueue[imageView] = item
//                    this.loadImages()
//                } else {
//                    imageView.setImageBitmap(imagePath[item.name])
//                }
//            }
        }

        val html5VideoStatus = item.html5VideoStatus
        val html5ImageView = view.findViewById<View>(R.id.html5) as ImageView
        val progressBar = view.findViewById(R.id.position) as ProgressBar
        progressBar.max = 1
        progressBar.progress = 0
        progressBar.visibility = View.INVISIBLE
        html5ImageView.visibility = View.INVISIBLE

        if (html5VideoStatus != null) {
            this.getConvertStatus(html5VideoStatus, item, progressBar, html5ImageView)
        }

        (view.findViewById<View>(R.id.name) as TextView).text = item.name
        (view.findViewById<View>(R.id.size) as TextView).text = item.size.toHumanReadableByte()

        this.setPosition(item, progressBar)
    }

    private fun setPosition(item: Item, progressBar: ProgressBar) {
        if (
            item.metaInfos !== null &&
            item.position !== null &&
            item.metaInfos!!.containsKey("duration")
        ) {
            progressBar.progressTintList = ColorStateList.valueOf(getColor(R.color.colorProgressDone))
            progressBar.max = item.metaInfos!!["duration"].toString().toFloat().toInt()
            progressBar.progress = item.position!!
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun getConvertStatus(
        html5VideoStatus: Html5Status?,
        item: Item,
        progressBar: ProgressBar,
        html5ImageView: ImageView
    ) {
        var actualHtml5VideoStatus = html5VideoStatus
        this.setHtml5StatusColor(actualHtml5VideoStatus, progressBar, html5ImageView)

        html5ImageView.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE

        this.runTask({
            while (
                actualHtml5VideoStatus == Html5Status.GENERATE ||
                actualHtml5VideoStatus == Html5Status.WAIT
            ) {
                this.setHtml5StatusColor(actualHtml5VideoStatus, progressBar, html5ImageView)

                try {
                    val convertStatus = Html5Task.convertStatus(
                        this,
                        item.html5VideoToken ?: "",
                    )

                    actualHtml5VideoStatus = convertStatus.status
                    item.html5VideoStatus = actualHtml5VideoStatus

                    if (actualHtml5VideoStatus != Html5Status.GENERATE) {
                        this.setHtml5StatusColor(actualHtml5VideoStatus, progressBar, html5ImageView)
                        this.runOnUiThread {
                            progressBar.progress = 0
                        }

                        if (actualHtml5VideoStatus == Html5Status.WAIT) {
                            Thread.sleep(3000)

                            continue
                        }

                        break
                    }

                    this.setHtml5StatusColor(actualHtml5VideoStatus, progressBar, html5ImageView)
                    this.runOnUiThread {
                        progressBar.max = convertStatus.frames
                        progressBar.progress = convertStatus.frame!!
                        progressBar.visibility = View.VISIBLE
                    }
                } catch (_: TaskException) {
                }

                Thread.sleep(1000)
            }

            this.setHtml5StatusColor(actualHtml5VideoStatus, progressBar, html5ImageView)
        })
    }

    private fun setHtml5StatusColor(
        html5VideoStatus: Html5Status?,
        progressBar: ProgressBar,
        html5ImageView: ImageView
    ) {
        if (html5VideoStatus == null) {
            return
        }

        val color = this.getColor(when (html5VideoStatus) {
            Html5Status.WAIT -> R.color.colorProgressWait
            Html5Status.ERROR -> R.color.colorProgressError
            Html5Status.GENERATE -> R.color.colorProgressGenerate
            Html5Status.GENERATED -> R.color.colorProgressDone
        })

        this.runOnUiThread {
            html5ImageView.setColorFilter(color)
            progressBar.progressTintList = ColorStateList.valueOf(color)
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
                } catch (_: TaskException) {}
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

    override fun getId(): Any = this.loadDir.toString()

    override fun isActivityforShotcut(shortcut: Shortcut): Boolean =
        shortcut.params?.get("dir") == this.loadDir.toString()
}