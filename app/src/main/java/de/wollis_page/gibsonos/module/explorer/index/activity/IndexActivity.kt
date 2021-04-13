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
    private val images = ArrayMap<String, ArrayMap<String, Bitmap>>()
    private val imageQueue = ArrayMap<ImageView, Item>()
    private var imagesLoading = false

    companion object {
        const val DIRECTORY_KEY = "directory"
        const val IMAGE_WIDTH_KEY = "image_width"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var directory: String? = null

        if (this.intent.hasExtra(DIRECTORY_KEY)) {
            directory = this.intent.getStringExtra(DIRECTORY_KEY)
        }

        this.loadList(directory ?: (this.getItem().params?.get("dir") ?: "").toString())

        this.findViewById<TextView>(android.R.id.title).setOnClickListener {
            Toast.makeText(this, "Click!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadList(directory: String = "") = this.load {
        Log.i(Config.LOG_TAG, "Read dir $directory")
        this.loadedDir = DirTask.read(this, it.account, directory)
        this.intent.putExtra(DIRECTORY_KEY, this.loadedDir.dir)

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
        var imageWidth: Int? = null

        if (this.intent.hasExtra(IMAGE_WIDTH_KEY)) {
            imageWidth = this.intent.getIntExtra(IMAGE_WIDTH_KEY, 0)
        }

        CompletableFuture.supplyAsync<Any> {
            while (this.imageQueue.size != 0) {
                val imageView = this.imageQueue.keyAt(0)
                val item = this.imageQueue[imageView] ?: continue
                this.imageQueue.remove(imageView)

                if (imageWidth == null) {
                    imageWidth = imageView.width
                    this.intent.putExtra(IMAGE_WIDTH_KEY, imageWidth)
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
                            imageWidth
                        )
                    }

                    this.runOnUiThread { imageView.setImageBitmap(imagePath[item.name]) }
                } catch (exception: ResponseException) {}
            }

            this.imagesLoading = false
        }.exceptionally { e -> e.printStackTrace() }
    }
}