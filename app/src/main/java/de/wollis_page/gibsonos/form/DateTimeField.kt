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
import kotlin.math.min

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

        view.findViewById<TextView>(R.id.label).text = field.title

        return view
    }

    override fun supports(field: Field): Boolean =
        field.xtype == "gosCoreComponentFormFieldDateTime"

    override fun getValue(view: View, field: Field, config: Map<String, Any>?): String {
        val yearPicker = view.findViewById<NumberPicker>(R.id.year)
        val monthPicker = view.findViewById<NumberPicker>(R.id.month)
        val dayPicker = view.findViewById<NumberPicker>(R.id.day)
        val hourPicker = view.findViewById<NumberPicker>(R.id.hour)
        val minutePicker = view.findViewById<NumberPicker>(R.id.minute)
        val secondPicker = view.findViewById<NumberPicker>(R.id.second)

        val selectedDate = GregorianCalendar()
        selectedDate.set(yearPicker.value, monthPicker.value-1, dayPicker.value, hourPicker.value, minutePicker.value, secondPicker.value)

        val formatter = SimpleDateFormat("yyyy-MM-dd H:m:s", Locale.GERMAN)

        return formatter.format(selectedDate.time)
    }

    override fun setValue(view: View, field: Field, value: Any?, config: Map<String, Any>?) {
        val today = GregorianCalendar()
        today.time = Date()
        val selectedDate = GregorianCalendar()
        selectedDate.time = Date()

        if (value is String) {
            selectedDate.setTime(SimpleDateFormat("yyyy-MM-dd H:m:s", Locale.GERMAN).parse(value)!!)
        }

        val todayYear = today.get(Calendar.YEAR)
        val yearPicker = view.findViewById<NumberPicker>(R.id.year)
        yearPicker.minValue = min(selectedDate.get(Calendar.YEAR), todayYear-1)
        yearPicker.maxValue = todayYear+100
        yearPicker.wrapSelectorWheel = false

        val monthPicker = view.findViewById<NumberPicker>(R.id.month)
        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        monthPicker.wrapSelectorWheel = false

        val dayPicker = view.findViewById<NumberPicker>(R.id.day)
        dayPicker.minValue = 1
        dayPicker.maxValue = 31
        dayPicker.wrapSelectorWheel = false

        val hourPicker = view.findViewById<NumberPicker>(R.id.hour)
        hourPicker.minValue = 0
        hourPicker.maxValue = 23
        hourPicker.wrapSelectorWheel = false

        val minutePicker = view.findViewById<NumberPicker>(R.id.minute)
        minutePicker.minValue = 0
        minutePicker.maxValue = 59
        minutePicker.wrapSelectorWheel = false

        val secondPicker = view.findViewById<NumberPicker>(R.id.second)
        secondPicker.minValue = 0
        secondPicker.maxValue = 59
        secondPicker.wrapSelectorWheel = false

        if (value is String) {
            yearPicker.value = selectedDate.get(Calendar.YEAR)
            monthPicker.value = selectedDate.get(Calendar.MONTH) + 1
            dayPicker.value = selectedDate.get(Calendar.DATE)
            hourPicker.value = selectedDate.get(Calendar.HOUR_OF_DAY)
            minutePicker.value = selectedDate.get(Calendar.MINUTE)
            secondPicker.value = selectedDate.get(Calendar.SECOND)
        }
    }
}