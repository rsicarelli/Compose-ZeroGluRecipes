package com.rsicarelli.zeroglu_recipes.domain.model

import kotlinx.serialization.Serializable
import javax.annotation.concurrent.Immutable


@Immutable
@Serializable
data class Recipe(
    val index: Int,
    val title: String,
    val totalTimeMillis: Long?,
    val setup: List<Setup> = emptyList(),
    val ingredients: List<Ingredient> = emptyList(),
    val instructions: List<Instruction> = emptyList(),
    val language: String = "",
    val tags: List<Tag> = emptyList(),
)

@Immutable
@Serializable
data class Ingredient(
    val custom_title: String? = null,
    val items: List<String> = emptyList()
)

@Immutable
@Serializable
data class Instruction(
    val custom_title: String? = null,
    val steps: List<String> = emptyList()
)

@Immutable
@Serializable
data class Setup(
    val custom_title: String? = null,
    val bread_shapes: List<String> = emptyList(),
    val browning_level: String = "",
    val programme: Long? = null
)

@Immutable
@Serializable
data class Tag(
    val id: String = "",
    val is_internal: Boolean? = null,
    val description: Map<String, String> = mapOf()
)