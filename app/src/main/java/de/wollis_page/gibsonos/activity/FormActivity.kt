package de.wollis_page.gibsonos.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.Form
import de.wollis_page.gibsonos.dto.form.Field
import de.wollis_page.gibsonos.exception.FormException
import de.wollis_page.gibsonos.form.AutoCompleteField
import de.wollis_page.gibsonos.form.BoolField
import de.wollis_page.gibsonos.form.DateField
import de.wollis_page.gibsonos.form.DateTimeField
import de.wollis_page.gibsonos.form.DirectoryField
import de.wollis_page.gibsonos.form.FieldInterface
import de.wollis_page.gibsonos.form.NumberField
import de.wollis_page.gibsonos.form.OptionField
import de.wollis_page.gibsonos.form.StringField
import de.wollis_page.gibsonos.task.FormTask
import org.json.JSONObject
import de.wollis_page.gibsonos.dto.form.Button as FormButton

abstract class FormActivity: GibsonOsActivity() {
    private var fields: Array<FieldInterface> = arrayOf(
        StringField(),
        NumberField(),
        OptionField(),
        BoolField(),
        AutoCompleteField(),
        DirectoryField(),
        DateTimeField(),
        DateField(),
    )
    private lateinit var formContainer: LinearLayout
    private var formViews: MutableMap<String, View> = mutableMapOf()
    private var formFields: MutableMap<String, Field> = mutableMapOf()
    private var formButtons: MutableMap<String, FormButton> = mutableMapOf()
    private var formFieldBuilders: MutableMap<String, FieldInterface> = mutableMapOf()
    private var formConfig: MutableMap<String, Map<String, Any>> = mutableMapOf()
    private var buttonViews: MutableMap<String, Button> = mutableMapOf()
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

                val fieldView = it.build(field, this) {
                    this.formConfig[formField.key] = it
                }
                val value = field.value

                it.setValue(fieldView, field, value, this.formConfig[formField.key])

                this.formViews[formField.key] = fieldView
                this.formFields[formField.key] = field
                this.formFieldBuilders[formField.key] = it
                this.formContainer.addView(fieldView)
            }
        }

        form.buttons.forEach { formButton ->
            val button = Button(this)
            button.text = formButton.value.text
            button.setOnClickListener {
                var parameters = this.getValues()
                parameters = parameters.plus(formButton.value.parameters)

                this.runTask({
                    val response = FormTask.post(
                        this,
                        formButton.value.module,
                        formButton.value.task,
                        formButton.value.action,
                        parameters,
                    )
                    this.afterButtonClick(formButton.key, formButton.value, button, response)
                })
            }

            this.buttonViews[formButton.key] = button
            this.formButtons[formButton.key] = formButton.value
            this.formContainer.addView(button)
        }

        this.afterBuild()
    }

    protected open fun afterBuild() {
    }

    protected open fun afterButtonClick(
        name: String,
        formButton: FormButton,
        button: Button,
        response: JSONObject,
    ) {
    }

    fun getValues(): Map<String, *> {
        val values = mutableMapOf<String, Any>()

        this.formFields.forEach {
            values[it.key] = this.getFieldBuilder(it.key).getValue(
                this.getView(it.key),
                it.value,
            ) ?: ""
        }

        return values
    }

    fun getValue(fieldName: String) : Any? {
        val field = this.getField(fieldName)
        val view = this.getView(fieldName)
        val fieldBuilder = this.getFieldBuilder(fieldName)

        return fieldBuilder.getValue(view, field)
    }

    fun setValues(values: Map<String, *>) {
        values.forEach {
            this.getFieldBuilder(it.key).setValue(
                this.getView(it.key),
                this.getField(it.key),
                it.value,
                this.formConfig[it.key],
            )
        }
    }

    fun setValue(fieldName: String, value: Any?) {
        val field = this.getField(fieldName)
        val view = this.getView(fieldName)
        val fieldBuilder = this.getFieldBuilder(fieldName)

        return fieldBuilder.setValue(view, field, value, this.formConfig[fieldName])
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

    fun getView(fieldName: String): View = this.formViews[fieldName]
        ?: throw FormException("Field '$fieldName' doesn't exists")

    protected fun getField(fieldName: String): Field = this.formFields[fieldName]
        ?: throw FormException("Field '$fieldName' doesn't exists")

    protected fun getFieldBuilder(fieldName: String): FieldInterface = this.formFieldBuilders[fieldName]
        ?: throw FormException("Field '$fieldName' doesn't exists")

    protected fun getConfig(fieldName: String): Map<String, Any> = this.formConfig[fieldName]
        ?: throw FormException("No config found for field '$fieldName'")

    protected fun getButtonsView(buttonName: String): View = this.buttonViews[buttonName]
        ?: throw FormException("Button '$buttonName' doesn't exists")

    protected fun getButton(buttonName: String): FormButton = this.formButtons[buttonName]
        ?: throw FormException("Button '$buttonName' doesn't exists")
}