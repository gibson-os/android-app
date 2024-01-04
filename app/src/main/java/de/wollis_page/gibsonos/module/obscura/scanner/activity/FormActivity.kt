package de.wollis_page.gibsonos.module.obscura.scanner.activity

import android.view.View
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.FormActivity
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.dto.Message
import de.wollis_page.gibsonos.dto.form.Button
import de.wollis_page.gibsonos.exception.ResponseException
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.helper.MessageBuilder
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

    override fun afterButtonClick(
        name: String,
        formButton: Button,
        button: android.widget.Button,
        response: JSONObject,
    ) {
        if (name != "scan") {
            return
        }

        this.showLoading()
        this.loadStatus()
        this.hideLoading()
    }

    private fun loadStatus(lastCheck: String? = null) {
        try {
            val status = ScannerTask.getStatus(
                this,
                this.intent.getStringExtra("deviceName").toString(),
                lastCheck,
            )

            if (status.locked) {
                Thread.sleep(500)

                this.loadStatus(status.date)
            }
        } catch (exception: ResponseException) {
            val response = JSONObject(exception.response)
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val message = moshi.adapter(Message::class.java).fromJson(response.getJSONObject("data").toString()) ?:
                throw TaskException("No message found!")

            this.runOnUiThread {
                val messageBuilder = MessageBuilder()
                messageBuilder.build(this, message) {
                    val button = this.getButton("scan")
                    val oldParameters = button.parameters
                    button.parameters = oldParameters.plus(message.extraParameters)
                    val buttonView = this.getButtonsView("scan")
                    buttonView.callOnClick()
                    button.parameters = oldParameters
                    null
                }.show()
            }
        }
    }
}