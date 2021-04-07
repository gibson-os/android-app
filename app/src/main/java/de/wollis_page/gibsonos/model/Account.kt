package de.wollis_page.gibsonos.model

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.orm.SugarRecord
import de.wollis_page.gibsonos.dto.desktop.App
import de.wollis_page.gibsonos.helper.Config
import kotlin.collections.ArrayList

class Account : SugarRecord, Parcelable {
    var alias: String? = null
    var user: String = ""
    var token: String? = null
    var url: String = ""
    var apps: MutableList<App> = ArrayList()

    constructor() {}

    constructor(user: String, url: String, token: String?) {
        Log.i(Config.LOG_TAG, "Create Account Model $user@$url with token $token")

        this.user = user
        this.url = url
        this.token = token
    }

    constructor(user: String, url: String, token: String?, alias: String?) {
        Log.i(Config.LOG_TAG, "Create Account Model $user@$url with token $token under alias $alias")

        this.user = user
        this.url = url
        this.token = token
        this.alias = alias
    }

    private constructor(`in`: Parcel) {
        readFromParcel(`in`)
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(this.id)
        dest.writeString(this.alias)
        dest.writeString(this.user)
        dest.writeString(this.token)
        dest.writeString(this.url)
        dest.writeList(this.apps)
    }

    private fun readFromParcel(`in`: Parcel) {
        this.id = `in`.readLong()
        this.alias = `in`.readString()
        this.user = `in`.readString().toString()
        this.token = `in`.readString()
        this.url = `in`.readString().toString()
        `in`.readList(this.apps, App::class.java.classLoader)
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