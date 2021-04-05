package de.wollis_page.gibsonos.model

import android.os.Parcel
import android.os.Parcelable
import com.orm.SugarRecord
import java.util.*

class Account : SugarRecord, Parcelable {
    var alias: String? = null
    var user: String = ""
    var token: String? = null
    var url: String = ""

    constructor() {}

    constructor(user: String, url: String, token: String?) {
        this.user = user
        this.token = url
        this.token = token
    }

    constructor(user: String, url: String, token: String?, alias: String?) {
        this.user = user
        this.url = url
        this.token = token
        this.alias = alias
    }

    private constructor(`in`: Parcel) {
        readFromParcel(`in`)
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(alias)
        dest.writeString(user)
        dest.writeString(token)
        dest.writeString(url)
    }

    private fun readFromParcel(`in`: Parcel) {
        id = `in`.readLong()
        alias = `in`.readString()
        user = `in`.readString().toString()
        token = `in`.readString()
        url = `in`.readString().toString()
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        const val EXTRA_ACCOUNT = "account"
        @JvmField val CREATOR: Parcelable.Creator<Account> = object : Parcelable.Creator<Account> {
            override fun createFromParcel(`in`: Parcel): Account {
                return Account(`in`)
            }

            override fun newArray(size: Int): Array<Account?> {
                return arrayOfNulls(size)
            }
        }
    }
}