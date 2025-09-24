package de.wollis_page.gibsonos.module.growDiary.setup.activity

import de.wollis_page.gibsonos.activity.FormActivity
import de.wollis_page.gibsonos.dto.form.Button
import de.wollis_page.gibsonos.module.growDiary.task.SetupTask
import org.json.JSONObject

class ClimateControlFormActivity: FormActivity() {
    override fun buildForm() = this.loadForm {
        var climateControl: Long? = this.intent.getLongExtra("climateControlId", 0)

        if (climateControl?.toInt() == 0) {
            climateControl = null
        }

        SetupTask.climateControlForm(
            this,
            this.intent.getLongExtra("setupId", 0),
            climateControl,
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