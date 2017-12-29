package de.wollis_page.gibsonos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import de.wollis_page.gibsonos.R;
import de.wollis_page.gibsonos.activity.base.AppCompartListActivity;
import de.wollis_page.gibsonos.adapter.AccountAdapter;
import de.wollis_page.gibsonos.model.Account;

public class MainActivity extends AppCompartListActivity implements NavigationView.OnNavigationItemSelectedListener  {

    NavigationView nav_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Account> accounts = Account.listAll(Account.class);

        if (accounts.size() == 0) {
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

        nav_view = findViewById(R.id.nav_view);
        Menu accountMenu = nav_view.getMenu();
        Toolbar toolbar = findViewById(R.id.toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        nav_view.setNavigationItemSelectedListener(this);

        for (Account account : accounts) {
            accountMenu.add(account.getId().intValue(),Menu.NONE,Menu.NONE, account.getAlias());
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //int id = item.getItemId();
        Toast.makeText(getApplicationContext(), "Selected: " + item.getTitle(), Toast.LENGTH_LONG).show();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
