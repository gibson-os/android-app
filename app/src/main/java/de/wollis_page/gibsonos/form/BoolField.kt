package de.wollis_page.gibsonos.form

import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import com.google.android.material.textfield.TextInputLayout
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.FormActivity
import de.wollis_page.gibsonos.dto.form.Field

class BoolField: FieldInterface {
    override fun build(
        field: Field,
        context: FormActivity,
        onValueChange: () -> Unit,
        getConfig: (config: Map<String, Any>) -> Unit,
    ): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(
            R.layout.base_form_checkbox_field,
            context.findViewById(android.R.id.content),
            false
        ) as TextInputLayout

        val checkbox = view.findViewById<CheckBox>(R.id.field)
        checkbox.text = field.title
        checkbox.setOnClickListener { onValueChange() }

        return view
    }

    override fun supports(field: Field): Boolean =
        field.xtype == "gosCoreComponentFormFieldCheckbox"

    override fun getValue(view: View, field: Field, config: Map<String, Any>?): Any {
        val isChecked = view.findViewById<CheckBox>(R.id.field).isChecked

        if (isChecked) {
            return field.config["inputValue"] ?: true
        }

        return field.config["uncheckedValue"] ?: false
    }

    override fun setValue(view: View, field: Field, value: Any?, config: Map<String, Any>?) {
        var boolValue = false

        if (value is Boolean) {
            boolValue = value
        } else if (value != null) {
            val inputValue = field.config["inputValue"]
            val uncheckedValue = field.config["uncheckedValue"]

            if (inputValue != null && value.toString() == inputValue.toString()) {
                boolValue = true
            } else if (uncheckedValue != null && value.toString() == uncheckedValue.toString()) {
                boolValue = false
            } else {
                val stringValue = value.toString().lowercase()
                boolValue = stringValue == "true" || stringValue == "1"
            }
        }

        view.findViewById<CheckBox>(R.id.field).isChecked = boolValue
    }
}
