package de.wollis_page.gibsonos.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import de.wollis_page.gibsonos.R;
import de.wollis_page.gibsonos.helper.Config;
import de.wollis_page.gibsonos.helper.DataStore;
import de.wollis_page.gibsonos.model.Account;

public class LoginActivity extends AppCompatActivity {

    private List<EditText> editTexts;
    private EditText etAlias, etUser, etPassword, etUrl;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etAlias = findViewById(R.id.alias);
        etUser = findViewById(R.id.user);
        etPassword = findViewById(R.id.password);
        etUrl = findViewById(R.id.url);

        editTexts = new ArrayList<>();
        editTexts.add(etAlias);
        editTexts.add(etUser);
        editTexts.add(etPassword);
        editTexts.add(etUrl);

        etAlias.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<Account> accounts = Account.find(Account.class, "alias = ?", s.toString());

                if (accounts.size() > 0) {
                    etAlias.setError(getString(R.string.account_error_alias_exists));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnLogin = findViewById(R.id.login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hasErrors = false;

                for (EditText editText : editTexts) {
                    if (editText.getText().toString().length() == 0) {
                        editText.setError(getString(R.string.account_error_value_empty));
                    }

                    if (editText.getError() != null && editText.getError().toString().length() > 0) {
                        hasErrors = true;
                    }
                }

                if (hasErrors) {
                    return;
                }

                new LoginTask(LoginActivity.this).execute();
            }
        });
    }

    private static class LoginTask extends AsyncTask<Void, Void, JSONObject> {

        private WeakReference<LoginActivity> activityReference;
        private String alias, user, password, url;

        LoginTask(LoginActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            LoginActivity activity = activityReference.get();
            if (activity == null) return;

            alias = activity.etAlias.getText().toString();
            user = activity.etUser.getText().toString();
            password = activity.etPassword.getText().toString();
            url = activity.etUrl.getText().toString();

            activity.btnLogin.setEnabled(false);
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            LoginActivity activity = activityReference.get();
            if (activity == null) return null;

            DataStore dataStore = new DataStore(activity, url);
            dataStore.setRoute("system", "user", "login");
            dataStore.addParam("username", user);
            dataStore.addParam("password", password);
            dataStore.addParam("model", Build.MODEL);

            String response = dataStore.getData();

            try {
                return new JSONObject(response);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            LoginActivity activity = activityReference.get();
            if (activity == null) return;

            activity.btnLogin.setEnabled(true);

            if (result == null) return;

            try {
                if (result.has("failure") && result.getBoolean("failure")) {
                    return;
                }

                if (result.has("success") && !result.getBoolean("success")) {
                    return;
                }

                if (result.has("device") && result.getBoolean("device")) {
                    return;
                }

                Account account = new Account(alias, user, password, url);
                account.save();

                activity.finish();
                activity.startActivity(new Intent(activity, MainActivity.class));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.i(Config.LOG_TAG, result.toString());
        }
    }
}
