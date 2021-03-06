package de.wollis_page.gibsonos.model

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.orm.SugarRecord
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.helper.Config

class Account: SugarRecord, Parcelable, ListItemInterface {
    var alias: String? = null
    var user: String = ""
    var token: String? = null
    var url: String = ""

    constructor()

    constructor(user: String, url: String, token: String?) {
        Log.i(Config.LOG_TAG, "Create Account Model $user@$url with token $token")

        this.user = user
        this.url = url
        this.token = token
    }

    private constructor(parcel: Parcel) {
        this.id = parcel.readLong()
        this.alias = parcel.readString()
        this.user = parcel.readString().toString()
        this.token = parcel.readString()
        this.url = parcel.readString().toString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(this.id)
        dest.writeString(this.alias)
        dest.writeString(this.user)
        dest.writeString(this.token)
        dest.writeString(this.url)
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