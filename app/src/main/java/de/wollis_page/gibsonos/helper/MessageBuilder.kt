package de.wollis_page.gibsonos.helper

import de.wollis_page.gibsonos.activity.FormActivity
import de.wollis_page.gibsonos.dto.DialogItem
import de.wollis_page.gibsonos.dto.Message
import de.wollis_page.gibsonos.dto.form.Button

class MessageBuilder {
    fun build(
        context: FormActivity,
        message: Message,
        button: android.widget.Button? = null,
        formButton: Map.Entry<String, Button>? = null,
    ): AlertListDialog {
        val options = ArrayList<DialogItem>()

        message.buttons.forEach { messageButton ->
            val item = DialogItem(messageButton.text)

            if (button != null && formButton != null && messageButton.sendRequest) {
                item.onClick = { _, _ ->
                    context.buttonClick(button, formButton, messageButton)
                }
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