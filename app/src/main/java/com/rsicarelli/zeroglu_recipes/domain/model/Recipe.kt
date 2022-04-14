package com.rsicarelli.zeroglu_recipes.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Recipe(
    val id: String = "",
    val title: String,
    @SerialName("total_time_millis")
    val totalTimeMillis: Long?,
    val setup: List<Setup>,
    val ingredients: List<Ingredient>,
    val instructions: List<Instruction>,
    val language: String
) {
    constructor() : this("", "", 0L, emptyList(), emptyList(), emptyList(), "")
}

@Serializable
data class Ingredient(
    val custom_title: String? = null,
    val items: List<String>
) {
    constructor() : this(null, emptyList())
}

@Serializable
data class Instruction(
    val custom_title: String? = null,
    val steps: List<String>
) {
    constructor() : this(null, emptyList())
}

@Serializable
data class Setup(
    val custom_title: String? = null,
    val bread_shapes: List<String>,
    val browning_level: String,
    val programme: Long? = null
) {
    constructor() : this(null, emptyList(), "", 0L)
}
