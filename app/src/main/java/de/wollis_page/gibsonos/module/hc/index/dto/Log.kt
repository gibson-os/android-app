package de.wollis_page.gibsonos.module.hc.index.dto

import de.wollis_page.gibsonos.dto.ListItemInterface

data class Log(
    var id: Long,
    var added: String,
    var masterId: Long,
    var masterName: String,
    var direction: String,
    var data: String,
    var type: Int,
    var command: String?,
    var moduleId: Long?,
    var moduleName: String?,
    var slaveAddress: Int?,
    var text: String?,
    var rendered: String?
) : ListItemInterface {
    override fun getId(): Long {
        return this.id
    }
}
