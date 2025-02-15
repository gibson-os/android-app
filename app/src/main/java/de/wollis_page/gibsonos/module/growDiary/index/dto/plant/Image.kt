package de.wollis_page.gibsonos.module.growDiary.index.dto.plant

import android.os.Parcel
import android.os.Parcelable
import de.wollis_page.gibsonos.dto.ListItemInterface

data class Image(
    var id: Long,
    var description: String?,
    var created: String,
    var day: Int,
    var week: Int,
): ListItemInterface, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
    ) {
    }

    override fun getId() = this.id
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(description)
        parcel.writeString(created)
        parcel.writeInt(day)
        parcel.writeInt(week)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Image> {
        override fun createFromParcel(parcel: Parcel): Image {
            return Image(parcel)
        }

        override fun newArray(size: Int): Array<Image?> {
            return arrayOfNulls(size)
        }
    }
}
