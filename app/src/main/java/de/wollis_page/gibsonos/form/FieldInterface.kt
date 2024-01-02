package de.wollis_page.gibsonos.form

import android.view.View
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.form.Field

interface FieldInterface {
    fun getView(field: Field, context: GibsonOsActivity): View
    fun supports(field: Field): Boolean
    fun getValue(view: View, field: Field): Any?
    fun setValue(view: View, field: Field, value: Any?)
}