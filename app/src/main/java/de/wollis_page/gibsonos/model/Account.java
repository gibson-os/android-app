package de.wollis_page.gibsonos.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

import java.util.Objects;

import androidx.annotation.NonNull;


public class Account extends SugarRecord implements Parcelable
{
    public static final String EXTRA_ACCOUNT = "account";

    private String alias;
    private String user;
    private String token;
    private String url;

    public Account()
    {
    }

    public Account(@NonNull String user, @NonNull String url, String token)
    {
        setUser(user);
        setToken(url);
        setToken(token);
    }

    public Account(@NonNull String user, @NonNull String url, String token, String alias)
    {
        setUser(user);
        setUrl(url);
        setToken(token);
        setAlias(alias);
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

    public void setUser(@NonNull String user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(@NonNull String url) {
        this.url = url;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeString(getAlias());
        dest.writeString(getUser());
        dest.writeString(getToken());
        dest.writeString(getUrl());
    }

    private void readFromParcel(Parcel in) {
        setId(in.readLong());
        setAlias(in.readString());
        setUser(Objects.requireNonNull(in.readString()));
        setToken(in.readString());
        setUrl(Objects.requireNonNull(in.readString()));
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
