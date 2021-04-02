package de.wollis_page.gibsonos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.Nullable;
import de.wollis_page.gibsonos.R;
import de.wollis_page.gibsonos.activity.base.AppCompatListActivity;
import de.wollis_page.gibsonos.adapter.AccountAdapter;
import de.wollis_page.gibsonos.model.Account;

public class MainActivity extends AppCompatListActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        this.navigationView.setNavigationItemSelectedListener(this);
        this.loadList();

        FloatingActionButton addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            this.loadList();
        }
    }

    private void loadList() {
        final List<Account> accounts = this.application.getAccountModels();

        if (accounts.size() == 0) {
            this.startActivityForResult(new Intent(this.getApplicationContext(), LoginActivity.class), 100);
        }

        AccountAdapter accountAdapter = new AccountAdapter(this.getApplicationContext(), accounts);
        this.setListAdapter(accountAdapter);

        this.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                runActivity(DesktopActivity.class, accounts.get(i));
            }
        });
    }
}
