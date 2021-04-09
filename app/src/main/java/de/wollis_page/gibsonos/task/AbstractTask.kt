package de.wollis_page.gibsonos.task

import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.base.GibsonOsActivity
import de.wollis_page.gibsonos.exception.ResponseException
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.helper.DataStore
import de.wollis_page.gibsonos.model.Account
import org.json.JSONObject

abstract class AbstractTask {
    protected fun getDataStore(
        context: GibsonOsActivity,
        account: Account,
        module: String,
        task: String,
        action: String
    ): DataStore {
        val dataStore = DataStore(context, account.url, account.token)
        dataStore.setRoute(module, task, action)

        return dataStore
    }

    protected fun run(context: GibsonOsActivity, dataStore: DataStore): JSONObject {
        context.showLoading()

        try {
            return dataStore.getData()
        } catch (exception: ResponseException) {
            exception.printStackTrace()

            throw TaskException(exception.message, exception.messageRessource)
        } catch (exception: Exception) {
            exception.printStackTrace()

            throw TaskException(exception.message ?: "Task exception")
        } finally {
            context.hideLoading()
        }
    }
}