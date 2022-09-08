package com.example.filmshelper.data.models.allMoviesTheatres

data class Result(
    val formatted_address: String,
    val geometry: Geometry,
    val icon: String,
    val icon_background_color: String,
    val icon_mask_base_uri: String,
    val name: String,
    val place_id: String,
    val reference: String,
    val types: List<String>
)