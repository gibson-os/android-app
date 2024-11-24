package de.wollis_page.gibsonos.module.growDiary.feed.activity

import de.wollis_page.gibsonos.activity.FormActivity
import de.wollis_page.gibsonos.dto.form.Button
import de.wollis_page.gibsonos.module.growDiary.task.FeedTask
import org.json.JSONObject

class FormActivity: FormActivity() {
    override fun buildForm() = this.loadForm {
        var feedId: Long? = this.intent.getLongExtra("feedId", 0)

        if (feedId?.toInt() == 0) {
            feedId = null
        }

        FeedTask.form(
            this,
            this.intent.getLongExtra("plantId", 0),
            feedId,
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