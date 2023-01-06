package de.wollis_page.gibsonos.model

import com.orm.SugarRecord
import de.wollis_page.gibsonos.dto.ListItemInterface

class Message: SugarRecord, ListItemInterface {
    lateinit var account: Account
    lateinit var module: String
    lateinit var task: String
    lateinit var action: String
    lateinit var title: String
    lateinit var body: String
    lateinit var date: String
    var payload: String = ""

    constructor()

    constructor(
        account: Account,
        module: String,
        task: String,
        action: String,
        title: String,
        body: String,
        date: String,
        payload: String = ""
    ) {
        this.account = account
        this.module = module
        this.task = task
        this.action = action
        this.title = title
        this.body = body
        this.date = date
        this.payload = payload
    }
}
