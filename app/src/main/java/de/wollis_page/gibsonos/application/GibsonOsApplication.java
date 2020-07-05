package de.wollis_page.gibsonos.application;

import android.util.Log;

import com.orm.SugarApp;

import java.util.ArrayList;
import java.util.List;

import de.wollis_page.gibsonos.helper.Config;
import de.wollis_page.gibsonos.process.Process;

public class GibsonOsApplication extends SugarApp
{
    List<Process> processList = new ArrayList<>();

    public void addToNavigation(Process process) {
        Log.i(Config.LOG_TAG, "Add: " + process.getTitle());
        processList.add(process);

        Log.i(Config.LOG_TAG, "ProcessList: " + processList.size());
    }
}
