package de.wollis_page.gibsonos.form

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.material.slider.Slider
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.FormActivity
import de.wollis_page.gibsonos.dto.form.Field

class SliderField : FieldInterface {
    override fun build(
        field: Field,
        context: FormActivity,
        onValueChange: () -> Unit,
        getConfig: (config: Map<String, Any>) -> Unit,
    ): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(
            R.layout.base_form_slider_field,
            context.findViewById(android.R.id.content),
            false
        )

        val label = view.findViewById<TextView>(R.id.label)
        label.text = field.title

        val slider = view.findViewById<Slider>(R.id.field)

        val minValue = (field.config["minValue"] as? Number)?.toFloat() ?: 0f
        val maxValue = (field.config["maxValue"] as? Number)?.toFloat() ?: 100f

        slider.valueFrom = minValue
        slider.valueTo = if (maxValue > minValue) maxValue else minValue + 1.0f

        (field.config["increment"] as? Number)?.toFloat()?.let {
            if (it > 0) {
                slider.stepSize = it
            }
        }

        slider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
            }

            override fun onStopTrackingTouch(slider: Slider) {
                onValueChange()
            }
        })

        return view
    }

    override fun supports(field: Field): Boolean =
        field.xtype == "gosCoreComponentFormFieldSliderField"

    override fun getValue(view: View, field: Field, config: Map<String, Any>?): Any =
        view.findViewById<Slider>(R.id.field).value

    override fun setValue(view: View, field: Field, value: Any?, config: Map<String, Any>?) {
        val slider = view.findViewById<Slider>(R.id.field)
        val floatValue = when (value) {
            is Number -> value.toFloat()
            is String -> value.toFloatOrNull() ?: slider.valueFrom
            else -> slider.valueFrom
        }

        if (floatValue >= slider.valueFrom && floatValue <= slider.valueTo) {
            slider.value = floatValue
        } else if (floatValue < slider.valueFrom) {
            slider.value = slider.valueFrom
        } else if (floatValue > slider.valueTo) {
            slider.value = slider.valueTo
        }
    }
}
