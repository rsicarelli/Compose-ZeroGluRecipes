package com.rsicarelli.zeroglu.domain

import javax.annotation.concurrent.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


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

@Serializable
data class Ingredient(
    @SerialName("custom_title")
    val title: String? = null,
    val items: List<String> = emptyList(),
)

@Serializable
data class Instruction(
    @SerialName("custom_title")
    val title: String? = null,
    val steps: List<String> = emptyList(),
)

@Serializable
data class Setup(
    @SerialName("custom_title")
    val title: String? = null,
    @SerialName("bread_shapes")
    val breadShapes: List<String> = emptyList(),
    @SerialName("browning_level")
    val browningLevel: String = "",
    val programme: Long? = null,
)

@Serializable
data class Tag(
    val id: String = "",
    @SerialName("is_internal")
    val isInternal: Boolean? = null,
    val description: Map<String, String> = mapOf(),
)
