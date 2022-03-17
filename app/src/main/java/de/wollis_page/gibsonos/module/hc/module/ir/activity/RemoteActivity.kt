package de.wollis_page.gibsonos.module.hc.module.ir.activity

import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.AppActivityInterface
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.module.hc.module.ir.dto.Remote
import de.wollis_page.gibsonos.module.hc.module.task.IrTask


class RemoteActivity: GibsonOsActivity(), AppActivityInterface {
    private lateinit var remote: Remote

    override fun getAppIcon(): Int = R.drawable.ic_cog

    override fun getContentView(): Int = R.layout.hc_module_ir_remote_view

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = findViewById<View>(R.id.remote) as ConstraintLayout

        this.runTask({
            this.remote = IrTask.remote(this, this.intent.getLongExtra("remoteId", 0))

            this.remote.keys.forEach {
                val button = Button(this)
                button.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                button.text = it.name

                this.runOnUiThread { layout.addView(button) }
            }
        })

    }
}