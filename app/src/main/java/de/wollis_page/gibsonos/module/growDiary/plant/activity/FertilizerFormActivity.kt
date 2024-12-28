package de.wollis_page.gibsonos.module.growDiary.plant.activity

import de.wollis_page.gibsonos.activity.FormActivity
import de.wollis_page.gibsonos.dto.form.Button
import de.wollis_page.gibsonos.module.growDiary.task.PlantTask
import org.json.JSONObject

class FertilizerFormActivity: FormActivity() {
    override fun buildForm() = this.loadForm {
        var fertilizerId: Long? = this.intent.getLongExtra("fertilizerId", 0)

        if (fertilizerId?.toInt() == 0) {
            fertilizerId = null
        }

        PlantTask.getFertilizerForm(
            this,
            this.intent.getLongExtra("plantId", 0),
            fertilizerId,
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