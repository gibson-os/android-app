package de.wollis_page.gibsonos.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.Form
import de.wollis_page.gibsonos.dto.form.Field
import de.wollis_page.gibsonos.form.AutoCompleteField
import de.wollis_page.gibsonos.form.BoolField
import de.wollis_page.gibsonos.form.DirectoryField
import de.wollis_page.gibsonos.form.FieldInterface
import de.wollis_page.gibsonos.form.OptionField
import de.wollis_page.gibsonos.form.StringField
import de.wollis_page.gibsonos.task.FormTask

abstract class FormActivity: GibsonOsActivity() {
    private var fields: Array<FieldInterface> = arrayOf(
        StringField(),
        OptionField(),
        BoolField(),
        AutoCompleteField(),
        DirectoryField(),
    )
    private lateinit var formContainer: LinearLayout
    private var formViews: MutableMap<String, View> = mutableMapOf()
    private var formFields: MutableMap<String, Field> = mutableMapOf()
    private var formFieldBuilders: MutableMap<String, FieldInterface> = mutableMapOf()
    override fun getContentView(): Int = R.layout.base_form

    protected abstract fun buildForm()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.formContainer = this.findViewById(R.id.form) as LinearLayout
        this.buildForm()
    }

    protected fun setForm(form: Form) {
        form.fields.forEach { formField ->
            val field = formField.value

            this.fields.forEach fieldEach@ {
                if (!it.supports(field)) {
                    return@fieldEach
                }

                val fieldView = it.getView(field, this)
                val value = field.value

                it.setValue(fieldView, field, value)

                this.formContainer.addView(fieldView)
                this.formViews[formField.key] = fieldView
                this.formFields[formField.key] = field
                this.formFieldBuilders[formField.key] = it
            }
        }

        form.buttons.forEach { formButton ->
            val button = Button(this)
            button.text = formButton.value.text
            button.setOnClickListener {
                var parameters = this.getValues()
                parameters = parameters.plus(formButton.value.parameters)

                this.runTask({
                    FormTask.post(
                        this,
                        formButton.value.module,
                        formButton.value.task,
                        formButton.value.action,
                        parameters,
                    )
                })
            }

            this.formContainer.addView(button)
        }
    }

    fun getValues(): Map<String, *> {
        val values = mutableMapOf<String, Any>()

        this.formFields.forEach {
            values[it.key] = this.formFieldBuilders[it.key]?.getValue(
                this.formViews[it.key]!!,
                it.value,
            ) ?: ""
        }

        return values
    }

    protected fun loadForm(run: () -> Form)
    {
        this.runTask({
            val form = run()
            this.runOnUiThread {
                this.setForm(form)
            }
        })
    }
}