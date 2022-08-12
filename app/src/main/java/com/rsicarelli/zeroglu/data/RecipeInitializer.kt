package com.rsicarelli.zeroglu.data

import android.content.Context
import androidx.startup.Initializer

class RecipeInitializer : Initializer<RecipeRemoteDataSource> {
    override fun create(context: Context): RecipeRemoteDataSource {
        return RecipeRemoteDataSource.init()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
