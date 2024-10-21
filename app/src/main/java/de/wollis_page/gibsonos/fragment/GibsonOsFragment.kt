package de.wollis_page.gibsonos.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.wollis_page.gibsonos.activity.GibsonOsActivity

abstract class GibsonOsFragment: Fragment() {
    lateinit var activity: GibsonOsActivity
    protected lateinit var fragmentsArguments: HashMap<String, *>

    protected abstract fun getContentView(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.activity = this.requireActivity() as GibsonOsActivity

        arguments?.takeIf { it.containsKey("arguments") }?.apply {
            fragmentsArguments = getSerializable("arguments") as HashMap<String, *>
        }
    }

    open fun updateData(data: String) {
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(this.getContentView(), container, false);
    }
}