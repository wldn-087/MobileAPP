package com.example.mobile_ii_00

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobile_ii_00.ui.theme.MOBILE_II_00Theme


//https://drive.google.com/drive/folders/1k5obinAL9J02DvZou1-z5-rZqWUpiqud?usp=sharing&pli=1
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContent {
            val navController = rememberNavController()


            NavHost(navController = navController, startDestination = "login") {
                composable("register") { RegisterScreen(navController) }
                composable("login") { LoginPage(navController,
                    context = this@MainActivity) }
                composable("main") { MainScreen(navController) }
            }
        }
    }
}