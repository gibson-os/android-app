package de.wollis_page.gibsonos.form

import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import com.google.android.material.textfield.TextInputLayout
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.form.Field

class BoolField: FieldInterface {
    override fun build(
        field: Field,
        context: GibsonOsActivity,
        getConfig: (config: Map<String, Any>) -> Unit,
    ): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(
            R.layout.base_form_checkbox_field,
            context.findViewById(android.R.id.content),
            false
        ) as TextInputLayout

        view.findViewById<CheckBox>(R.id.field).text = field.title

        return view
    }

    override fun supports(field: Field): Boolean =
        field.xtype == "gosCoreComponentFormFieldCheckbox"

    override fun getValue(view: View, field: Field): Any =
        view.findViewById<CheckBox>(R.id.field).isChecked

    override fun setValue(view: View, field: Field, value: Any?) {
        var boolValue = false

        if (value is Boolean) {
            boolValue = value
        }

        if (value is String) {
            if (value.lowercase() == "true") {
                boolValue = true
            }

            if (value.toInt() == 1) {
                boolValue = true
            }
        }

        if ((value is Int || value is Long) && value == 1) {
            boolValue = true
        }

        view.findViewById<CheckBox>(R.id.field).isChecked = boolValue
    }
}