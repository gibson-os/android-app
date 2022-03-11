package de.wollis_page.gibsonos.module.hc.index.dto

import android.os.Parcel
import android.os.Parcelable
import de.wollis_page.gibsonos.dto.ListItemInterface

data class Module(
    var id: Long,
    var type: String,
    var name: String,
    var address: Int,
    var helper: String,
    var modified: String
): ListItemInterface, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(type)
        parcel.writeString(name)
        parcel.writeInt(address)
        parcel.writeString(helper)
        parcel.writeString(modified)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Module> {
        override fun createFromParcel(parcel: Parcel): Module {
            return Module(parcel)
        }

        override fun newArray(size: Int): Array<Module?> {
            return arrayOfNulls(size)
        }
    }
}
