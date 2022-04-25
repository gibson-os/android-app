package de.wollis_page.gibsonos.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import de.wollis_page.gibsonos.activity.GibsonOsActivity

abstract class GibsonOsFragment: Fragment() {
    lateinit var activity: GibsonOsActivity
    protected lateinit var fragmentsArguments: HashMap<String, *>

    protected abstract fun getContentView(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.activity = this.requireActivity() as GibsonOsActivity
    }

    open fun updateData(data: String) {
    }
}