package de.wollis_page.gibsonos.form

import android.app.DatePickerDialog
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

class DateTimeField: FieldInterface {
    override fun build(
        field: Field,
        context: FormActivity,
        getConfig: (config: Map<String, Any>) -> Unit,
    ): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(
            R.layout.base_form_date_time_field,
            context.findViewById(android.R.id.content),
            false
        ) as LinearLayout

        view.findViewById<TextInputLayout>(R.id.dateFieldLayout).hint = field.title

        val dateButton = view.findViewById<ImageButton>(R.id.dateButton)
        dateButton.contentDescription = field.title
        dateButton.setOnClickListener {
            val valueDate = GregorianCalendar()
            val textfield = view.findViewById<TextView>(R.id.dateField)
            val value = textfield.text.toString()

            valueDate.time = Date()

            if (value != "") {
                valueDate.time = SimpleDateFormat("yyyy-MM-dd", Locale.GERMAN).parse(value)!!
            }

            val datePickerDialog = DatePickerDialog(
                context,
                { _, year, monthOfYear, dayOfMonth ->
                    val selectedDate = GregorianCalendar()
                    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.GERMAN)

                    selectedDate.set(year, monthOfYear, dayOfMonth)
                    textfield.text = formatter.format(selectedDate.time)
                },
                valueDate.get(Calendar.YEAR),
                valueDate.get(Calendar.MONTH),
                valueDate.get(Calendar.DATE),
            )
            datePickerDialog.show()
        }

        view.findViewById<TextInputLayout>(R.id.timeFieldLayout).hint = field.title

        val timeButton = view.findViewById<ImageButton>(R.id.timeButton)
        timeButton.contentDescription = field.title
        timeButton.setOnClickListener {
            val valueDate = GregorianCalendar()
            val textfield = view.findViewById<TextView>(R.id.timeField)
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
        field.xtype == "gosCoreComponentFormFieldDateTime"

    override fun getValue(view: View, field: Field, config: Map<String, Any>?) =
        view.findViewById<TextInputEditText>(R.id.dateField).text.toString() + " " + view.findViewById<TextInputEditText>(R.id.timeField).text.toString()

    override fun setValue(view: View, field: Field, value: Any?, config: Map<String, Any>?) {
        if (value !is String) {
            return
        }

        val selectedDate = GregorianCalendar()
        val formatter = SimpleDateFormat("yyyy-MM-dd H:m:s", Locale.GERMAN)
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.GERMAN)
        val timeFormatter = SimpleDateFormat("HH:mm:ss", Locale.GERMAN)
        selectedDate.time = formatter.parse(value)!!

        view.findViewById<TextInputEditText>(R.id.dateField).setText(dateFormatter.format(selectedDate.time))
        view.findViewById<TextInputEditText>(R.id.timeField).setText(timeFormatter.format(selectedDate.time))
    }
}