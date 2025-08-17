package de.wollis_page.gibsonos.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.ContentFrameLayout
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import de.wollis_page.gibsonos.activity.GibsonOsActivity

abstract class GibsonOsFragment: Fragment() {
    lateinit var activity: GibsonOsActivity
    protected lateinit var fragmentsArguments: HashMap<String, *>
    private var actionView: View? = null
    protected var menu: Menu? = null

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

    open fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return true
    }

    open fun onPrepareMenu(menu: Menu) {
    }

    open fun getMenuView(): Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val menuView = this.getMenuView()

        if (menuView != null) {
            val fragment = this;

            this.requireActivity().addMenuProvider(object : MenuProvider {
//                override fun onPrepareMenu(menu: Menu) {
//                    super.onPrepareMenu(menu)
//                }

                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    fragment.menu = menu
                    menuInflater.inflate(menuView, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return fragment.onMenuItemSelected(menuItem)
                }

                override fun onPrepareMenu(menu: Menu) {
                    super.onPrepareMenu(menu)

                    fragment.onPrepareMenu(menu)
                }

                //            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                //                return when(menuItem.itemId){
                //                    R.id.navigation_notifications -> {
                //                        true
                //                    }
                //                    R.id.navigation_wallet -> {
                //                        true
                //                    }
                //                    else -> false
                //                }
                //            }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        }

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