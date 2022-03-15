package de.wollis_page.gibsonos.task

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.exception.ResponseException
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.helper.DataStore
import de.wollis_page.gibsonos.model.Account
import org.json.JSONObject

abstract class AbstractTask {
    protected fun getDataStore(
        account: Account,
        module: String,
        task: String,
        action: String
    ): DataStore {
        val dataStore = DataStore(account.url, account.token)
        dataStore.setRoute(module, task, action)

        return dataStore
    }

    @Throws(TaskException::class)
    protected fun run(context: GibsonOsActivity, dataStore: DataStore, showLoading: Boolean = true): JSONObject {
        if (showLoading) {
            context.showLoading()
        }

        try {
            return dataStore.loadJson()
        } catch (exception: ResponseException) {
            exception.printStackTrace()

            throw TaskException(exception.message, exception.messageRessource)
        } catch (exception: Exception) {
            exception.printStackTrace()

            throw TaskException(exception.message ?: "Task exception")
        } finally {
            if (showLoading) {
                context.hideLoading()
            }
        }
    }

    protected inline fun <reified E> loadList(
        context: GibsonOsActivity,
        dataStore: DataStore,
        start: Long = 0,
        limit: Long = 1,
    ): ListResponse<E> {
        dataStore.setPage(start, limit)
        val response = this.run(context, dataStore)
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val listType = Types.newParameterizedType(MutableList::class.java, E::class.java)
        val jsonAdapter = moshi.adapter<MutableList<E>>(listType)

        return ListResponse(
            jsonAdapter.fromJson(response.getJSONArray("data").toString())
                ?: throw TaskException("Data not in response!"),
            if (response.has("total")) response.getLong("total") else 0,
            start,
            limit
        )
    }
}