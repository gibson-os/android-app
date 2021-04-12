package de.wollis_page.gibsonos.module.core.desktop.dto

import android.os.Parcel
import android.os.Parcelable
import android.util.ArrayMap
import de.wollis_page.gibsonos.dto.ListItemInterface

class Item(
    var module: String,
    var task: String,
    var action: String,
    var text: String,
    var icon: String,
    var thumb: String,
    var customIcon: Long,
    var params: Map<String, Any>?
): ListItemInterface, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readLong(),
        null
    ) {
        this.params = this.readParamsFromParcel(parcel)
    }

    private fun readParamsFromParcel(parcel: Parcel): Map<String, Any> {
        val params = ArrayMap<String, Any>()
        parcel.readMap(params, Map::class.java.classLoader)

        return params
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(this.module)
        parcel.writeString(this.task)
        parcel.writeString(this.action)
        parcel.writeString(this.text)
        parcel.writeString(this.icon)
        parcel.writeString(this.thumb)
        parcel.writeLong(this.customIcon)
        parcel.writeMap(this.params)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item {
            return Item(parcel)
        }

        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }
    }
}