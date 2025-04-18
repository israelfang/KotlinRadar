package com.kotlinradar.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kotlinradar.app.presentation.ui.theme.KotlinRadarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinRadarTheme(
                dynamicColor = false
            ) {
                RepoListScreen()
            }
        }
    }
}
