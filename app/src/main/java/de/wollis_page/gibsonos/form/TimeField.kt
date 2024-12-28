package de.wollis_page.gibsonos.form

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.TextView
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

        view.findViewById<TextView>(R.id.label).text = field.title

        return view
    }

    override fun supports(field: Field): Boolean =
        field.xtype == "gosCoreComponentFormFieldTime"

    override fun getValue(view: View, field: Field, config: Map<String, Any>?): String {
        val hourPicker = view.findViewById<NumberPicker>(R.id.hour)
        val minutePicker = view.findViewById<NumberPicker>(R.id.minute)
        val secondPicker = view.findViewById<NumberPicker>(R.id.second)

        val selectedDate = GregorianCalendar()
        selectedDate.set(Calendar.HOUR_OF_DAY, hourPicker.value)
        selectedDate.set(Calendar.MINUTE, minutePicker.value)
        selectedDate.set(Calendar.SECOND, secondPicker.value)

        val formatter = SimpleDateFormat("H:m:s", Locale.GERMAN)

        return formatter.format(selectedDate.time)
    }

    override fun setValue(view: View, field: Field, value: Any?, config: Map<String, Any>?) {
        val today = GregorianCalendar()
        today.time = Date()
        val selectedDate = GregorianCalendar()
        selectedDate.time = Date()

        if (value is String) {
            selectedDate.setTime(SimpleDateFormat("H:m:s", Locale.GERMAN).parse(value)!!)
        }

        val hourPicker = view.findViewById<NumberPicker>(R.id.hour)
        hourPicker.minValue = 0
        hourPicker.maxValue = 23
        hourPicker.wrapSelectorWheel = false
        hourPicker.value = selectedDate.get(Calendar.HOUR_OF_DAY)

        val minutePicker = view.findViewById<NumberPicker>(R.id.minute)
        minutePicker.minValue = 0
        minutePicker.maxValue = 59
        minutePicker.wrapSelectorWheel = false
        minutePicker.value = selectedDate.get(Calendar.MINUTE)

        val secondPicker = view.findViewById<NumberPicker>(R.id.second)
        secondPicker.minValue = 0
        secondPicker.maxValue = 59
        secondPicker.wrapSelectorWheel = false
        secondPicker.value = selectedDate.get(Calendar.SECOND)
    }
}