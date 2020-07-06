package de.wollis_page.gibsonos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import de.wollis_page.gibsonos.R;
import de.wollis_page.gibsonos.dto.desktop.Item;

public class DesktopAdapter extends BaseAdapter {
    private List<Item> desktop;
    private LayoutInflater inflater;

    public DesktopAdapter(Context context, List<Item> desktop) {
        inflater = LayoutInflater.from(context);
        this.desktop = desktop;
    }

    @Override
    public int getCount() {
        return desktop.size();
    }

    @Override
    public Object getItem(int i) {
        return desktop.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.desktop_list_item, viewGroup, false);
        ((TextView) view.findViewById(R.id.text)).setText(this.desktop.get(i).getText());

        return view;
    }
}
