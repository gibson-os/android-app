package de.wollis_page.gibsonos.dto.desktop

import de.wollis_page.gibsonos.dto.ListInterface

class Item(
    var module: String,
    var task: String,
    var action: String,
    var text: String,
    var icon: String,
    var thumb: String,
    var customIcon: Long,
    //var params: LinkedHashMap<String, Any>
): ListInterface