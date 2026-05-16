package de.wollis_page.gibsonos.module.tc.task

import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.fragment.GibsonOsFragment
import de.wollis_page.gibsonos.module.tc.index.dto.Train
import de.wollis_page.gibsonos.task.AbstractTask

object TrainTask: AbstractTask() {
    fun getList(context: GibsonOsFragment, start: Long, limit: Long): ListResponse<Train> {
        val dataStore = this.getDataStore(context.activity.getAccount(), "tc", "train", "list")

        return this.loadList(context, dataStore, start, limit)
    }
}