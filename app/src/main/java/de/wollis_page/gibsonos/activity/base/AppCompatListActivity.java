package de.wollis_page.gibsonos.activity.base;

import android.widget.ListAdapter;
import android.widget.ListView;

public abstract class AppCompatListActivity extends GibsonOsActivity {
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
