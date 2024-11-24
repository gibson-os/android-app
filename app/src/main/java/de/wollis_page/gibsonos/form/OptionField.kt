package de.wollis_page.gibsonos.form

import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.FormActivity
import de.wollis_page.gibsonos.dto.form.Field

class OptionField: FieldInterface {
    override fun build(
        field: Field,
        context: FormActivity,
        getConfig: (config: Map<String, Any>) -> Unit,
    ): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(
            R.layout.base_form_option_field,
            context.findViewById(android.R.id.content),
            false
        ) as TextInputLayout
        val fieldView = view.findViewById<AutoCompleteTextView>(R.id.field)
        val options = field.config["options"] as Map<*, *>
        var items: Array<String> = emptyArray()

        options.forEach {
            items = items.plus(it.key.toString())
        }

        view.hint = field.title
        fieldView.setAdapter(ArrayAdapter(
            context,
            R.layout.base_dropdown_item,
            items,
        ))

        return view
    }

    override fun supports(field: Field): Boolean =
        field.xtype == "gosCoreComponentFormFieldComboBox"

    override fun getValue(view: View, field: Field): Any? =
        (field.config["options"] as Map<*, *>)[view.findViewById<AutoCompleteTextView>(R.id.field).text.toString()]

    override fun setValue(view: View, field: Field, value: Any?) {
        view.findViewById<AutoCompleteTextView>(R.id.field).setText(
            (this.getOptionByValue(field, value)?.key ?: "").toString(),
            false
        )
    }

    private fun getOptionByValue(field: Field, value: Any?): Map.Entry<*, *>? {
        if (value == null) {
            return null
        }

        val options = field.config["options"] as Map<*, *>
        var optionValue: Map.Entry<*, *>? = null

        options.forEach {
            if (it.value.toString() != value.toString()) {
                return@forEach
            }

            optionValue = it
        }

        return optionValue
    }
}