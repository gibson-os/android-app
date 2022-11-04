package de.wollis_page.gibsonos.module.explorer.service

import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.module.explorer.html5.dialog.ConvertDialog
import de.wollis_page.gibsonos.module.explorer.index.dto.Item
import de.wollis_page.gibsonos.module.explorer.task.FileTask
import de.wollis_page.gibsonos.module.explorer.task.Html5Task

class Html5Service(private val convertDialog: ConvertDialog) {
    fun convert(
        context: GibsonOsActivity,
        dir: String,
        item: Item,
        callback: (tokens: MutableMap<String, String>) -> Unit
    ) {
        val files = ArrayList<String>()
        files.add(item.name)

        context.runTask({
            if (item.metaInfos === null) {
                item.metaInfos = FileTask.metaInfos(context, dir + "/" + item.name)
            }

            val audioStreams = (item.metaInfos?.get("audioStreams") ?: emptyMap<Any, Any>()) as Map<*, *>
            val subtitleStreams = (item.metaInfos?.get("subtitleStreams") ?: emptyMap<Any, Any>()) as Map<*, *>

            context.runOnUiThread {
                val dialog = this.convertDialog.build(
                    dir,
                    files,
                    audioStreams,
                    subtitleStreams,
                    callback
                )

                if (dialog == null) {
                    context.runTask({
                        callback(Html5Task.convert(
                            context,
                            dir,
                            files,
                            audioStreams.keys.iterator().next().toString()
                        ))
                    })

                    return@runOnUiThread
                }

                dialog.show()
            }
        })
    }
}