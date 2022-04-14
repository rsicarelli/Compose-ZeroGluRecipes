package com.rsicarelli.zeroglu_recipes.feature.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.material.card.MaterialCardView
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.rsicarelli.zeroglu_recipes.data.RecipeRemoteDataSource
import com.rsicarelli.zeroglu_recipes.feature.destinations.RecipeDetailScreenDestination
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Destination(start = true)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(RecipeRemoteDataSource())
    ),
    navigator: DestinationsNavigator
) {
    val state by viewModel.state.collectAsState()

    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        content = {
            items(state.size) {
                val recipe = state[it]
                Card(
                    onClick = { navigator.navigate(RecipeDetailScreenDestination(recipe)) },
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(32.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 24.dp, top = 16.dp, bottom = 16.dp, end = 16.dp),
                        text = recipe.title,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        })
}

class HomeViewModelFactory(private val recipeDataSource: RecipeRemoteDataSource) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(recipeDataSource) as T
        }

        throw IllegalStateException()
    }

}