package de.wollis_page.gibsonos.form

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.form.Field
import de.wollis_page.gibsonos.module.explorer.index.dialog.DirListDialog
import de.wollis_page.gibsonos.module.explorer.task.DirTask

class DirectoryField: FieldInterface {
    override fun build(field: Field, context: GibsonOsActivity): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(
            R.layout.base_form_directory_field,
            context.findViewById(android.R.id.content),
            false
        ) as LinearLayout

        view.findViewById<TextInputLayout>(R.id.fieldLayout).hint = field.title

        val dirListDialogBuilder = DirListDialog(context) { item, dirList ->
            this.setValue(view, field, dirList.id)
        }
        val button = view.findViewById<ImageButton>(R.id.button)
        button.contentDescription = field.title
        button.setOnClickListener {
            context.runTask({
                val dirListDialog = dirListDialogBuilder.build(
                    DirTask.dirList(context, this.getValue(view, field).toString())
                )

                context.runOnUiThread {
                    dirListDialog.show()
                }
            })
        }

        return view
    }

    override fun supports(field: Field): Boolean = field.xtype == "gosModuleExplorerDirParameter"

    override fun getValue(view: View, field: Field): Any? =
        view.findViewById<TextInputEditText>(R.id.field).text

    override fun setValue(view: View, field: Field, value: Any?) {
        view.findViewById<TextInputEditText>(R.id.field).setText(value?.toString() ?: "")
    }
}