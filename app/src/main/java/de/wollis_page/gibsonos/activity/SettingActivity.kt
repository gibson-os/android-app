package de.wollis_page.gibsonos.activity

import android.app.UiModeManager
import android.os.Bundle
import com.google.android.material.switchmaterial.SwitchMaterial
import de.wollis_page.gibsonos.R

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

    override fun getId(): Any {
        return 0
    }
}