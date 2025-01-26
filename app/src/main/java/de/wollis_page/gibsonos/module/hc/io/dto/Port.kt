package de.wollis_page.gibsonos.module.hc.io.dto

import android.os.Parcel
import android.os.Parcelable
import de.wollis_page.gibsonos.dto.ListItemInterface

data class Port(
    var id: Long,
    var number: Int,
    var name: String,
    var value: Boolean,
    var direction: Int,
    var pullUp: Boolean,
    var pwm: Int,
    var fadeIn: Int,
    var valueNames: Array<String>
) : ListItemInterface, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readInt() > 0,
        parcel.readInt(),
        parcel.readInt() > 0,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readArray(Array::class.java.classLoader) as Array<String>
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeInt(number)
        parcel.writeString(name)
        parcel.writeInt(if (value) 1 else 0)
        parcel.writeInt(direction)
        parcel.writeInt(if (pullUp) 1 else 0)
        parcel.writeInt(pwm)
        parcel.writeInt(fadeIn)
        parcel.writeArray(valueNames)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Port> {
        override fun createFromParcel(parcel: Parcel): Port {
            return Port(parcel)
        }

        override fun newArray(size: Int): Array<Port?> {
            return arrayOfNulls(size)
        }
    }

    override fun getId() = this.id
}