package de.wollis_page.gibsonos.form

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.FormActivity
import de.wollis_page.gibsonos.dto.form.Field
import de.wollis_page.gibsonos.dto.form.FormFile
import java.io.File

class FileField : FieldInterface {
    override fun build(
        field: Field,
        context: FormActivity,
        onValueChange: () -> Unit,
        getConfig: (config: Map<String, Any>) -> Unit,
    ): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(
            R.layout.base_form_file_field,
            context.findViewById(android.R.id.content),
            false
        ) as LinearLayout

        view.findViewById<TextInputLayout>(R.id.fieldLayout).hint = field.title

        val button = view.findViewById<ImageButton>(R.id.button)
        button.contentDescription = field.title
        
        val clickListener = View.OnClickListener {
            context.pickFile("*/*") { uri ->
                if (uri != null) {
                    val file = this.getFileFromUri(context, uri)
                    val mimeType = context.contentResolver.getType(uri) ?: "application/octet-stream"
                    val formFile = FormFile(file, mimeType)
                    
                    view.tag = formFile
                    view.findViewById<TextInputEditText>(R.id.field).setText(uri.path)
                    onValueChange()
                }
            }
        }

        button.setOnClickListener(clickListener)
        view.findViewById<TextInputEditText>(R.id.field).setOnClickListener(clickListener)

        return view
    }

    override fun supports(field: Field): Boolean = field.xtype == "gosCoreComponentFormFieldFile"

    override fun getValue(view: View, field: Field, config: Map<String, Any>?): Any? = view.tag

    override fun setValue(view: View, field: Field, value: Any?, config: Map<String, Any>?) {
        if (value is FormFile) {
            view.tag = value
            view.findViewById<TextInputEditText>(R.id.field).setText(value.file.name)
        }
    }

    private fun getFileFromUri(context: android.content.Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri) ?: throw Exception("Cannot open stream")
        val file = File(context.cacheDir, "upload_${System.currentTimeMillis()}")
        file.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }
        return file
    }
}
