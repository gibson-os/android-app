package de.wollis_page.gibsonos.activity

import android.app.UiModeManager
import android.os.Bundle
import com.google.android.material.switchmaterial.SwitchMaterial
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut

class SettingActivity: GibsonOsActivity() {
    override fun getContentView(): Int = R.layout.setting

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uiModeManager = getSystemService(UI_MODE_SERVICE) as UiModeManager
        val darkMode = this.findViewById<SwitchMaterial>(R.id.darkMode)
        darkMode.isChecked = uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES
        darkMode.setOnCheckedChangeListener { compoundButton, checked ->
            this.application.setDarkMode(checked)
        }
    }

    override fun getId(): Long = 0

    override fun isActivityforShotcut(shortcut: Shortcut): Boolean {
        return false
    }
}