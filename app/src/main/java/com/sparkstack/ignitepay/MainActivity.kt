package com.sparkstack.ignitepay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import com.sparkstack.ignitepay.ui.IgnitePayApp
import com.sparkstack.ignitepay.ui.theme.IgnitePayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IgnitePayTheme {
                Surface(modifier = Modifier.fillMaxSize()) { IgnitePayApp() }
            }
        }
    }
}
