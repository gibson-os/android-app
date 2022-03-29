package de.wollis_page.gibsonos.fragment

import androidx.fragment.app.Fragment

abstract class GibsonOsFragment: Fragment() {
    protected lateinit var fragmentsArguments: HashMap<String, *>

    open fun updateData(data: String) {
    }
}