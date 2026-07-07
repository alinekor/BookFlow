package com.example.bookflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.bookflow.di.AppModule
import com.example.bookflow.ui.theme.BookFlowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppModule.init(applicationContext)

        enableEdgeToEdge()
        setContent {
            BookFlowTheme { BookFlowApp() }
        }
    }
}