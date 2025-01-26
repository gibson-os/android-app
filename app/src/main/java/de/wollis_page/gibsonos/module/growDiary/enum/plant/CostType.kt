package de.wollis_page.gibsonos.module.growDiary.enum.plant

import com.squareup.moshi.Json

/**
 *
 *     case SEED = 'seed';
 *     case WATER = 'water';
 *     case SUBSTRATE = 'substrate';
 *     case FERTILIZER = 'fertilizer';
 *     case LIGHT = 'light';
 *     case CLIMATE_CONTROL = 'climateControl';
 *     case SUM = 'sum';
 */

enum class CostType {
    @Json(name="seed")
    SEED,
    @Json(name="water")
    WATER,
    @Json(name="substrate")
    SUBSTRATE,
    @Json(name="fertilizer")
    FERTILIZER,
    @Json(name="light")
    LIGHT,
    @Json(name="climateControl")
    CLIMATE_CONTROL,
    @Json(name="sum")
    SUM,
}