package de.wollis_page.gibsonos.module.core.desktop.dto

import android.os.Parcel
import android.os.Parcelable
import android.util.ArrayMap
import de.wollis_page.gibsonos.dto.ListItemInterface

data class Shortcut(
    var module: String,
    var task: String,
    var action: String,
    var text: String,
    var icon: String,
    var parameters: Map<String, Any>?,
    var id: Long = 0,
    var position: Long = -1,
): ListItemInterface, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        null,
        0,
        -1
    ) {
        this.parameters = this.readParamsFromParcel(parcel)
        this.id = parcel.readLong()
        this.position = parcel.readLong()
    }

    private fun readParamsFromParcel(parcel: Parcel): Map<String, Any> {
        val parameters = ArrayMap<String, Any>()
        parcel.readMap(parameters, Map::class.java.classLoader)

        return parameters
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(this.module)
        parcel.writeString(this.task)
        parcel.writeString(this.action)
        parcel.writeString(this.text)
        parcel.writeString(this.icon)
        parcel.writeMap(this.parameters)
        parcel.writeLong(this.id)
        parcel.writeLong(this.position)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Shortcut> {
        override fun createFromParcel(parcel: Parcel): Shortcut {
            return Shortcut(parcel)
        }

        override fun newArray(size: Int): Array<Shortcut?> {
            return arrayOfNulls(size)
        }
    }

    override fun getId(): Long {
        return this.id
    }
}