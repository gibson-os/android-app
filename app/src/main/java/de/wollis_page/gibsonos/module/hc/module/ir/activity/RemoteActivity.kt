package de.wollis_page.gibsonos.module.hc.module.ir.activity

import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.AppActivityInterface
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.module.hc.module.ir.dto.Remote
import de.wollis_page.gibsonos.module.hc.module.task.IrTask


class RemoteActivity: GibsonOsActivity(), AppActivityInterface {
    private lateinit var remote: Remote
    private var moduleId: Long = 0

    override fun getAppIcon(): Int = R.drawable.ic_cog

    override fun getContentView(): Int = R.layout.hc_module_ir_remote_view

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.moduleId = this.intent.getLongExtra("moduleId", 0)
        val layout = findViewById<View>(R.id.remote) as RelativeLayout

        this.runTask({
            this.remote = IrTask.remote(this, this.intent.getLongExtra("remoteId", 0))
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            val displayWidth = displayMetrics.widthPixels
            val unitWidth = displayWidth / remote.width

            this.remote.keys.forEach {
                val shape = ShapeDrawable()
                shape.shape = RectShape()
                shape.setPadding(0, 0, 0,0)
                shape.paint.color = Color.BLACK
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
                        IrTask.sendRemoteKey(this, this.moduleId, key.eventId, key.keys)
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