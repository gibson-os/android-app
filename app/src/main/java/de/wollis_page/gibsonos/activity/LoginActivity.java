package de.wollis_page.gibsonos.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import de.wollis_page.gibsonos.R;
import de.wollis_page.gibsonos.activity.base.GibsonOsActivity;
import de.wollis_page.gibsonos.model.Account;
import de.wollis_page.gibsonos.task.UserTask;

public class LoginActivity extends GibsonOsActivity {

    private List<EditText> editTexts;
    private EditText etAlias, etUser, etPassword, etUrl;

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

        final LoginActivity activity = this;
        Button btnLogin = findViewById(R.id.login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
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

            CompletableFuture.supplyAsync(new Supplier<Object>() {
                @Override
                public Object get() {
                    //v.setEnabled(false);
                    Account account = UserTask.login(
                        activity,
                        etUrl.getText().toString(),
                        etUser.getText().toString(),
                        etPassword.getText().toString()
                    );
                    assert account != null;
                    account.setAlias(etAlias.getText().toString());
                    account.setUrl(etUrl.getText().toString());
                    account.save();

                    //v.setEnabled(true);
                    setResult(RESULT_OK);
                    activity.finish();

                    return null;
                }
            });
            }
        });
    }
}
