package de.wollis_page.gibsonos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import java.util.List;

import de.wollis_page.gibsonos.R;
import de.wollis_page.gibsonos.activity.base.AppCompartListActivity;
import de.wollis_page.gibsonos.adapter.AccountAdapter;
import de.wollis_page.gibsonos.model.Account;

public class MainActivity extends AppCompartListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Account> accounts = Account.listAll(Account.class);

        if (accounts.size() == 0) {
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

        FloatingActionButton addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        AccountAdapter accountAdapter = new AccountAdapter(getApplicationContext(), accounts);
        setListAdapter(accountAdapter);
    }
}
