package de.wollis_page.gibsonos.model

import com.orm.SugarRecord
import de.wollis_page.gibsonos.dto.ListItemInterface

class Message(
    var account: Account,
    var module: String,
    var task: String,
    var action: String,
    var title: String,
    var body: String,
    var payload: String = "",
): SugarRecord(), ListItemInterface {
}
