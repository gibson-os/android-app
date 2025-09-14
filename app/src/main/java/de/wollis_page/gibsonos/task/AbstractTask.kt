package de.wollis_page.gibsonos.task

import android.util.Log
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.dto.response.Filter
import de.wollis_page.gibsonos.exception.ResponseException
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.fragment.GibsonOsFragment
import de.wollis_page.gibsonos.helper.Config
import de.wollis_page.gibsonos.helper.DataStore
import de.wollis_page.gibsonos.helper.ListInterface
import de.wollis_page.gibsonos.model.Account
import org.json.JSONObject

abstract class AbstractTask {
    protected fun getDataStore(
        account: Account,
        module: String,
        task: String,
        action: String,
        method: String = "GET",
    ): DataStore {
        val dataStore = DataStore(account.url, method, account.token)
        dataStore.setRoute(module, task, action)

        return dataStore
    }

    @Throws(TaskException::class)
    protected fun run(
        context: GibsonOsActivity,
        dataStore: DataStore,
        showLoading: Boolean = true,
        catchResponseException: Boolean = true,
    ): JSONObject {
        if (showLoading) {
            context.showLoading()
        }

        try {
            return dataStore.loadJson()
        } catch (exception: ResponseException) {
            exception.printStackTrace()

            if (catchResponseException) {
                throw TaskException(exception.message, exception.messageResource)
            }

            throw exception
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
        messageResource: Int? = null,
        showLoading: Boolean = true,
        catchResponseException: Boolean = true,
    ): E {
        val response = this.run(context, dataStore, showLoading, catchResponseException)

        return this.getJsonAdapter<E>().fromJson(response.getJSONObject("data").toString()) ?:
            throw TaskException("Object not in response!", messageResource)
    }

    protected inline fun <reified E> getJsonAdapter(): JsonAdapter<E>
    {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        return moshi.adapter(E::class.java)
    }

    protected inline fun <reified E> loadList(
        context: GibsonOsFragment,
        dataStore: DataStore,
        start: Long = 0,
        limit: Long = 1,
        messageResource: Int? = null
    ): ListResponse<E> {
        if (context is ListInterface) {
            Log.d(Config.LOG_TAG, "loadList: add filter")
            dataStore.addParam("filters", context.selectedFilters)
        }

        return this.loadList<E>(
            context.activity,
            dataStore,
            start,
            limit,
            messageResource
        )
    }

    protected inline fun <reified E> loadList(
        context: GibsonOsActivity,
        dataStore: DataStore,
        start: Long = 0,
        limit: Long = 1,
        messageResource: Int? = null
    ): ListResponse<E> {
        dataStore.setPage(start, limit)

        if (context is ListInterface) {
            Log.d(Config.LOG_TAG, "loadList: add filter")
            dataStore.addParam("filters", context.selectedFilters)
        }

        val response = this.run(context, dataStore)
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val listType = Types.newParameterizedType(MutableList::class.java, E::class.java)
        val jsonAdapter = moshi.adapter<MutableList<E>>(listType)
        var possilbeFilters: MutableMap<String, Filter>? = null
        var possibleOrders: MutableList<String>? = null

        if (response.has("filters")) {
            val responseFilters = response.get("filters")

            if (responseFilters is JSONObject) {
                val filterListType = Types.newParameterizedType(MutableMap::class.java, String::class.java, Filter::class.java)
                possilbeFilters = moshi.adapter<MutableMap<String, Filter>?>(filterListType).fromJson(responseFilters.toString())
            }
        }

        if (response.has("possibleOrders")) {
            val stringListType = Types.newParameterizedType(MutableList::class.java, String::class.java)
            possibleOrders = moshi.adapter<MutableList<String>?>(stringListType).fromJson(response.getJSONArray("possibleOrders").toString())
        }

        return ListResponse(
            jsonAdapter.fromJson(response.getJSONArray("data").toString())
                ?: throw TaskException("Data not in response!", messageResource),
            if (response.has("total")) response.getLong("total") else 0,
            start,
            limit,
            possilbeFilters,
            possibleOrders,
        )
    }
}