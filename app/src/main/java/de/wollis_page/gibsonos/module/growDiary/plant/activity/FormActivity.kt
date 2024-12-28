package de.wollis_page.gibsonos.module.growDiary.plant.activity

import de.wollis_page.gibsonos.activity.FormActivity
import de.wollis_page.gibsonos.dto.form.Button
import de.wollis_page.gibsonos.module.growDiary.task.PlantTask
import org.json.JSONObject

class FormActivity: FormActivity() {
    override fun buildForm() = this.loadForm {
        var plantId: Long? = this.intent.getLongExtra("plantId", 0)

        if (plantId?.toInt() == 0) {
            plantId = null
        }

        PlantTask.getForm(
            this,
            plantId,
        )
    }

    override fun getId() = 0

    override fun afterButtonClick(
        name: String,
        formButton: Button,
        button: android.widget.Button,
        response: JSONObject
    ) {
        this.setResult(RESULT_OK)
        this.finish()
    }
}