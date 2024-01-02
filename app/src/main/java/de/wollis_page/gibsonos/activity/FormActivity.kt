package de.wollis_page.gibsonos.activity

import android.os.Bundle
import android.widget.LinearLayout
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.Form
import de.wollis_page.gibsonos.form.AutoCompleteField
import de.wollis_page.gibsonos.form.FieldInterface
import de.wollis_page.gibsonos.form.TextField

abstract class FormActivity: GibsonOsActivity() {
    private var fields: Array<FieldInterface> = arrayOf(
        TextField(),
        AutoCompleteField(),
    )
    private lateinit var formContainer: LinearLayout
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
            }
        }
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