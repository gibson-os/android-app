package de.wollis_page.gibsonos.form

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.FormActivity
import de.wollis_page.gibsonos.dto.form.Field
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class NumberField: FieldInterface {
    override fun build(
        field: Field,
        context: FormActivity,
        getConfig: (config: Map<String, Any>) -> Unit,
    ): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(
            R.layout.base_form_number_field,
            context.findViewById(android.R.id.content),
            false
        ) as TextInputLayout

        if (field.config.containsKey("decimals")) {
            view.findViewById<TextInputEditText>(R.id.field).inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        }

        this.addListeners(field, context, view)

        view.hint = field.title

        return view
    }

    override fun supports(field: Field): Boolean =
        field.xtype == "gosCoreComponentFormFieldNumberField"

    override fun getValue(view: View, field: Field, config: Map<String, Any>?): Any? =
        view.findViewById<TextInputEditText>(R.id.field).text

    override fun setValue(view: View, field: Field, value: Any?, config: Map<String, Any>?) {
        view.findViewById<TextInputEditText>(R.id.field).setText(value?.toString() ?: "")
    }

    private fun addListeners(
        field: Field,
        context: FormActivity,
        view: TextInputLayout,
    ) {
        val listeners = field.config["listeners"]

        if (listeners !is Map<*, *>) {
            return
        }

        val numberSymbols = DecimalFormatSymbols(Locale.getDefault())
        numberSymbols.decimalSeparator = '.'
        numberSymbols.groupingSeparator = ','

        listeners.forEach {
            val sourceField = (context.getView(it.key.toString()) as TextInputLayout)
                .findViewById<TextInputEditText>(R.id.field)
            val value = it.value

            if (value is Map<*, *>) {
                value.forEach { listener ->
                    if (listener.key == "multiplier") {
                        sourceField.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(
                                s: CharSequence?,
                                start: Int,
                                count: Int,
                                after: Int
                            ) {
                            }

                            override fun onTextChanged(
                                s: CharSequence?,
                                start: Int,
                                before: Int,
                                count: Int
                            ) {
                            }

                            override fun afterTextChanged(editable: Editable?) {
                                val sourceValue = editable.toString().toFloatOrNull() ?: return

                                val newEditable = Editable.Factory.getInstance().newEditable(
                                    DecimalFormat("#.##", numberSymbols).format(sourceValue * listener.value.toString().toDouble())
                                )
                                view.editText?.text = newEditable
                            }

                        })
                    }
                }
            }
        }
    }
}