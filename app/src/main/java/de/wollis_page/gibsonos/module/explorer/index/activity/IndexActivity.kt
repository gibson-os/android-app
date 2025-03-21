package de.wollis_page.gibsonos.module.explorer.index.activity

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.ListActivity
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.exception.ResponseException
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.helper.Config
import de.wollis_page.gibsonos.helper.toHumanReadableByte
import de.wollis_page.gibsonos.module.explorer.index.dialog.DirDialog
import de.wollis_page.gibsonos.module.explorer.index.dialog.DirListDialog
import de.wollis_page.gibsonos.module.explorer.index.dialog.ItemDialog
import de.wollis_page.gibsonos.module.explorer.index.dto.Dir
import de.wollis_page.gibsonos.module.explorer.index.dto.Html5Status
import de.wollis_page.gibsonos.module.explorer.index.dto.Item
import de.wollis_page.gibsonos.module.explorer.service.ChromecastService
import de.wollis_page.gibsonos.module.explorer.task.DirTask
import de.wollis_page.gibsonos.module.explorer.task.FileTask
import de.wollis_page.gibsonos.module.explorer.task.Html5Task
import de.wollis_page.gibsonos.service.ImageLoaderService


class IndexActivity: ListActivity() {
    lateinit var loadedDir: Dir
    lateinit var playerLauncher: ActivityResultLauncher<Intent>
    lateinit var chromecastService: ChromecastService
    private lateinit var itemDialog: ItemDialog
    private lateinit var dirDialog: DirDialog
    private var loadDir: String? = null
    private lateinit var imageLoaderService: ImageLoaderService<Item>

    override fun getListRessource() = R.layout.explorer_index_list_item

    override fun getContentView() = R.layout.base_chromecast_list

    companion object {
        const val DIRECTORY_KEY = "directory"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        this.imageLoaderService = ImageLoaderService(
            this.activity,
            {
                FileTask.image(
                    this.getAccount(),
                    this.loadedDir.dir,
                    it.name,
                    this.resources.getDimension(R.dimen.thumb_width).toInt()
                )
            },
            { item, image ->
                this.getViewByItem(item)?.findViewById<ImageView>(R.id.icon)?.setImageBitmap(image)
            }
        )

        if (savedInstanceState != null) {
            this.loadedDir = savedInstanceState.getParcelable(DIRECTORY_KEY)!!
            this.loadDir = loadedDir.dir
        }

        super.onCreate(savedInstanceState)

        this.chromecastService = ChromecastService(this) { session ->
            val remoteMediaClient = session?.remoteMediaClient
            val contentId = remoteMediaClient?.currentItem?.media?.contentId

            val listItem = (this.listAdapter.items as ArrayList<Item>).find {
                it.html5VideoToken == contentId
            }

            if (listItem !== null) {
                val progressBar =
                    this.getViewByItem(listItem)?.findViewById<ProgressBar>(R.id.position)

                listItem.position =
                    ((remoteMediaClient?.approximateStreamPosition ?: 0) / 1000).toInt()
                Log.d(Config.LOG_TAG, "update position: " + listItem.position)
                this.setPosition(listItem, progressBar)
            }
        }

        this.addSearch { it, searchTerm ->
            val item = it as Item

            item.name.lowercase().contains(searchTerm.lowercase())
        }

        val dirListDialogBuilder = DirListDialog(this) { _, dirList ->
            this.loadList(dirList.id)
        }

        this.findViewById<TextView>(android.R.id.title).setOnClickListener {
            this.hideSearch()
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

        this.itemDialog = ItemDialog(this)
        this.dirDialog = DirDialog(this)

        this.loadList((this.getShortcut()?.parameters?.get("dir") ?: "").toString())
    }

    override fun loadList(start: Long, limit: Long) {
        if (this.loadDir == null) {
            this.loadDir = (this.getShortcut()?.parameters?.get("dir") ?: "").toString()
        }

        this.loadList(this.loadDir)
    }

    fun loadList(directory: String? = "") = this.load {
        var cleanDirectory = directory ?: ""

        if (cleanDirectory.last() == '/') {
            cleanDirectory = cleanDirectory.substring(0, cleanDirectory.length - 1)
        }

        Log.i(Config.LOG_TAG, "Read dir $directory")
        this.loadedDir = DirTask.read(this, cleanDirectory)
        this.loadDir = loadedDir.dir
        this.listAdapter.items = this.loadedDir.data as ArrayList<ListItemInterface>

        this.runOnUiThread {
            this.listView.scrollToPosition(0)
        }

        val loadedDir = this.loadDir.toString()

        this.getShortcut()?.parameters?.set("dir", loadedDir)
        this.setTitle(loadedDir)
    }

    override fun onClick(item: ListItemInterface) {
        if (item !is Item) {
            return
        }

        this.hideSearch()

        if (item.type == "dir") {
            this.loadList(item.path + "/" + item.name)

            return
        }

        this.itemDialog.build(item).show()
    }

    override fun onLongClick(item: ListItemInterface): Boolean {
        if (item !is Item || item.type != "dir") {
            return false
        }

        this.hideSearch()

        this.dirDialog.build(item).show()

        return true
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
                this.imageLoaderService.viewImage(item, imageView, R.drawable.ic_file)
            }
        }

        val html5VideoStatus = item.html5VideoStatus
        val html5ImageView = view.findViewById<ImageView>(R.id.html5)
        val progressBar = view.findViewById<ProgressBar>(R.id.position)
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

    fun setPosition(item: Item, progressBar: ProgressBar?) {
        if (
            progressBar === null ||
            item.metaInfos === null ||
            item.position === null ||
            !item.metaInfos!!.containsKey("duration")
        ) {
            return
        }

        progressBar.progressTintList = ColorStateList.valueOf(getColor(R.color.colorProgressDone))
        progressBar.max = item.metaInfos!!["duration"].toString().toFloat().toInt()
        progressBar.progress = item.position!!
        progressBar.visibility = View.VISIBLE
    }

    private fun getConvertStatus(
        html5VideoStatus: Html5Status?,
        item: Item,
        initProgressBar: ProgressBar?,
        initHtml5ImageView: ImageView?
    ) {
        var progressBar = initProgressBar
        var html5ImageView = initHtml5ImageView
        var actualHtml5VideoStatus = html5VideoStatus
        this.setHtml5StatusColor(actualHtml5VideoStatus, progressBar, html5ImageView)

        html5ImageView?.visibility = View.VISIBLE
        progressBar?.visibility = View.VISIBLE

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
                            progressBar?.progress = 0
                        }

                        if (actualHtml5VideoStatus == Html5Status.WAIT) {
                            Thread.sleep(3000)

                            continue
                        }

                        break
                    }

                    this.setHtml5StatusColor(actualHtml5VideoStatus, progressBar, html5ImageView)
                    this.runOnUiThread {
                        progressBar?.max = convertStatus.frames
                        progressBar?.progress = convertStatus.frame!!
                        progressBar?.visibility = View.VISIBLE
                    }
                } catch (_: TaskException) {
                } catch (_: ResponseException) {
                }

                Thread.sleep(1000)

                val view = this.getViewByItem(item)
                progressBar = view?.findViewById(R.id.position)
                html5ImageView = view?.findViewById(R.id.html5)
            }

            this.setHtml5StatusColor(actualHtml5VideoStatus, progressBar, html5ImageView)
        })
    }

    private fun setHtml5StatusColor(
        html5VideoStatus: Html5Status?,
        progressBar: ProgressBar?,
        html5ImageView: ImageView?
    ) {
        if (
            html5VideoStatus === null ||
            progressBar === null ||
            html5ImageView === null
        ) {
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable(DIRECTORY_KEY, this.loadedDir)
    }

    override fun getId() = this.loadDir.toString()

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        this.chromecastService.onCreateOptionsMenu(menu)

        return true
    }

    override fun onResume() {
        super.onResume()
        this.chromecastService.onResume()
    }

    override fun onPause() {
        super.onPause()
        this.chromecastService.onPause()
    }
}