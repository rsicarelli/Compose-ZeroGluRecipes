package com.rsicarelli.zeroglu_recipes.data

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rsicarelli.zeroglu_recipes.domain.model.Ingredient
import com.rsicarelli.zeroglu_recipes.domain.model.Instruction
import com.rsicarelli.zeroglu_recipes.domain.model.Recipe
import com.rsicarelli.zeroglu_recipes.domain.model.Setup
import com.rsicarelli.zeroglu_recipes.domain.model.Tag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class RecipeRemoteDataSource {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val _recipes = MutableStateFlow(emptyList<Recipe>())
    val recipes: SharedFlow<List<Recipe>> = _recipes.asSharedFlow()

    private val _tags = MutableStateFlow(emptyList<Tag>())
    val tags: SharedFlow<List<Tag>> = _tags.asSharedFlow()

    init {
        scope.launch {
            init().collect()
        }
    }

    private suspend fun init(): Flow<Unit> {
        return callbackFlow {
            val recipesCollection = Firebase.firestore.collection("ZeroGlu-Pro")
            val tagsCollection = Firebase.firestore.collection("Tags")

            val tagsSnapshotListener = tagsCollection.addSnapshotListener { value, error ->
                if (error == null) {
                    val list = value?.toObjects(Tag::class.java)
                        ?.toList()
                        ?: emptyList()
                    _tags.tryEmit(list)
                }
            }

            val snapshotListener = recipesCollection.addSnapshotListener { value, error ->
                if (error == null) {
                    scope.launch {
                        val list = value?.toObjects(RecipeDto::class.java)
                            ?.asSequence()
                            ?.filter { it.title.isNotBlank() }
                            ?.sortedBy { it.index }
                            ?.toList()
                            ?.map { recipe ->
                                scope.async {
                                    Pair(
                                        recipe,
                                        recipe.tags
                                            .map { scope.async { it.get().await() } }
                                            .awaitAll()
                                            .mapNotNull { it.toObject(Tag::class.java) }
                                    )
                                }
                            }?.awaitAll()
                            ?.map {
                                with(it.first) {
                                    Recipe(
                                        index = index,
                                        title = title,
                                        totalTimeMillis = totalTimeMillis,
                                        setup = setup,
                                        ingredients = ingredients,
                                        instructions = instructions,
                                        language = language,
                                        tags = it.second
                                    )
                                }
                            }
                            ?: emptyList()

                        _recipes.tryEmit(list)
                    }
                }
            }

            awaitClose {
                snapshotListener.remove()
                tagsSnapshotListener.remove()
            }
        }
    }

    companion object {
        private lateinit var recipeRemoteDataSource: RecipeRemoteDataSource

        fun init(): RecipeRemoteDataSource =
            RecipeRemoteDataSource().also { recipeRemoteDataSource = it }

        val instance: RecipeRemoteDataSource
            get() {
                if (!(::recipeRemoteDataSource.isInitialized)) {
                    error("oops")
                }
                return recipeRemoteDataSource
            }
    }
}

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
    val tags: List<@Contextual DocumentReference>,
) {
    constructor() : this(0, "", 0L, emptyList(), emptyList(), emptyList(), "", emptyList())
}