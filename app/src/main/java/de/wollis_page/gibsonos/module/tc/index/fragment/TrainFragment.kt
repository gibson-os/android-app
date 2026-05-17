package de.wollis_page.gibsonos.module.tc.index.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.fragment.ListFragment
import de.wollis_page.gibsonos.module.tc.index.dto.Train
import de.wollis_page.gibsonos.module.tc.task.TrainTask
import de.wollis_page.gibsonos.service.ActivityLauncherService
import de.wollis_page.gibsonos.service.ImageLoaderService

class TrainFragment: ListFragment() {
    private lateinit var imageLoaderService: ImageLoaderService<Train>
    lateinit var formLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.formLauncher = this.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode != Activity.RESULT_OK) {
                return@registerForActivityResult
            }

            this.loadList()
        }

        this.imageLoaderService = ImageLoaderService(
            this.activity,
            {
                TrainTask.getImage(
                    this.activity,
                    it.id,
                    this.resources.getDimension(R.dimen.thumb_width).toInt()
                )
            },
            { train, image ->
                this.getViewByItem(train)?.findViewById<ImageView>(R.id.image)?.setImageBitmap(image)
            }
        )
    }

    override fun onClick(item: ListItemInterface) {
        if (item !is Train) {
            return
        }

        this.runTask({
            ActivityLauncherService.startActivity(
                this.activity,
                "tc",
                "index",
                "form",
                mapOf(
                    "task" to "train",
                    "action" to "controlForm",
                    "id" to item.id,
                ),
            )
        })
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Train) {
            return
        }

        view.findViewById<TextView>(R.id.name).text = item.name

        this.imageLoaderService.viewImage(
            item,
            view.findViewById(R.id.image),
            R.drawable.ic_train,
        )
    }

    override fun loadList(start: Long, limit: Long) = this.load {
        this.listAdapter.setListResponse(TrainTask.getList(this, start, limit))
    }

    override fun getListRessource() = R.layout.tc_train_list_item

    override fun actionButton() = R.layout.base_button_add

    override fun actionOnClickListener() {
        this.runTask({
            ActivityLauncherService.startActivity(
                this.activity,
                "tc",
                "index",
                "form",
                mapOf(
                    "task" to "train",
                    "action" to "form",
                    "closeAfterButtonClick" to true,
                ),
                this.formLauncher,
            )
        })
    }

    override fun actionView() = this.activity.findViewById<FloatingActionButton>(R.id.addButton)

    override fun getDeleteTitle() = this.getString(R.string.tc_train_delete_title)

    override fun getDeleteMessage(item: ListItemInterface) = this.getString(
        R.string.tc_train_delete_message,
        if (item is Train) item.name.trim() else ""
    )

    override fun deleteItem(item: ListItemInterface): Boolean = this.delete(item) {
        TrainTask.delete(this.activity, item as Train)
    }
}