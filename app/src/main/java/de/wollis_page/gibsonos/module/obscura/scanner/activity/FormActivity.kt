package de.wollis_page.gibsonos.module.obscura.scanner.activity

import android.view.View
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.FormActivity
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.dto.form.Button
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.module.obscura.task.ScannerTask
import de.wollis_page.gibsonos.module.obscura.template.dto.Template
import org.json.JSONObject

class FormActivity: FormActivity() {
    override fun buildForm() = this.loadForm {
        ScannerTask.getForm(
            this,
            this.intent.getStringExtra("deviceName").toString(),
            this.intent.getStringExtra("vendor").toString(),
            this.intent.getStringExtra("model").toString(),
        )
    }

    override fun getId(): Int = 0

    override fun isActivityforShotcut(shortcut: Shortcut): Boolean = true

    override fun afterBuild() {
        val templateView = this.getView("name") as TextInputLayout
        val autocompleteView = templateView.findViewById<AutoCompleteTextView>(R.id.field)
        val form = this

        autocompleteView.onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, selectedView: View?, position: Int, id: Long) {
                val autocompleteConfig = form.getConfig("name")
                val template = (autocompleteConfig["response"] as ListResponse<Template>).data[position]

                form.setValue("name", template.name)
                form.setValue("path", template.path)
                form.setValue("filename", template.filename)
                form.setValue("multipage", template.multipage)
                form.setValue("format", template.format)

                template.options.forEach {
                    form.setValue("options[" + it.key + "]", it.value)
                }
            }
        }
    }

    override fun afterButtonClick(formButton: Button, button: android.widget.Button, response: JSONObject) {
        if (
            formButton.module != "obscura" &&
            formButton.task != "scanner" &&
            formButton.action != "scan"
        ) {
            return
        }

        this.showLoading()
        this.loadStatus()
        this.hideLoading()
    }

    private fun loadStatus(lastCheck: String? = null) {
        val status = ScannerTask.getStatus(
            this,
            this.intent.getStringExtra("deviceName").toString(),
            lastCheck,
        )

        if (status.locked) {
            Thread.sleep(500)
            this.loadStatus(status.date)
        }
    }
}