package de.wollis_page.gibsonos.activity.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import de.wollis_page.gibsonos.R;
import de.wollis_page.gibsonos.activity.DesktopActivity;
import de.wollis_page.gibsonos.application.GibsonOsApplication;
import de.wollis_page.gibsonos.model.Account;
import de.wollis_page.gibsonos.process.Process;

public abstract class GibsonOsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    final public static String ACCOUNT_KEY = "account";
    private Account account;

    protected GibsonOsApplication application;
    protected NavigationView navigationView;
    protected Menu accountMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.application = (GibsonOsApplication) getApplication();

        Intent intent = getIntent();
        Bundle data  = intent.getExtras();
        this.account = null;

        if (intent.hasExtra(Account.EXTRA_ACCOUNT)) {
            this.account = intent.getParcelableExtra(Account.EXTRA_ACCOUNT);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (drawer != null) {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
        }

        this.navigationView = findViewById(R.id.nav_view);
        this.navigationView.setNavigationItemSelectedListener(this);
        this.accountMenu = this.navigationView.getMenu();
        this.application.addToNavigation(new Process(getTitle().toString(), this.account, this.getClass(), data));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        this.runActivity(DesktopActivity.class, Account.findById(Account.class, item.getGroupId()));

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    protected void runActivity(Class<?> activity) {
        this.runActivity(activity, this.account);
    }

    protected void runActivity(Class<?> activity, Account account) {
        //this.finish();

        Intent intent = new Intent(this, activity);
        intent.putExtra(ACCOUNT_KEY, account);

        this.startActivity(intent);
    }

    public Account getAccount() {
        return account;
    }
}
