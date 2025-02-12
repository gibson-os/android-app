package de.wollis_page.gibsonos.module.hc.ir.activity

import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.module.hc.index.dto.Module
import de.wollis_page.gibsonos.module.hc.ir.dto.Remote
import de.wollis_page.gibsonos.module.hc.task.IrTask
import de.wollis_page.gibsonos.module.hc.task.ModuleTask
import de.wollis_page.gibsonos.service.AppIntentExtraService

class RemoteActivity: GibsonOsActivity() {
    private lateinit var remote: Remote
    private lateinit var module: Module

    override fun getContentView(): Int = R.layout.hc_module_ir_remote_view

    override fun getId() = this.module.id.toString() + '_' + this.remote.getId().toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.runTask({
            var remoteId = this.intent.getLongExtra("remoteId", 0)

            if (remoteId.toInt() == 0) {
                remoteId = ((AppIntentExtraService.getIntentExtra(SHORTCUT_KEY, intent) as Shortcut).parameters?.get("remoteId") as Double).toLong()
            }

            this.module = (AppIntentExtraService.getIntentExtra("module", intent) as Module?)
                ?: ModuleTask.getById(this, ((AppIntentExtraService.getIntentExtra(SHORTCUT_KEY, intent) as Shortcut).parameters?.get("moduleId") as Double).toLong())
            val layout = findViewById<View>(R.id.remote) as RelativeLayout
            this.remote = IrTask.remote(this, remoteId)
            this.setTitle(this.module.name + ": " + this.remote.name)
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            val displayWidth = displayMetrics.widthPixels
            val unitWidth = displayWidth / (remote.width + 1)

            this.remote.buttons.forEach {
                val shape = ShapeDrawable()
                shape.shape = RectShape()
                shape.setPadding(0, 0, 0,0)
                shape.paint.color = this.resources.getColor(R.color.colorAsset, this.theme)
                shape.paint.strokeWidth = 1f
                shape.paint.style = Paint.Style.STROKE
                val button = Button(this)
                button.setPadding(0, 0, 0, 0)
                button.background = shape
                button.text = it.name
                button.stateListAnimator = null
                val key = it
                button.setOnClickListener {
                    this.runTask({
                        IrTask.sendButton(this, this.module.id, key.id)
                    })
                }
                val params = RelativeLayout.LayoutParams(it.width * unitWidth, it.height * unitWidth)
                params.leftMargin = it.left * unitWidth
                params.topMargin = it.top * unitWidth

                this.runOnUiThread { layout.addView(button, params) }
            }
        })

    }
}