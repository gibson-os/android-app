package de.wollis_page.gibsonos.form

import android.app.TimePickerDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.FormActivity
import de.wollis_page.gibsonos.dto.form.Field
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale

class TimeField: FieldInterface {
    override fun build(
        field: Field,
        context: FormActivity,
        getConfig: (config: Map<String, Any>) -> Unit,
    ): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(
            R.layout.base_form_time_field,
            context.findViewById(android.R.id.content),
            false
        ) as LinearLayout

        view.findViewById<TextInputLayout>(R.id.fieldLayout).hint = field.title

        val button = view.findViewById<ImageButton>(R.id.button)
        button.contentDescription = field.title
        button.setOnClickListener {
            val valueDate = GregorianCalendar()
            val textfield = view.findViewById<TextView>(R.id.field)
            val value = textfield.text.toString()

            valueDate.time = Date()

            if (value != "") {
                valueDate.time = SimpleDateFormat("HH:mm:ss", Locale.GERMAN).parse(value)!!
            }

            val datePickerDialog = TimePickerDialog(
                context,
                { _, hourOfDay, minute ->
                    val selectedDate = GregorianCalendar()
                    val formatter = SimpleDateFormat("HH:mm:ss", Locale.GERMAN)

                    selectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    selectedDate.set(Calendar.MINUTE, minute)
                    selectedDate.set(Calendar.SECOND, 0)
                    textfield.text = formatter.format(selectedDate.time)
                },
                valueDate.get(Calendar.HOUR_OF_DAY),
                valueDate.get(Calendar.MINUTE),
                true,
            )
            datePickerDialog.show()
        }

        return view
    }

    override fun supports(field: Field): Boolean =
        field.xtype == "gosCoreComponentFormFieldTime"

    override fun getValue(view: View, field: Field, config: Map<String, Any>?) =
        view.findViewById<TextInputEditText>(R.id.field).text

    override fun setValue(view: View, field: Field, value: Any?, config: Map<String, Any>?) {
        view.findViewById<TextInputEditText>(R.id.field).setText(value?.toString() ?: "")
    }
}