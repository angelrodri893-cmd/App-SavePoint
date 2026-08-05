package com.example.app_savepoint.data.remote

import com.google.gson.annotations.SerializedName

data class JuegoRemotoDto(
    val id: Int,
    val title: String,
    val thumbnail: String,
    @SerializedName("short_description") val shortDescription: String,
    @SerializedName("game_url") val gameUrl: String,
    val genre: String,
    val platform: String,
    val publisher: String,
    val developer: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("freetogame_profile_url") val profileUrl: String
)

data class JuegoDetalleDto(
    val id: Int,
    val title: String,
    val thumbnail: String,
    val status: String,
    @SerializedName("short_description") val shortDescription: String,
    val description: String,
    @SerializedName("game_url") val gameUrl: String,
    val genre: String,
    val platform: String,
    val publisher: String,
    val developer: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("freetogame_profile_url") val profileUrl: String,
    @SerializedName("minimum_system_requirements") val requisitos: RequisitosDto?,
    val screenshots: List<CapturaDto> = emptyList()
)

data class RequisitosDto(
    val os: String?,
    val processor: String?,
    val memory: String?,
    val graphics: String?,
    val storage: String?
)

data class CapturaDto(val id: Int, val image: String)
