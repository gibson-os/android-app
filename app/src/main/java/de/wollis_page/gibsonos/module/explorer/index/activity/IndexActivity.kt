package de.wollis_page.gibsonos.module.explorer.index.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.util.ArrayMap
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.ListActivity
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.exception.ResponseException
import de.wollis_page.gibsonos.helper.Config
import de.wollis_page.gibsonos.helper.toHumanReadableByte
import de.wollis_page.gibsonos.module.explorer.index.dto.Dir
import de.wollis_page.gibsonos.module.explorer.index.dto.Item
import de.wollis_page.gibsonos.module.explorer.task.DirTask
import de.wollis_page.gibsonos.module.explorer.task.FileTask
import java.util.concurrent.CompletableFuture

class IndexActivity: ListActivity() {
    override fun getListRessource() = R.layout.explorer_index_list_item
    private lateinit var loadedDir: Dir
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
        super.onCreate(savedInstanceState)

        this.findViewById<TextView>(android.R.id.title).setOnClickListener {
            Toast.makeText(this, "Click!", Toast.LENGTH_SHORT).show()
        }

        if (savedInstanceState == null) {
            this.loadList((this.getItem().params?.get("dir") ?: "").toString())

            return
        }

        this.loadedDir = savedInstanceState.getParcelable(DIRECTORY_KEY)!!
        this.setDir()

        this.images = savedInstanceState.getSerializable(IMAGES_KEY) as HashMap<String, ArrayMap<String, Bitmap>>
        this.imageWidth = savedInstanceState.getInt(IMAGE_WIDTH_KEY)

        if (this.imageWidth == 0) {
            this.imageWidth = null
        }
    }

    private fun loadList(directory: String = "") = this.load {
        Log.i(Config.LOG_TAG, "Read dir $directory")
        this.loadedDir = DirTask.read(this, it.account, directory)
        this.setDir()
    }

    private fun setDir() {
        this.setTitle(this.loadedDir.dir)
        this.listAdapter.items = this.loadedDir.data.toMutableList()
    }

    override fun onCLick(item: ListItemInterface) {
        if (item !is Item) {
            return
        }

        if (item.type == "dir") {
            this.loadList(item.path + "/" + item.name)

            return
        }

        Toast.makeText(this, R.string.not_implemented_yet, Toast.LENGTH_LONG).show()
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

        val imageView = view.findViewById<View>(R.id.icon) as ImageView

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

        if (item.html5VideoToken != null) {
            (view.findViewById<View>(R.id.html5) as ImageView).visibility = View.VISIBLE
        } else {
            (view.findViewById<View>(R.id.html5) as ImageView).visibility = View.INVISIBLE
        }

        (view.findViewById<View>(R.id.name) as TextView).text = item.name
        (view.findViewById<View>(R.id.size) as TextView).text = item.size.toHumanReadableByte()
    }

    private fun loadImages() {
        if (this.imagesLoading) {
            return
        }

        this.imagesLoading = true

        CompletableFuture.supplyAsync<Any> {
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
        }.exceptionally { e -> e.printStackTrace() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(IMAGE_WIDTH_KEY, this.imageWidth ?: 0)
        outState.putParcelable(DIRECTORY_KEY, this.loadedDir)
        outState.putSerializable(IMAGES_KEY, this.images)
    }
}