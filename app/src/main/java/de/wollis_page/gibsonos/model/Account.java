package de.wollis_page.gibsonos.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;


public class Account extends SugarRecord implements Parcelable {

    public static final String EXTRA_ACCOUNT = "account";

    private String alias;
    private String user;
    private String password;
    private String url;

    public Account() {

    }

    public Account(String alias, String user, String password, String url) {
        setAlias(alias);
        setUser(user);
        setPassword(password);
        setUrl(url);
    }

    private Account(Parcel in) {
        readFromParcel(in);
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeString(getAlias());
        dest.writeString(getUser());
        dest.writeString(getPassword());
        dest.writeString(getUrl());
    }

    private void readFromParcel(Parcel in) {
        setId(in.readLong());
        setAlias(in.readString());
        setUser(in.readString());
        setPassword(in.readString());
        setUrl(in.readString());
    }

    public static final Parcelable.Creator<Account> CREATOR = new Parcelable.Creator<Account>() {
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        public Account[] newArray(int size) {
            return new Account[size];
        }
    };

    public int describeContents() {
        return 0;
    }
}
