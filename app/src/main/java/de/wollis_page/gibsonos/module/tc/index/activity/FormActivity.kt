package de.wollis_page.gibsonos.module.tc.index.activity

import de.wollis_page.gibsonos.activity.FormActivity
import de.wollis_page.gibsonos.module.tc.task.FormTask

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
}