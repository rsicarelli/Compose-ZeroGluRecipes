package com.rsicarelli.zeroglu.presentation.home

import androidx.compose.runtime.Immutable
import com.rsicarelli.zeroglu.R
import com.rsicarelli.zeroglu.domain.Ingredient
import com.rsicarelli.zeroglu.domain.Instruction
import com.rsicarelli.zeroglu.domain.Recipe
import com.rsicarelli.zeroglu.domain.Setup
import com.rsicarelli.zeroglu.domain.Tag
import kotlinx.serialization.Serializable

//region HomeState
@Immutable
data class HomeState(
    val recipeItems: List<RecipeItem> = listOf(),
    val tags: List<TagItem> = listOf(),
    val selectedTags: Sequence<TagItem> = emptySequence(),
    val errorItem: ErrorItem? = null,
)
//endregion

//region ErrorItem
sealed interface ErrorItem
object UnknownError : ErrorItem
//endregion

//region RecipeItem
internal

fun List<Recipe>.toRecipeItems(selectedTags: Sequence<TagItem>): List<RecipeItem> =
    asSequence()
        .map {
            with(it) {
                RecipeItem(
                    index = index,
                    title = title,
                    totalTimeMillis = totalTimeMillis,
                    setup = setup.toSetupItems(),
                    ingredients = ingredients.toIngredientsItems(),
                    instructions = instructions.toInstructionsItems(),
                    language = language,
                    tags = tags.toTagsItem(),
                )
            }
        }
        .filter { it.tags.containsAll(selectedTags.toList()) }
        .toList()

@Immutable
@Serializable
data class RecipeItem(
    val index: Int,
    val title: String,
    val totalTimeMillis: Long?,
    val setup: List<SetupItem>,
    val ingredients: List<IngredientItem>,
    val instructions: List<InstructionItem>,
    val language: String,
    val tags: List<TagItem>,
)
//endregion

//region IngredientItem
private fun List<Ingredient>.toIngredientsItems(): List<IngredientItem> =
    asSequence()
        .first()
        .items
        .asSequence()
        .map { it.splitToSequence(":") }
        .map { IngredientItem(Pair(it.first().trim(), it.last().trim())) }
        .toList()

@JvmInline
@Serializable
value class IngredientItem(val value: Pair<String, String>)
//endregion

//region TagItem
internal fun List<Tag>.toTagsItem(language: String = "en"): List<TagItem> =
    asSequence().map {
        TagItem(
            id = it.id,
            isInternal = it.isInternal,
            description = it.description[language] ?: ""
        )
    }.toList()


typealias TagDescription = String

@Immutable
@Serializable
data class TagItem(
    val id: String,
    val isInternal: Boolean?,
    val description: TagDescription,
)
//endregion

//region InstructionItem
private fun List<Instruction>.toInstructionsItems(): List<InstructionItem> =
    asSequence()
        .filter { it.steps.isNotEmpty() }
        .map { (title, steps) -> InstructionItem(title, steps) }
        .toList()

@Immutable
@Serializable
data class InstructionItem(
    val title: String?,
    val steps: List<String>,
)
//endregion

//region SetupItems
private fun List<Setup>.toSetupItems(): List<SetupItem> =
    asSequence().map { (title, breadShapes, browningLevel, programme) ->
        SetupItem(
            title = title,
            breadShapes = BreadShapes(breadShapes.map(::BreadShape)),
            browningLevel = BrowningLevel(browningLevel),
            programme = Programme(programme)
        )
    }.toList()

@Immutable
@Serializable
data class SetupItem(
    val title: String?,
    val breadShapes: BreadShapes,
    val browningLevel: BrowningLevel,
    val programme: Programme,
)

@JvmInline
@Serializable
value class BreadShapes(val value: List<BreadShape>)

@JvmInline
@Serializable
value class BreadShape(val value: String)

@JvmInline
@Serializable
value class BrowningLevel(val value: String)

@JvmInline
@Serializable
value class Programme(val value: Long?)
//endregion
