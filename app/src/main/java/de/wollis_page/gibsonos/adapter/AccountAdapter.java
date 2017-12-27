package de.wollis_page.gibsonos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import de.wollis_page.gibsonos.R;
import de.wollis_page.gibsonos.model.Account;

public class AccountAdapter extends BaseAdapter {
    private List<Account> accounts;
    private LayoutInflater inflater;

    public AccountAdapter(Context context, List<Account> accounts) {
        inflater = LayoutInflater.from(context);
        this.accounts = accounts;
    }

    @Override
    public int getCount() {
        return accounts.size();
    }

    @Override
    public Object getItem(int position) {
        return accounts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return accounts.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.account_list_item, parent, false);
        ((TextView) view.findViewById(R.id.alias)).setText(accounts.get(position).getAlias());
        ((TextView) view.findViewById(R.id.url)).setText(accounts.get(position).getUrl());
        ((TextView) view.findViewById(R.id.user)).setText(accounts.get(position).getUser());

        return view;
    }
}
