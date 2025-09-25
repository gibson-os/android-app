package de.wollis_page.gibsonos.module.growDiary.index.activity

import de.wollis_page.gibsonos.activity.FormActivity
import de.wollis_page.gibsonos.dto.form.Button
import de.wollis_page.gibsonos.module.growDiary.task.FormTask
import org.json.JSONObject

class FormActivity: FormActivity() {
    override fun buildForm() = this.loadForm {
        var id: Long? = this.intent.getLongExtra("id", 0)

        if (id?.toInt() == 0) {
            id = null
        }

        FormTask.getForm(
            this,
            this.intent.getStringExtra("task")!!,
            this.intent.getStringExtra("action") ?: "form",
            id,
            this.intent.getSerializableExtra("additionalParameters") as HashMap<String, String>? ?: hashMapOf(),
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