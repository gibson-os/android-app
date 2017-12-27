package de.wollis_page.gibsonos.model;

import com.orm.SugarRecord;


public class Account extends SugarRecord {

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
}
