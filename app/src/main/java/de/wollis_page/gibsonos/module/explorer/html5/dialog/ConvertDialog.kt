package de.wollis_page.gibsonos.module.explorer.html5.dialog

import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.DialogItem
import de.wollis_page.gibsonos.helper.AlertListDialog
import de.wollis_page.gibsonos.module.explorer.task.Html5Task

class ConvertDialog(private var context: GibsonOsActivity) {
    private var selectedLanguage: String? = null

    fun build(
        dir: String,
        files: ArrayList<String>,
        audioStreams: Map<*, *>,
        subtitleStreams: Map<*, *>,
        callback: (tokens: MutableMap<String, String>) -> Unit
    ): AlertListDialog? {
        val languages = ArrayList<DialogItem>()
        val subtitles = ArrayList<DialogItem>()

        if (audioStreams.size <= 1 && subtitleStreams.isEmpty()) {
            return null
        }

        subtitles.add(this.getNoSubtitleItem(dir, files, callback))

        subtitleStreams.forEach { (subtitleStreamKey, subtitleStream) ->
            subtitles.add(this.getSubtitleItem(
                subtitleStreamKey.toString(),
                subtitleStream as Map<*, *>,
                dir,
                files,
                callback
            ))
        }

        val subtitlesDialog = AlertListDialog(
            this.context,
            this.context.getString(R.string.explorer_html5_subtitle),
            subtitles
        )

        if (audioStreams.size <= 1) {
            return subtitlesDialog
        }

        audioStreams.forEach { (audioStreamKey, audioStream) ->
            languages.add(this.getLanguageItem(
                audioStreamKey.toString(),
                audioStream as Map<*, *>,
                subtitleStreams,
                subtitlesDialog,
                dir,
                files,
                callback
            ))
        }

        return AlertListDialog(
            this.context,
            this.context.getString(R.string.explorer_html5_language),
            languages
        )
    }

    private fun getLanguageItem(
        audioStreamKey: String,
        audioStream: Map<*, *>,
        subtitleStreams: Map<*, *>,
        subtitlesDialog: AlertListDialog,
        dir: String,
        files: MutableList<String>,
        callback: (tokens: MutableMap<String, String>) -> Unit
    ): DialogItem {
        var languageTitle = audioStream["language"].toString()

        if (audioStream["default"] == true) {
            languageTitle += " (" + this.context.getString(R.string.explorer_html5_default) + ")"
        }

        languageTitle += " " + audioStream["format"] +
                " [" + audioStream["channels"] + " " +
                audioStream["bitrate"] + "]"

        val dialogItem = DialogItem(languageTitle)

        dialogItem.icon = R.drawable.ic_language
        dialogItem.onClick = {
            if (subtitleStreams.isEmpty()) {
                this.context.runTask({
                    callback(Html5Task.convert(
                        this.context,
                        dir,
                        files,
                        audioStreamKey
                    ))
                })
            } else {
                this.selectedLanguage = audioStreamKey
                subtitlesDialog.show()
            }

            null
        }

        return dialogItem
    }

    private fun getNoSubtitleItem(
        dir: String,
        files: MutableList<String>,
        callback: (tokens: MutableMap<String, String>) -> Unit
    ): DialogItem {
        val dialogItem = DialogItem(this.context.getString(R.string.explorer_html5_no_subtitle))

        dialogItem.icon = R.drawable.ic_subtitle
        dialogItem.onClick = {
            this.context.runTask({
                callback(
                    Html5Task.convert(
                        this.context,
                        dir,
                        files,
                        this.selectedLanguage,
                        "none"
                    ))
            })
        }

        return dialogItem
    }

    private fun getSubtitleItem(
        subtitleStreamKey: String,
        subtitleStream: Map<*, *>,
        dir: String,
        files: MutableList<String>,
        callback: (tokens: MutableMap<String, String>) -> Unit
    ): DialogItem {
        var subtitleTitle = subtitleStream["language"].toString()

        if (subtitleStream["forced"] == true) {
            subtitleTitle += " (" + this.context.getString(R.string.explorer_html5_forced) + ")"
        }

        if (subtitleStream["default"] == true) {
            subtitleTitle += " (" + this.context.getString(R.string.explorer_html5_default) + ")"
        }

        val dialogItem = DialogItem(subtitleTitle)

        dialogItem.icon = R.drawable.ic_subtitle
        dialogItem.onClick = {
            this.context.runTask({
                callback(
                    Html5Task.convert(
                        this.context,
                        dir,
                        files,
                        this.selectedLanguage,
                        subtitleStreamKey
                    ))
            })
        }

        return dialogItem
    }
}