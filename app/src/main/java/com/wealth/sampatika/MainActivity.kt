package com.wealth.sampatika

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import com.wealth.sampatika.ui.navigation.SampatikaApp
import com.wealth.sampatika.ui.theme.SampatikaTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            SampatikaTheme {
                SampatikaApp()
            }
        }
    }
}