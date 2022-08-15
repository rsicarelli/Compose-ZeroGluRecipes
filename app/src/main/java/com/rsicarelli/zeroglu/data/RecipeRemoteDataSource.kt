package com.rsicarelli.zeroglu.data

import android.util.Log
import com.google.firebase.firestore.DocumentReference
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
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
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
                        value.map { queryDocumentSnapshot -> async { queryDocumentSnapshot.toObject<RecipeDto>() } }
                            .awaitAll()
                            .asSequence()
                            .filter { recipeDto -> recipeDto.title.isNotBlank() }
                            .sortedBy(RecipeDto::index)
                            .toList()
                            .map { async { it.Recipe() } }
                            .awaitAll()
                            .let {
                                trySendBlocking(it)
                                    .onFailure { error ->
                                        Log.e("RecipeRemoteDataSource", "Error sending recipe back to subscriber", error)
                                    }
                            }
                    }
                }.let { awaitClose(it::remove) }
        }

    private suspend fun RecipeDto.Recipe(): Recipe =
        Recipe(
            index = index,
            title = title,
            totalTimeMillis = totalTimeMillis,
            setup = setup,
            ingredients = ingredients,
            instructions = instructions,
            language = language,
            tags = tagsDocumentReference.toTags()
        )

    private suspend fun List<DocumentReference>.toTags(): List<Tag> =
        coroutineScope {
            mapNotNull { documentReference -> async { documentReference.await() } }
                .awaitAll()
                .mapNotNull { documentSnapshot -> async { documentSnapshot.toObject<Tag>() } }
                .awaitAll()
                .filterNotNull()
        }
}

private suspend fun DocumentReference.await() = get().await()


@Serializable
data class RecipeDto(
    val index: Int,
    val title: String,
    @SerialName("total_time_millis")
    val totalTimeMillis: Long?,
    val setup: List<Setup>,
    val ingredients: List<Ingredient>,
    val instructions: List<Instruction>,
    val language: String,
    @SerialName("tags")
    val tagsDocumentReference: List<@Contextual DocumentReference>,
) {

    //Firebase requires a constructor like this to work with Serializable
    @Suppress("unused")
    constructor() : this(
        index = 0,
        title = "",
        totalTimeMillis = 0L,
        setup = emptyList(),
        ingredients = emptyList(),
        instructions = emptyList(),
        language = "",
        tagsDocumentReference = emptyList()
    )
}
