package de.wollis_page.gibsonos.module.hc.index.dto

import android.os.Parcel
import android.os.Parcelable
import de.wollis_page.gibsonos.dto.ListItemInterface

data class Master(
    var id: Long,
    var name: String,
    var address: String,
    var protocol: String,
    var added: String,
    var modified: String
): ListItemInterface, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(address)
        parcel.writeString(protocol)
        parcel.writeString(added)
        parcel.writeString(modified)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Master> {
        override fun createFromParcel(parcel: Parcel): Master {
            return Master(parcel)
        }

        override fun newArray(size: Int): Array<Master?> {
            return arrayOfNulls(size)
        }
    }

    override fun getId(): Long {
        return this.id
    }

}
