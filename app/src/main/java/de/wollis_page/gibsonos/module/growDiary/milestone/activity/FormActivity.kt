package de.wollis_page.gibsonos.module.growDiary.milestone.activity

import de.wollis_page.gibsonos.activity.FormActivity
import de.wollis_page.gibsonos.dto.form.Button
import de.wollis_page.gibsonos.module.growDiary.task.MilestoneTask
import org.json.JSONObject

class FormActivity: FormActivity() {
    override fun buildForm() = this.loadForm {
        var milestoneId: Long? = this.intent.getLongExtra("milestoneId", 0)

        if (milestoneId?.toInt() == 0) {
            milestoneId = null
        }

        MilestoneTask.getForm(
            this,
            this.intent.getLongExtra("plantId", 0),
            milestoneId,
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