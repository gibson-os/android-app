package de.wollis_page.gibsonos.form

import android.view.LayoutInflater
import android.view.View
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.form.Field

class TextField: FieldInterface {
    override fun getView(field: Field, context: GibsonOsActivity): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(
            R.layout.base_form_text_field,
            context.findViewById(android.R.id.content),
            false
        ) as TextInputLayout

        view.hint = field.title

        return view
    }

    override fun supports(field: Field): Boolean =
        field.xtype == "gosCoreComponentFormFieldTextField"

    override fun getValue(view: View, field: Field): Any? =
        view.findViewById<TextInputEditText>(R.id.field).text

    override fun setValue(view: View, field: Field, value: Any?) {
        view.findViewById<TextInputEditText>(R.id.field).setText(value?.toString() ?: "")
    }
}