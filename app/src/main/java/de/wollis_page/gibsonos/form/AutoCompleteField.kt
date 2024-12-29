package de.wollis_page.gibsonos.form

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.FormActivity
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.dto.form.Field
import de.wollis_page.gibsonos.exception.AppException
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.module.core.task.AutoCompleteTask
import kotlin.reflect.full.declaredMemberProperties

class AutoCompleteField: FieldInterface {
    override fun build(
        field: Field,
        context: FormActivity,
        getConfig: (config: Map<String, Any>) -> Unit,
    ): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(
            R.layout.base_form_auto_complete_field,
            context.findViewById(android.R.id.content),
            false
        ) as TextInputLayout

        val autoCompleteClassname = field.config["autoCompleteClassname"].toString()

        context.runTask({
            val clazz = AutoCompleteTask.getDtoMapping()[autoCompleteClassname]
                ?: throw TaskException("$autoCompleteClassname has no mapping")
            val response = AutoCompleteTask.get(
                context,
                autoCompleteClassname,
                field.config["parameters"] as Map<String, String>,
            )
            val displayField = field.config["displayField"].toString()
            val fieldView = view.findViewById<AutoCompleteTextView>(R.id.field)
            var items: Array<String> = emptyArray()

            response.data.forEach {
                if (it == null || !clazz.isInstance(it)) {
                    throw AppException("Wrong instance")
                }

                val optionName = this.getItemDisplayValue(it, displayField)
                items = items.plus(optionName)
            }

            context.runOnUiThread {
                fieldView.setAdapter(ArrayAdapter(
                    context,
                    R.layout.base_dropdown_item,
                    items,
                ))
                this.setViewValue(view, field, response)
            }

            getConfig(mapOf(
                "response" to response,

            ))
        })

        view.hint = field.title

        return view
    }

    override fun supports(field: Field): Boolean =
        field.xtype == "gosModuleCoreParameterTypeAutoComplete"

    override fun getValue(view: View, field: Field, config: Map<String, Any>?): Any? {
        val displayValue = view.findViewById<AutoCompleteTextView>(R.id.field).text.toString()
        val response = (config?.get("response") ?: return null) as ListResponse<*>
        val valueFieldName = field.config["valueField"].toString()
        val displayFieldName = field.config["displayField"].toString()
        var returnValue: Any? = null

        response.data.forEach {
            val itemDisplayValue = this.getItemDisplayValue(it!!, displayFieldName)

            if (itemDisplayValue == displayValue) {
                returnValue = this.getItemValue(it, valueFieldName)
            }
        }

        if (returnValue == null) {
            return displayValue
        }

        return returnValue
    }

    override fun setValue(view: View, field: Field, value: Any?, config: Map<String, Any>?) {
        view.findViewById<TextView>(R.id.value).text = value?.toString() ?: ""

        val response = (config?.get("response") ?: return) as ListResponse<*>

        this.setViewValue(view, field, response)
    }

    private fun setViewValue(view: View, field: Field, response: ListResponse<*>) {
        val textField = view.findViewById<AutoCompleteTextView>(R.id.field)
        val value = view.findViewById<TextView>(R.id.value).text
        val valueFieldName = field.config["valueField"].toString()
        val displayFieldName = field.config["displayField"].toString()

        response.data.forEach {
            var itemValue = this.getItemValue(it!!, valueFieldName)

            if (itemValue is Long) {
                itemValue = itemValue.toFloat()
            }

            if (itemValue is Int) {
                itemValue = itemValue.toFloat()
            }

            itemValue = itemValue.toString()

            if (itemValue == value.toString()) {
                textField.setText(this.getItemDisplayValue(it, displayFieldName))

                return
            }
        }
    }

    private fun getItemDisplayValue(item: Any, displayFieldName: String) =
        item::class.declaredMemberProperties.find { it.name == displayFieldName }!!.getter.call(item).toString()

    private fun getItemValue(item: Any, valueFieldName: String) =
        item::class.declaredMemberProperties.find { it.name == valueFieldName }!!.getter.call(item)

    private fun addListeners(
        field: Field,
        context: FormActivity,
        view: TextInputLayout,
    ) {
        val listeners = field.config["listeners"]

        if (listeners !is Map<*, *>) {
            return
        }

        listeners.forEach {
            val sourceField = (context.getView(it.key.toString()) as TextInputLayout)
                .findViewById<AutoCompleteTextView>(R.id.field)
            val value = it.value

            if (value is Map<*, *>) {
                value.forEach { listener ->
                    if (listener.key == "params") {
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
                                val sourceValue = editable?.toString() ?: return

//                                val newEditable = Editable.Factory.getInstance().newEditable(
//                                    (sourceValue * listener.value.toString().toFloat()).toString()
//                                )
//                                view.editText?.text = newEditable
                            }
                        })
                    }
                }
            }
        }
    }
}