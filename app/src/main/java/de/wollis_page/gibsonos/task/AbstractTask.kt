package de.wollis_page.gibsonos.task

import com.squareup.moshi.JsonAdapter
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

    protected inline fun <reified E> load(
        context: GibsonOsActivity,
        dataStore: DataStore,
        messageRessource: Int? = null
    ): E {
        val response = this.run(context, dataStore)

        return this.getJsonAdapter<E>().fromJson(response.getJSONObject("data").toString()) ?:
            throw TaskException("Object not in response!", messageRessource)
    }

    protected inline fun <reified E> getJsonAdapter(): JsonAdapter<E>
    {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        return moshi.adapter(E::class.java)
    }

    protected inline fun <reified E> loadList(
        context: GibsonOsActivity,
        dataStore: DataStore,
        start: Long = 0,
        limit: Long = 1,
        messageRessource: Int? = null
    ): ListResponse<E> {
        dataStore.setPage(start, limit)
        val response = this.run(context, dataStore)
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val listType = Types.newParameterizedType(MutableList::class.java, E::class.java)
        val jsonAdapter = moshi.adapter<MutableList<E>>(listType)

        return ListResponse(
            jsonAdapter.fromJson(response.getJSONArray("data").toString())
                ?: throw TaskException("Data not in response!", messageRessource),
            if (response.has("total")) response.getLong("total") else 0,
            start,
            limit
        )
    }
}