package de.wollis_page.gibsonos.helper

import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.DialogItem
import de.wollis_page.gibsonos.dto.FlattedDialogItem
import de.wollis_page.gibsonos.dto.Message

class MessageBuilder {
    fun build(
        context: GibsonOsActivity,
        message: Message,
        onClick: ((flattedItem: FlattedDialogItem) -> Any?)? = null,
    ): AlertListDialog {
        val options = ArrayList<DialogItem>()

        message.buttons.forEach { button ->
            val item = DialogItem(button.text)
            item.onClick = onClick ?: {
            }
            options.add(item)
        }

        return AlertListDialog(
            context,
            message.msg,
            options,
        )
    }
}