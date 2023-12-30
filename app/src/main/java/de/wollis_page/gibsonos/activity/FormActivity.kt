package de.wollis_page.gibsonos.activity

import android.os.Bundle
import android.widget.LinearLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.Form

abstract class FormActivity: GibsonOsActivity() {
    lateinit var formContainer: LinearLayout
    override fun getContentView(): Int = R.layout.base_form

    protected abstract fun getForm(): Form

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.formContainer = this.findViewById(R.id.form) as LinearLayout
        val form = this.getForm()

        form.fields.forEach {
            val field = when (it.value.xtype) {
                "gosCoreComponentFormFieldTextField" -> TextInputLayout(this)
                else -> TextInputLayout(this)
            }

            val hint = TextInputEditText(this)
            hint.hint = it.value.title
            field.addView(hint)

            this.formContainer.addView(field)
        }
    }

    protected fun loadForm(run: () -> Form): Form
    {
        var form = Form()

        this.runTask({
            form = run()
        })

        return form
    }
}