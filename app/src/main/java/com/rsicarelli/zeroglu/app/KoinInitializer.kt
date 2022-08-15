package com.rsicarelli.zeroglu.app

import android.content.Context
import androidx.startup.Initializer
import com.rsicarelli.zeroglu.data.DataModule
import com.rsicarelli.zeroglu.presentation.home.HomeModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

@Suppress("unused")
class KoinInitializer : Initializer<KoinApplication> {

    override fun create(context: Context): KoinApplication =
        startKoin {
            modules(
                HomeModule().module,
                DataModule().module
            )
        }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
