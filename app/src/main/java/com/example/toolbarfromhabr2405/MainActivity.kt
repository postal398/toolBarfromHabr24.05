package com.example.toolbarfromhabr2405

import CollapsingHeader
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.toolbarfromhabr2405.ui.theme.ToolBarfromHabr2405Theme
import androidx.compose.material3.*
import androidx.compose.material.icons.filled.Home
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            ToolBarfromHabr2405Theme {
                AppNavigation()
                AppNavigationWithBottomBar()
            }
        }
    }
    @Composable
    fun AppNavigation() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "first") {
            composable("first") { CollapsingHeader(navController) }
            composable("second") { SecondScreen(navController) }
        }
    }

}



@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf("first", "second")
    BottomNavigation {
        items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(Icons.Default.Home, contentDescription = null) },
                label = { Text(screen) },
                selected = false, // You can добавить логику для проверки текущего маршрута
                onClick = { navController.navigate(screen) }
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppNavigationWithBottomBar() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) {
        NavHost(navController = navController, startDestination = "first") {
            composable("first") { CollapsingHeader(navController) }
            composable("second") { SecondScreen(navController) }
        }
    }
}