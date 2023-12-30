package de.wollis_page.gibsonos.module.obscura.scanner.activity

import de.wollis_page.gibsonos.activity.FormActivity
import de.wollis_page.gibsonos.dto.Form
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.module.obscura.task.ScannerTask

class FormActivity: FormActivity() {
    override fun getForm(): Form = this.loadForm {
        ScannerTask.getForm(
            this,
            this.intent.getStringExtra("deviceName").toString(),
            this.intent.getStringExtra("vendor").toString(),
            this.intent.getStringExtra("model").toString(),
        )
    }

    override fun getId(): Int = 0

    override fun isActivityforShotcut(shortcut: Shortcut): Boolean = true
}