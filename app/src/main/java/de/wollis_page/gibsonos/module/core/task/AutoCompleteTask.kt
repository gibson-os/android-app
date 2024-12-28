package de.wollis_page.gibsonos.module.core.task

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.module.growDiary.index.dto.Fertilizer
import de.wollis_page.gibsonos.module.growDiary.index.dto.Seed
import de.wollis_page.gibsonos.module.growDiary.index.dto.fertilizer.Scheme
import de.wollis_page.gibsonos.module.obscura.template.dto.Template
import de.wollis_page.gibsonos.task.AbstractTask

object AutoCompleteTask: AbstractTask() {
    fun get(
        context: GibsonOsActivity,
        autoCompleteClassname: String,
        parameters: Map<String, String> = emptyMap()
    ): ListResponse<*> {
        val dataStore = this.getDataStore(context.getAccount(), "core", "autoComplete", "")
        dataStore.addParam("autoCompleteClassname", autoCompleteClassname)

        parameters.forEach {
            dataStore.addParam(it.key, it.value)
        }

        val response = this.run(context, dataStore)
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val clazz = this.getDtoMapping()[autoCompleteClassname]
            ?: throw TaskException("$autoCompleteClassname has no mapping")

        val listType = Types.newParameterizedType(MutableList::class.java, clazz)
        val jsonAdapter = moshi.adapter<MutableList<*>>(listType)

        return ListResponse(
            jsonAdapter.fromJson(response.getJSONArray("data").toString())
                ?: throw TaskException("Data not in response!"),
            if (response.has("total")) response.getLong("total") else 0,
        )
    }

    fun getDtoMapping(): Map<String, Class<*>>
    {
        return mapOf(
            "GibsonOS\\Module\\Obscura\\AutoComplete\\TemplateAutoComplete" to Template::class.java,
            "GibsonOS\\Module\\GrowDiary\\AutoComplete\\SeedAutoComplete" to Seed::class.java,
            "GibsonOS\\Module\\GrowDiary\\AutoComplete\\FertilizerAutoComplete" to Fertilizer::class.java,
            "GibsonOS\\Module\\GrowDiary\\AutoComplete\\Fertilizer\\SchemeAutoComplete" to Scheme::class.java,
        )
    }
}