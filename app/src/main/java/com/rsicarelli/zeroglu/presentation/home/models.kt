package com.rsicarelli.zeroglu.presentation.home

import androidx.compose.runtime.Immutable
import com.rsicarelli.zeroglu.domain.model.Ingredient
import com.rsicarelli.zeroglu.domain.model.Instruction
import com.rsicarelli.zeroglu.domain.model.Recipe
import com.rsicarelli.zeroglu.domain.model.Setup
import com.rsicarelli.zeroglu.domain.model.Tag
import com.rsicarelli.zeroglu.ui.ComposeLazyList
import kotlinx.serialization.Serializable

//region HomeState
@Immutable
data class HomeState(
    val recipeItems: ComposeLazyList<RecipeItem> = hashMapOf(),
    val tags: ComposeLazyList<TagItem> = hashMapOf(),
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

fun List<Recipe>.toRecipeItems(): ComposeLazyList<RecipeItem> {
    val tagsItem = mutableMapOf<Long, RecipeItem>()

    asSequence().forEachIndexed { _, recipe ->
        with(recipe) {
            tagsItem[index.toLong()] = RecipeItem(
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
    return tagsItem
}

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
    val tags: ComposeLazyList<TagItem>,
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
internal fun List<Tag>.toTagsItem(language: String = "en"): ComposeLazyList<TagItem> {
    val tagsItem = mutableMapOf<Long, TagItem>()

    asSequence().forEachIndexed { index, (id, is_internal, description) ->
        tagsItem[index.toLong()] = TagItem(id, is_internal, requireNotNull(description[language]))
    }

    return tagsItem.toMap()
}


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
            breadShapes = BreadShapes(breadShapes),
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
value class BreadShapes(val value: List<String>)

@JvmInline
@Serializable
value class BrowningLevel(val value: String)

@JvmInline
@Serializable
value class Programme(val value: Long?)
//endregion
