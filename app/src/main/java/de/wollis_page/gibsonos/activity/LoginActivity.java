package de.wollis_page.gibsonos.activity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import de.wollis_page.gibsonos.R;
import de.wollis_page.gibsonos.helper.Config;
import de.wollis_page.gibsonos.helper.DataStore;

public class LoginActivity extends AppCompatActivity {

    private EditText etAlias, etUser, etPassword, etUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etAlias = findViewById(R.id.alias);
        etUser = findViewById(R.id.user);
        etPassword = findViewById(R.id.password);
        etUrl = findViewById(R.id.url);

        Button btnLogin = findViewById(R.id.login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoginTask(LoginActivity.this).execute();
            }
        });
    }

    private static class LoginTask extends AsyncTask<Void, Void, JSONObject> {

        private WeakReference<LoginActivity> activityReference;

        LoginTask(LoginActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            LoginActivity activity = activityReference.get();
            if (activity == null) return null;

            DataStore dataStore = new DataStore(activity, activity.etUrl.getText().toString());
            dataStore.setRoute("system", "user", "login");
            dataStore.addParam("username", activity.etUrl.getText().toString());
            dataStore.addParam("password", activity.etPassword.getText().toString());
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
            if (result == null) return;

            LoginActivity activity = activityReference.get();
            if (activity == null) return;

            Log.i(Config.LOG_TAG, result.toString());


        }
    }
}
