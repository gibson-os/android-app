package de.wollis_page.gibsonos.activity.base;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.wollis_page.gibsonos.application.GibsonOsApplication;
import de.wollis_page.gibsonos.model.Account;
import de.wollis_page.gibsonos.process.Process;

public abstract class GibsonOsActivity extends AppCompatActivity {
    final public static String ACCOUNT_KEY = "account";
    private Account account;

    GibsonOsApplication application;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (GibsonOsApplication) getApplication();

        Intent intent = getIntent();
        Bundle data  = intent.getExtras();
        this.account = null;

        if (intent.hasExtra(Account.EXTRA_ACCOUNT)) {
            this.account = intent.getParcelableExtra(Account.EXTRA_ACCOUNT);
        }

        application.addToNavigation(new Process(getTitle().toString(), this.account, this.getClass(), data));
    }

    protected void runActivity(Class<?> activity) {
        this.runActivity(activity, this.account);
    }

    protected void runActivity(Class<?> activity, Account account) {
        this.finish();

        Intent intent = new Intent(this, activity);
        intent.putExtra(ACCOUNT_KEY, account);

        this.startActivity(intent);
    }

    public Account getAccount() {
        return account;
    }
}
