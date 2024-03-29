package de.wollis_page.gibsonos.model

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.orm.SugarRecord
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.helper.Config

class Account: SugarRecord, Parcelable, ListItemInterface {
    var alias: String? = null
    var userId: Long = 0
    lateinit var userName: String
    var token: String? = null
    lateinit var url: String
    var deviceId: Long = 0

    constructor()

    constructor(userId: Long, userName: String, deviceId: Long, url: String, token: String?) {
        Log.i(Config.LOG_TAG, "Create Account Model $userName@$url with token $token")

        this.userId = userId
        this.userName = userName
        this.url = url
        this.token = token
        this.deviceId = deviceId
    }

    private constructor(parcel: Parcel) {
        this.id = parcel.readLong()
        this.alias = parcel.readString()
        this.userId = parcel.readLong()
        this.userName = parcel.readString().toString()
        this.token = parcel.readString()
        this.url = parcel.readString().toString()
        this.deviceId = parcel.readLong()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(this.id)
        dest.writeString(this.alias)
        dest.writeLong(this.userId)
        dest.writeString(this.userName)
        dest.writeString(this.token)
        dest.writeString(this.url)
        dest.writeLong(this.deviceId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Account> {
        override fun createFromParcel(parcel: Parcel): Account {
            return Account(parcel)
        }

        override fun newArray(size: Int): Array<Account?> {
            return arrayOfNulls(size)
        }
    }
}