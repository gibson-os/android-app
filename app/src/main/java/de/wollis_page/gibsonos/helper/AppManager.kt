package de.wollis_page.gibsonos.helper

import android.util.Log
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.AppActivityInterface
import de.wollis_page.gibsonos.exception.AppException

object AppManager {
    fun getActivityClass(module: String, task: String): Class<*> {
        val packageName = "de.wollis_page.gibsonos.module.$module.$task.activity.IndexActivity"
        Log.i(Config.LOG_TAG, "Look for package: $packageName")

        return Class.forName(packageName)
    }

    fun getAppIcon(module: String, task: String): Int {
        try {
            val activityClass = this.getActivityClass(module, task).newInstance()

            if (activityClass is AppActivityInterface) {
                return activityClass.getAppIcon()
            }

            Log.i(Config.LOG_TAG, activityClass::class.java.toString() + " is not an instance of AppActivityInterface")
        } catch (exception: ClassNotFoundException) {
            Log.i(Config.LOG_TAG, "Class for module $module and task $task doesnt exists")
        }

        return R.drawable.ic_android
    }
}