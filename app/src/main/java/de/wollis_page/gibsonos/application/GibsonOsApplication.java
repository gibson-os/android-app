package de.wollis_page.gibsonos.application;

import android.util.Log;

import com.orm.SugarApp;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import de.wollis_page.gibsonos.activity.base.GibsonOsActivity;
import de.wollis_page.gibsonos.dto.Account;
import de.wollis_page.gibsonos.helper.Config;
import de.wollis_page.gibsonos.process.Process;

public class GibsonOsApplication extends SugarApp
{
    private List<Account> accounts = new ArrayList<>();

    public void onCreate() {
        super.onCreate();

        for (de.wollis_page.gibsonos.model.Account account : de.wollis_page.gibsonos.model.Account.listAll(de.wollis_page.gibsonos.model.Account.class)) {
            Log.d(Config.LOG_TAG, "onCreate: " + account.getId());
            this.addAccount(account);
        }
    }

    public void addAccount(@NonNull de.wollis_page.gibsonos.model.Account account) {
        this.accounts.add(new Account(account));
    }

    public List<Account> getAccounts() {
        return this.accounts;
    }

    public void addProcess(GibsonOsActivity activity) {
        de.wollis_page.gibsonos.model.Account account = activity.getAccount();
        Account accountDto = this.getAccount(account);

        assert accountDto != null;
        accountDto.addProccess(new Process(
                activity.getTitle().toString(),
                account,
                activity.getClass(),
                activity.getIntent().getExtras()
        ));
    }

    private Account getAccount(@NonNull de.wollis_page.gibsonos.model.Account account) {
        for (Account accountDto : this.accounts) {
            if (accountDto.getAccount().getId().equals(account.getId())) {
                return accountDto;
            }
        }

        return null;
    }

    public List<de.wollis_page.gibsonos.model.Account> getAccountModels() {
        List<de.wollis_page.gibsonos.model.Account> accounts = new ArrayList<>();

        for (Account accountDto : this.accounts) {
            accounts.add(accountDto.getAccount());
        }

        return accounts;
    }
}
