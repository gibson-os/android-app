package de.wollis_page.gibsonos.module.growDiary.setup.activity

import de.wollis_page.gibsonos.activity.FormActivity
import de.wollis_page.gibsonos.dto.form.Button
import de.wollis_page.gibsonos.module.growDiary.task.SetupTask
import org.json.JSONObject

class ClimateControlPlannedRuntimeFormActivity: FormActivity() {
    override fun buildForm() = this.loadForm {
        var plannedRuntimeId: Long? = this.intent.getLongExtra("plannedRuntimeId", 0)

        if (plannedRuntimeId?.toInt() == 0) {
            plannedRuntimeId = null
        }

        SetupTask.climateControlPlannedRuntimeForm(
            this,
            this.intent.getLongExtra("climateControlId", 0),
            plannedRuntimeId,
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