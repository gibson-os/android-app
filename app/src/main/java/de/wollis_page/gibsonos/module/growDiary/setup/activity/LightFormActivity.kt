package de.wollis_page.gibsonos.module.growDiary.setup.activity

import de.wollis_page.gibsonos.activity.FormActivity
import de.wollis_page.gibsonos.dto.form.Button
import de.wollis_page.gibsonos.module.growDiary.task.SetupTask
import org.json.JSONObject

class LightFormActivity: FormActivity() {
    override fun buildForm() = this.loadForm {
        var light: Long? = this.intent.getLongExtra("lightId", 0)

        if (light?.toInt() == 0) {
            light = null
        }

        SetupTask.lightForm(
            this,
            this.intent.getLongExtra("setupId", 0),
            light,
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