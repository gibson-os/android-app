package de.wollis_page.gibsonos.activity;

import android.os.Bundle;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import de.wollis_page.gibsonos.R;
import de.wollis_page.gibsonos.activity.base.AppCompartListActivity;
import de.wollis_page.gibsonos.dto.Desktop;
import de.wollis_page.gibsonos.task.DesktopTask;

public class DesktopActivity extends AppCompartListActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_desktop);
        super.onCreate(savedInstanceState);

        final DesktopActivity activity = this;
        CompletableFuture.supplyAsync(new Supplier<Object>() {
            @Override
            public Object get() {
                Desktop desktop = DesktopTask.index(activity, activity.getAccount());

                return null;
            }
        });
    }
}
