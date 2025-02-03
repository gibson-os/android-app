package de.wollis_page.gibsonos.module.growDiary.climate.activity

import de.wollis_page.gibsonos.activity.FormActivity
import de.wollis_page.gibsonos.dto.form.Button
import de.wollis_page.gibsonos.module.growDiary.task.ClimateTask
import org.json.JSONObject

class FormActivity: FormActivity() {
    override fun buildForm() = this.loadForm {
        var climateId: Long? = this.intent.getLongExtra("climateId", 0)

        if (climateId?.toInt() == 0) {
            climateId = null
        }

        var plantId: Long? = this.intent.getLongExtra("plantId", 0)

        if (plantId?.toInt() == 0) {
            plantId = null
        }

        var setupId: Long? = this.intent.getLongExtra("setupId", 0)

        if (setupId?.toInt() == 0) {
            setupId = null
        }

        ClimateTask.getForm(this, plantId, setupId, climateId)
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