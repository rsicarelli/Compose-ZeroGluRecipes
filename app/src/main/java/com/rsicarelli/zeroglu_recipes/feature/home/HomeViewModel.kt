package com.rsicarelli.zeroglu_recipes.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsicarelli.zeroglu_recipes.data.RecipeRemoteDataSource
import com.rsicarelli.zeroglu_recipes.domain.model.Recipe
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    recipeRemoteDataSource: RecipeRemoteDataSource
) : ViewModel() {

    init {
        viewModelScope.launch { recipeRemoteDataSource.init().collect() }
    }

    val state: StateFlow<List<Recipe>> = recipeRemoteDataSource.recipes.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

}