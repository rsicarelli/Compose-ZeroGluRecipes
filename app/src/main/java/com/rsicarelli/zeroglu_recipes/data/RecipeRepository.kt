package com.rsicarelli.zeroglu_recipes.data

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rsicarelli.zeroglu_recipes.domain.model.Recipe
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

class RecipeRemoteDataSource {
    private val firestore = Firebase.firestore

    private val _recipes = MutableStateFlow(emptyList<Recipe>())
    val recipes: SharedFlow<List<Recipe>> = _recipes.asSharedFlow()

    private val _tags = MutableStateFlow(emptyList<Tag>())
    val tags: SharedFlow<List<Tag>> = _tags.asSharedFlow()

    suspend fun init(): Flow<Unit> {
        return callbackFlow {
            val collection = firestore.collection("ZeroGlu-Pro")
            val snapshotListener = collection.addSnapshotListener { value, error ->
                if (error == null) {
                    val list = value?.toObjects(Recipe::class.java)
                        ?.toList()
                        ?.asSequence()
                        ?.filter { it.title.isNotBlank() }
                        ?.sortedBy { it.index }
                        ?.map { recipe ->
                            runCatching {
                                runBlocking {
                                    recipe.copy(
                                        tags = withContext(this.coroutineContext) {
                                            (recipe.tags as List<DocumentReference>)
                                                .map {
                                                    it.get().await()
                                                        .toObject(Tag::class.java) as Any
                                                }
                                        }
                                    )
                                }
                            }.onFailure { println("ERROR -> ${recipe.title}") }
                                .getOrElse { recipe }
                        }
                        ?.toList()
                        ?: emptyList()
                    _recipes.tryEmit(list)
                }
            }

            val tagsCollection = firestore.collection("Tags")
            val tagsSnapshotListener = tagsCollection.addSnapshotListener { value, error ->
                if (error == null) {
                    val list = value?.toObjects(Tag::class.java)
                        ?.toList()
                        ?: emptyList()
                    _tags.tryEmit(list)
                }
            }

            awaitClose {
                snapshotListener.remove()
                tagsSnapshotListener.remove()
            }
        }
    }
}

@Serializable
data class Tag(
    val id: String,
    val description: Map<String, String>
) {
    constructor() : this("", mapOf())
}