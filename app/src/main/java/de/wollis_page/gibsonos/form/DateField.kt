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

class DateField: FieldInterface {
    override fun build(
        field: Field,
        context: FormActivity,
        getConfig: (config: Map<String, Any>) -> Unit,
    ): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(
            R.layout.base_form_date_field,
            context.findViewById(android.R.id.content),
            false
        ) as LinearLayout

        view.findViewById<TextView>(R.id.label).text = field.title

        return view
    }

    override fun supports(field: Field): Boolean =
        field.xtype == "gosCoreComponentFormFieldDate"

    override fun getValue(view: View, field: Field): String {
        val yearPicker = view.findViewById<NumberPicker>(R.id.year)
        val monthPicker = view.findViewById<NumberPicker>(R.id.month)
        val dayPicker = view.findViewById<NumberPicker>(R.id.day)

        val selectedDate = GregorianCalendar()
        selectedDate.set(yearPicker.value, monthPicker.value-1, dayPicker.value)

        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.GERMAN)

        return formatter.format(selectedDate.time)
    }

    override fun setValue(view: View, field: Field, value: Any?) {
        val today = GregorianCalendar()
        today.time = Date()
        val selectedDate = GregorianCalendar()
        selectedDate.time = Date()

        if (value is String) {
            selectedDate.setTime(SimpleDateFormat("yyyy-MM-dd", Locale.GERMAN).parse(value)!!)
        }

        val todayYear = today.get(Calendar.YEAR)
        val yearPicker = view.findViewById<NumberPicker>(R.id.year)
        yearPicker.minValue = min(selectedDate.get(Calendar.YEAR), todayYear-1)
        yearPicker.maxValue = todayYear+100
        yearPicker.wrapSelectorWheel = false
        yearPicker.value = selectedDate.get(Calendar.YEAR)

        val monthPicker = view.findViewById<NumberPicker>(R.id.month)
        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        monthPicker.wrapSelectorWheel = false
        monthPicker.value = selectedDate.get(Calendar.MONTH)+1

        val dayPicker = view.findViewById<NumberPicker>(R.id.day)
        dayPicker.minValue = 1
        dayPicker.maxValue = 31
        dayPicker.wrapSelectorWheel = false
        dayPicker.value = selectedDate.get(Calendar.DATE)
    }
}