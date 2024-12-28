package de.wollis_page.gibsonos.form

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.FormActivity
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.dto.form.Field
import de.wollis_page.gibsonos.exception.AppException
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.helper.Config
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
            val displayField = field.config["displayField"]
            val fieldView = view.findViewById<AutoCompleteTextView>(R.id.field)
            var items: Array<String> = emptyArray()

            response.data.forEach {
                if (it == null || !clazz.isInstance(it)) {
                    throw AppException("Wrong instance")
                }

                val optionName = it::class.declaredMemberProperties.find { it.name == displayField }!!.getter.call(it).toString()
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

            getConfig(mapOf("response" to response))
        })

        view.hint = field.title

        return view
    }

    override fun supports(field: Field): Boolean =
        field.xtype == "gosModuleCoreParameterTypeAutoComplete"

    override fun getValue(view: View, field: Field): Any? =
        view.findViewById<AutoCompleteTextView>(R.id.value).text

    override fun setValue(view: View, field: Field, value: Any?, config: Map<String, Any>?) {
        view.findViewById<TextView>(R.id.value).text = value?.toString() ?: ""
Log.d(Config.LOG_TAG, value.toString())
        val response = config?.get("response") ?: return

        this.setViewValue(view, field, response as ListResponse<*>)
    }

    private fun setViewValue(view: View, field: Field, response: ListResponse<*>) {
        val textField = view.findViewById<AutoCompleteTextView>(R.id.field)
        val value = view.findViewById<TextView>(R.id.value).text
        val valueFieldName = field.config["valueField"].toString()
        val displayField = field.config["displayField"]

        response.data.forEach {
            if (it == null) {
                throw AppException("Wrong instance")
            }

            var itemValue = it::class.declaredMemberProperties.find { it.name == valueFieldName }!!.getter.call(it)

            if (itemValue is Long) {
                itemValue = itemValue.toFloat()
            }

            if (itemValue is Int) {
                itemValue = itemValue.toFloat()
            }

            itemValue = itemValue.toString()

            if (itemValue == value.toString()) {
                textField.setText(it::class.declaredMemberProperties.find { it.name == displayField }!!.getter.call(it).toString())

                return
            }
        }
    }
}