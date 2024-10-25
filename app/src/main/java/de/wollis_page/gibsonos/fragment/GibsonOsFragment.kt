package de.wollis_page.gibsonos.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.ContentFrameLayout
import androidx.fragment.app.Fragment
import de.wollis_page.gibsonos.activity.GibsonOsActivity

abstract class GibsonOsFragment: Fragment() {
    lateinit var activity: GibsonOsActivity
    protected lateinit var fragmentsArguments: HashMap<String, *>
    private var actionView: View? = null

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

    override fun onPause() {
        super.onPause()

        this.actionView ?: return
        this.activity.findViewById<ContentFrameLayout>(android.R.id.content).removeView(this.actionView)
        this.actionView = null
    }

    override fun onResume() {
        super.onResume()

        val actionButton = this.actionButton() ?: return
        val inflater = LayoutInflater.from(this.activity)

        this.actionView = inflater.inflate(
            actionButton,
            this.activity.findViewById(android.R.id.content),
            false
        )
        this.activity.findViewById<ContentFrameLayout>(android.R.id.content).addView(this.actionView)
        this.actionView()?.setOnClickListener {
            this.actionOnClickListener()
        }
    }

    protected open fun actionButton(): Int? = null

    protected open fun actionView(): View? = null

    protected open fun actionOnClickListener() {
    }
}