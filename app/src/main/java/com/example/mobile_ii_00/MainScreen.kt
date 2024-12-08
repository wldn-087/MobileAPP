package com.example.mobile_ii_00

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobile_ii_00.ui.theme.EzemGreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun MainScreen(navController: NavController){
    MainScreenDesign()
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview(){
    MainScreenDesign()
}

@Composable
fun MainScreenDesign(){
    val systemUiController = rememberSystemUiController()

    LaunchedEffect(key1 = systemUiController) {
        systemUiController.setStatusBarColor(
            color = EzemGreen,
        )

    }

    val items = List(100) { "Item $it" }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {


        items(items) { item ->
            Text( item, modifier = Modifier.padding(8.dp))
        }

    }
}