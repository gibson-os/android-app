package de.wollis_page.gibsonos.process;

import android.os.Bundle;

import de.wollis_page.gibsonos.model.Account;

public class Process {

    private String title;
    private Account account;
    private Class className;
    private Bundle data;

    public Process(String title, Account account, Class className, Bundle data) {
        this.title = title;
        this.account = account;
        this.className = className;
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Class getClassName() {
        return className;
    }

    public void setClassName(Class className) {
        this.className = className;
    }

    public Bundle getData() {
        return data;
    }

    public void setData(Bundle data) {
        this.data = data;
    }
}
