package de.wollis_page.gibsonos.module.explorer.service

import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.DialogItem
import de.wollis_page.gibsonos.helper.AlertListDialog
import de.wollis_page.gibsonos.module.explorer.index.dto.Item
import de.wollis_page.gibsonos.module.explorer.task.FileTask
import de.wollis_page.gibsonos.module.explorer.task.Html5Task

class Html5Service {
    fun convert(context: GibsonOsActivity, dir: String, item: Item) {
        context.runTask({
            if (item.metaInfos === null) {
                item.metaInfos = FileTask.metaInfos(context, dir + "/" + item.name)
            }

            context.runOnUiThread {
                this.showConvertDialog(context, dir, item)
            }
        })
    }

    private fun showConvertDialog(context: GibsonOsActivity, dir: String, item: Item) {
        var selectedLanguage: String? = null

        val languages = ArrayList<DialogItem>()
        val subtitles = ArrayList<DialogItem>()

        val audioStreams = (item.metaInfos?.get("audioStreams") ?: emptyMap<Any, Any>()) as Map<*, *>
        val subtitleStreams = (item.metaInfos?.get("subtitleStreams") ?: emptyMap<Any, Any>()) as Map<*, *>

        val files = ArrayList<String>()
        files.add(item.name)

        if (audioStreams.size == 1 && subtitleStreams.isEmpty()) {
            context.runTask({
                Html5Task.convert(
                    context,
                    dir,
                    files,
                    audioStreams.keys.iterator().next().toString()
                )
            })

            return
        }

        val subtitleItemNone = DialogItem("Kein Untertitel")
        subtitleItemNone.icon = R.drawable.ic_subtitle
        subtitleItemNone.onClick = {
            context.runTask({
                Html5Task.convert(
                    context,
                    dir,
                    files,
                    selectedLanguage,
                    "none"
                )
            })
        }
        subtitles.add(subtitleItemNone)

        subtitleStreams.forEach { subtitleStreamKey, subtitleStream ->
            val mappedSubtitleStream = subtitleStream as Map<*, *>
            var subtitleTitle = mappedSubtitleStream["language"].toString()

            if (mappedSubtitleStream["forced"] == true) {
                subtitleTitle += " (Forced)"
            }

            if (mappedSubtitleStream["default"] == true) {
                subtitleTitle += " (Standard)"
            }

            val subtitleItem = DialogItem(subtitleTitle)
            subtitleItem.icon = R.drawable.ic_subtitle
            subtitleItem.onClick = {
                context.runTask({
                    Html5Task.convert(
                        context,
                        dir,
                        files,
                        selectedLanguage,
                        subtitleStreamKey.toString()
                    )
                })
            }
            subtitles.add(subtitleItem)
        }

        val subtitlesDialog = AlertListDialog(context, "Untertitel", subtitles);

        if (audioStreams.size == 1) {
            subtitlesDialog.show()

            return
        }

        audioStreams.forEach { audioStreamKey, audioStream ->
            val mappedAudioStream = audioStream as Map<*, *>
            var languageTitle = mappedAudioStream["language"].toString()

            if (mappedAudioStream["default"] == true) {
                languageTitle += " (Standard)"
            }

            languageTitle += " " + mappedAudioStream["format"] +
                    " [" + mappedAudioStream["channels"] + " " +
                    mappedAudioStream["bitrate"] + "]"

            val languageItem = DialogItem(languageTitle)
            languageItem.icon = R.drawable.ic_language

            if (subtitleStreams.isEmpty()) {
                context.runTask({
                    Html5Task.convert(
                        context,
                        dir,
                        files,
                        selectedLanguage
                    )
                })

                return@forEach
            } else {
                languageItem.onClick = {
                    selectedLanguage = audioStreamKey.toString()
                    subtitlesDialog.show()

                    null
                }
            }

            languages.add(languageItem)
        }

        AlertListDialog(context, "Sprache", languages).show()
    }
}