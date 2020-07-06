package de.wollis_page.gibsonos.dto;

import java.util.List;

import androidx.annotation.NonNull;
import de.wollis_page.gibsonos.dto.desktop.App;
import de.wollis_page.gibsonos.dto.desktop.Item;

public class Desktop
{
    @NonNull
    private List<Item> desktop;
    @NonNull
    private List<App> apps;

    public Desktop(@NonNull List<Item> desktop, @NonNull List<App> apps) {
        this.desktop = desktop;
        this.apps = apps;
    }

    @NonNull
    public List<Item> getDesktop() {
        return desktop;
    }

    public void setDesktop(@NonNull List<Item> desktop) {
        this.desktop = desktop;
    }

    @NonNull
    public List<App> getApps() {
        return apps;
    }

    public void setApps(@NonNull List<App> apps) {
        this.apps = apps;
    }
}
