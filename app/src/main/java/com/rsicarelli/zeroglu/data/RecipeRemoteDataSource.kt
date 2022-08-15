package com.rsicarelli.zeroglu.data

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.rsicarelli.zeroglu.domain.Ingredient
import com.rsicarelli.zeroglu.domain.Instruction
import com.rsicarelli.zeroglu.domain.Recipe
import com.rsicarelli.zeroglu.domain.Setup
import com.rsicarelli.zeroglu.domain.Tag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.Contextual
import org.koin.core.annotation.Factory

private const val RecipesCollectionReference = "ZeroGlu-Pro"

interface RecipeRemoteDataSource {

    val recipes: Flow<List<Recipe>>
}

@Factory
internal class RecipeRemoteDataSourceImpl : RecipeRemoteDataSource {

    override val recipes: Flow<List<Recipe>>
        get() = recipes()

    private fun recipes(): Flow<List<Recipe>> =
        callbackFlow {
            Firebase.firestore.collection(RecipesCollectionReference)
                .addSnapshotListener { value, error ->
                    require(error == null) { error?.localizedMessage ?: "Error on firebase" }
                    requireNotNull(value)

                    launch(Dispatchers.IO) {
                        value.map { async { it.toObject<RecipeDto>() } }
                            .awaitAll()
                            .sortedBy(RecipeDto::index)
                            .map { async { it.toRecipe() } }
                            .awaitAll()
                            .let { recipes ->
                                trySendBlocking(recipes)
                            }
                    }
                }.let { awaitClose(it::remove) }
        }
}

private suspend fun DocumentReference.await() = get().await()

private data class RecipeDto(
    val index: Int,
    val title: String,
    @get:PropertyName("total_time_millis")
    @set:PropertyName("total_time_millis")
    var totalTimeMillis: Long?,
    val setup: List<SetupDto>,
    val ingredients: List<IngredientDto>,
    val instructions: List<InstructionDto>,
    val language: String,
    val tags: List<@Contextual DocumentReference>,
) {

    suspend fun toRecipe(): Recipe =
        Recipe(
            index = index,
            title = title,
            totalTimeMillis = totalTimeMillis,
            setup = setup.map(SetupDto::toDomain),
            ingredients = ingredients.map(IngredientDto::toDomain),
            instructions = instructions.map(InstructionDto::toDomain),
            language = language,
            tags = toTagDomain()
        )

    private suspend fun toTagDomain(): List<Tag> =
        coroutineScope {
            tags.map { documentReference -> async { documentReference.await() } }
                .awaitAll()
                .mapNotNull { it.toObject(Tag::class.java) }
        }

    @Suppress("unused")
    constructor() : this(
        index = 0,
        title = "",
        totalTimeMillis = 0L,
        setup = emptyList(),
        ingredients = emptyList(),
        instructions = emptyList(),
        language = "",
        tags = emptyList()
    )
}

private data class IngredientDto(
    @get:PropertyName("custom_title")
    @set:PropertyName("custom_title")
    var title: String? = null,
    val items: List<String> = emptyList(),
) {

    fun toDomain() =
        Ingredient(
            title = title,
            items = items
        )

    @Suppress("unused")
    constructor() : this("", emptyList())
}

private data class InstructionDto(
    @get:PropertyName("custom_title")
    @set:PropertyName("custom_title")
    var title: String? = null,
    val steps: List<String> = emptyList(),
) {

    fun toDomain() =
        Instruction(
            title = title,
            steps = steps
        )

    @Suppress("unused")
    constructor() : this("", emptyList())
}

private data class SetupDto(
    @get:PropertyName("custom_title")
    @set:PropertyName("custom_title")
    var title: String? = null,
    @get:PropertyName("bread_shapes")
    @set:PropertyName("bread_shapes")
    var breadShapes: List<String> = emptyList(),
    @get:PropertyName("browning_level")
    @set:PropertyName("browning_level")
    var browningLevel: String = "",
    var programme: Long? = null,
) {

    fun toDomain() =
        Setup(
            title = title,
            breadShapes = breadShapes,
            browningLevel = browningLevel,
            programme = programme
        )

    @Suppress("unused")
    constructor() : this("", emptyList(), "", 0L)
}
