package de.wollis_page.gibsonos.module.explorer.index.dto

import android.os.Parcel
import android.os.Parcelable

data class Dir(
    val data: MutableList<Item>,
    val dir: String,
    val homePath: String,
    val total: Long
): Parcelable {
    constructor(parcel: Parcel) : this(
        ArrayList(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readLong()
    ) {
        parcel.readList(this.data, MutableList::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(this.dir)
        parcel.writeString(this.homePath)
        parcel.writeLong(this.total)
        parcel.writeList(this.data)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Dir> {
        override fun createFromParcel(parcel: Parcel): Dir {
            return Dir(parcel)
        }

        override fun newArray(size: Int): Array<Dir?> {
            return arrayOfNulls(size)
        }
    }
}