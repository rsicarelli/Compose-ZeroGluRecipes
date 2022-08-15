package com.rsicarelli.zeroglu.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.ramcosta.composedestinations.DestinationsNavHost
import com.rsicarelli.zeroglu.app.ui.theme.AppTheme
import com.rsicarelli.zeroglu.app.ui.theme.HarmonizedTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        WindowCompat.setDecorFitsSystemWindows(window, false)

        super.onCreate(savedInstanceState)
        setContent {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                HarmonizedTheme {
                    Content()
                }
            } else {
                AppTheme {
                    Content()
                }
            }
        }
    }

    @Composable
    private fun Content() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            DestinationsNavHost(navGraph = NavGraphs.root)
        }
    }
}

