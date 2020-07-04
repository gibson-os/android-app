package de.wollis_page.gibsonos.task;

import android.content.Context;
import android.os.Build;

import de.wollis_page.gibsonos.helper.DataStore;
import de.wollis_page.gibsonos.model.Account;

public class UserTask
{
    public static Account login(Context context, String url, String username, String password) {
        DataStore dataStore = new DataStore(context, url, "");
        dataStore.setRoute("core", "user", "appLogin");
        dataStore.addParam("username", username);
        dataStore.addParam("password", password);
        dataStore.addParam("model", Build.MODEL);

        try {
            return new Account(username, url, dataStore.getData().getJSONObject("data").getString("token"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
