package de.wollis_page.gibsonos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import de.wollis_page.gibsonos.R;
import de.wollis_page.gibsonos.activity.base.AppCompartListActivity;
import de.wollis_page.gibsonos.activity.base.GibsonOsActivity;
import de.wollis_page.gibsonos.adapter.AccountAdapter;
import de.wollis_page.gibsonos.helper.Config;
import de.wollis_page.gibsonos.model.Account;

public class MainActivity extends AppCompartListActivity implements NavigationView.OnNavigationItemSelectedListener
{
    NavigationView nav_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final List<Account> accounts = this.loadList();

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
                //finish();
                startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 100);
            }
        });

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                runActivity(DesktopActivity.class, accounts.get(i));
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

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        this.runActivity(DesktopActivity.class, Account.findById(Account.class, item.getGroupId()));

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private List<Account> loadList() {
        final List<Account> accounts = Account.listAll(Account.class);

        if (accounts.size() == 0) {
            this.startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 100);
        }

        AccountAdapter accountAdapter = new AccountAdapter(getApplicationContext(), accounts);
        setListAdapter(accountAdapter);

        return accounts;
    }
}
