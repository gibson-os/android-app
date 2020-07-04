package de.wollis_page.gibsonos.task;

import android.content.Context;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.json.JSONObject;

import de.wollis_page.gibsonos.dto.Desktop;
import de.wollis_page.gibsonos.helper.DataStore;
import de.wollis_page.gibsonos.model.Account;

public class DesktopTask
{
    public static Desktop index(Context context, Account account) {
        DataStore dataStore = new DataStore(context, account.getUrl(), account.getToken());
        dataStore.setRoute("core", "desktop", "index");

        try {
            JSONObject response = dataStore.getData();
            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<Desktop> jsonAdapter = moshi.adapter(Desktop.class);

            return jsonAdapter.fromJson(response.getJSONObject("data").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
