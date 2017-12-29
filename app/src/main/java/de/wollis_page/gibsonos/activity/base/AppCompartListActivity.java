package de.wollis_page.gibsonos.activity.base;

import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;

public abstract class AppCompartListActivity extends AppCompatActivity {
    private ListView listView;

    protected ListView getListView() {
        if (listView == null) {
            listView = findViewById(android.R.id.list);
        }

        return listView;
    }

    protected void setListAdapter(ListAdapter adapter) {
        getListView().setAdapter(adapter);
    }
}
