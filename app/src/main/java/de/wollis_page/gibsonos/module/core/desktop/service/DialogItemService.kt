package de.wollis_page.gibsonos.module.core.desktop.service

import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.DialogItem
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.module.core.task.DesktopTask

object DialogItemService {
    fun getDesktopItem(context: GibsonOsActivity, shortcut: Shortcut): DialogItem {
        val dialogItem = DialogItem(context.getString(R.string.core_desktop_add))

        val drawable: Drawable = DrawableCompat.wrap(AppCompatResources.getDrawable(context, R.drawable.ic_plus)!!)
        DrawableCompat.setTint(drawable, context.resources.getColor(R.color.colorAsset, context.theme))
        dialogItem.icon = R.drawable.ic_plus
        dialogItem.onClick = { flattedItem, adapter ->
            context.runTask({
                DesktopTask.add(context, shortcut)
            })
        }

        return dialogItem
    }
}