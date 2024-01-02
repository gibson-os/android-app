package de.wollis_page.gibsonos.form

import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.form.Field
import de.wollis_page.gibsonos.exception.AppException
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.module.core.task.AutoCompleteTask
import kotlin.reflect.full.declaredMemberProperties

class AutoCompleteField: FieldInterface {
    override fun getView(field: Field, context: GibsonOsActivity): View {
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
            val valueField = field.config["valueField"]
            val displayField = field.config["displayField"]
            val options: MutableMap<String, String> = mutableMapOf()
            val fieldView = view.findViewById<AutoCompleteTextView>(R.id.field)
            var items: Array<String> = emptyArray()

            response.data.forEach {
                if (it == null || !clazz.isInstance(it)) {
                    throw AppException("Wrong instance")
                }

                val optionValue = it::class.declaredMemberProperties.find { it.name == valueField }!!.getter.call(it).toString()
                val optionName = it::class.declaredMemberProperties.find { it.name == displayField }!!.getter.call(it).toString()
                options[optionName] = optionValue
            }

//            field.config["options"] = options

            options.forEach {
                items = items.plus(it.key)
            }

            context.runOnUiThread {
                fieldView.setAdapter(ArrayAdapter(
                    context,
                    R.layout.base_dropdown_item,
                    items,
                ))
            }
        })

        view.hint = field.title

        return view
    }

    override fun supports(field: Field): Boolean =
        field.xtype == "gosModuleCoreParameterTypeAutoComplete"

    override fun getValue(view: View, field: Field): Any? =
        view.findViewById<AutoCompleteTextView>(R.id.field).text

    override fun setValue(view: View, field: Field, value: Any?) {
        view.findViewById<AutoCompleteTextView>(R.id.field).setText(value?.toString() ?: "")
    }
}