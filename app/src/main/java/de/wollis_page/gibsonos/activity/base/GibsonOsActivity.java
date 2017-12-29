package de.wollis_page.gibsonos.activity.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import de.wollis_page.gibsonos.application.GibsonOsApplication;
import de.wollis_page.gibsonos.model.Account;
import de.wollis_page.gibsonos.process.Process;

public abstract class GibsonOsActivity extends AppCompatActivity {

    GibsonOsApplication application;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (GibsonOsApplication) getApplication();


        Intent intent = getIntent();
        Bundle data  = intent.getExtras();
        Account account = null;
        if (intent.hasExtra(Account.EXTRA_ACCOUNT)) {
            account = intent.getParcelableExtra(Account.EXTRA_ACCOUNT);
        }

        application.addToNavigation(new Process(getTitle().toString(), account, this.getClass(), data));
    }
}
