package de.wollis_page.gibsonos.module.hc.io.activity

import android.os.Bundle
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.Tab
import de.wollis_page.gibsonos.dto.Update
import de.wollis_page.gibsonos.model.Message
import de.wollis_page.gibsonos.module.hc.index.activity.ModuleActivity
import de.wollis_page.gibsonos.module.hc.io.dto.Port
import de.wollis_page.gibsonos.module.hc.io.fragment.IndexFragment

class IndexActivity : ModuleActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.update = Update(
            "hc",
            "io",
            "index",
            this.module.id.toString(),
            Port::class
        )
    }

    override fun getTabs(): Array<Tab> {
        val arguments = hashMapOf("moduleId" to this.module.id)

        return arrayOf(
            Tab(R.string.hc_module_io_tab, IndexFragment::class, arguments),
            *super.getTabs()
        )
    }

    override fun updateMessage(message: Message) {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val listType = Types.newParameterizedType(MutableList::class.java, Port::class.java)
        val adapter = moshi.adapter<MutableList<Port>>(listType)
        val port = adapter.fromJson(message.payload)?.get(0)

        if (port === null) {
            return
        }

        val indexFragment = this.adapter.fragments[0] as IndexFragment
        val portItem = indexFragment.listAdapter.items.find {
            it is Port && it.id == port.id
        } as Port

        portItem.name = port.name
        portItem.direction = port.direction
        portItem.pullUp = port.pullUp
        portItem.value = port.value
        portItem.valueNames = port.valueNames
        portItem.pwm = port.pwm
        portItem.fadeIn = port.fadeIn

        this.runOnUiThread {
            indexFragment.listAdapter.notifyItemChanged(indexFragment.listAdapter.items.indexOf(portItem))
        }
    }
}