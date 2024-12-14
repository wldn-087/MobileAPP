package com.example.mobile_ii_00

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mobile_ii_00.ui.theme.EzemGreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Calendar

@Composable
fun MainScreen(navController: NavController) {
    MainScreenDesign()
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    MainScreenDesign()
}

@Composable
fun MainScreenDesign() {
    val systemUiController = rememberSystemUiController()

    // State untuk menyimpan nama user
    var fullname by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val topPicks = remember { mutableStateListOf<Coffee>() }
    val greeting = remember { getGreetingMessage() }

    // Mengubah warna status bar
    LaunchedEffect(systemUiController) {
        systemUiController.setStatusBarColor(color = EzemGreen)
    }
    val poppinsBold = FontFamily(
        Font(R.font.poppins_bold, FontWeight.Bold)
    )

    // Ambil data user saat halaman dibuka
    LaunchedEffect(token) {
        val apiUrl = "http://$domain:5000/api/me/"

        // Mengambil data dari API
        try {
            withContext(Dispatchers.IO) {
                val url = URL(apiUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("Authorization", "Bearer $token")
                connection.connect()

                val responseCode = connection.responseCode
                if (responseCode == 200) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val responseBody = reader.readLine()
                    reader.close()

                    // Parsing JSON respons
                    val user = com.google.gson.Gson().fromJson(responseBody, Map::class.java)
                    val fetchedUsername = user["fullName"] as String?
                    fetchedUsername?.let {
                        fullname = it
                    }
                } else {
                    errorMessage = "Gagal mengambil data user: $responseCode"
                }
                connection.disconnect()
            }
        } catch (e: Exception) {
            errorMessage = "Terjadi kesalahan: ${e.message}"
        }
    }
    LaunchedEffect(Unit) {
        val apiUrl = "http://$domain:5000/api/coffee/top-picks/"
        try {
            withContext(Dispatchers.IO) {
                val url = URL(apiUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("Authorization", "Bearer $token")
                connection.connect()

                val responseCode = connection.responseCode
                if (responseCode == 200) {
                    val reader = connection.inputStream.bufferedReader()
                    val responseBody = reader.readText()
                    reader.close()

                    val coffeeList = com.google.gson.Gson().fromJson(responseBody, Array<Coffee>::class.java)
                    topPicks.addAll(coffeeList)
                } else {
                    errorMessage = "Gagal memuat data: $responseCode"
                }
                connection.disconnect()
            }
        } catch (e: Exception) {
            errorMessage = "Terjadi kesalahan: ${e.message}"
        }
    }

    // Tampilan layar utama
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Spacer(modifier = Modifier.size(24.dp))
            Text(greeting)
            when {
                fullname != null -> {
                    Text(text = "$fullname",
                        fontFamily = poppinsBold,
                        fontSize = 18.sp)
                }
                errorMessage != null -> {
                    Text(text = errorMessage ?: "Terjadi kesalahan")
                }
                else -> {
                    Text(text = "Loading...",
                        fontFamily = poppinsBold,
                        fontSize = 18.sp)
                }
            }
        }
        item { Text("Top Picks : ",
            fontFamily = poppinsBold) }

        if (errorMessage != null) {
            item {
                Text(text = errorMessage ?: "Terjadi kesalahan", fontSize = 16.sp)
            }
        } else {
            items(topPicks) { coffee ->
                CoffeeCard(coffee = coffee)
            }
        }
    }
}

fun getGreetingMessage(): String {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)

    return when {
        hour in 5..11 -> "Good Morning"
        hour in 12..16 -> "Good Afternoon"
        hour in 17..20 -> "Good Evening"
        else -> "Good Night"
    }
}

@Composable
fun CoffeeCard(coffee: Coffee) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Elevasi Material3
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageBitmap = remember { mutableStateOf<Bitmap?>(null) }

            LaunchedEffect(coffee.imagePath) {
                withContext(Dispatchers.IO) {
                    try {
                        val imageUrl = "http://$domain:5000/images/${coffee.imagePath}"
                        val connection = URL(imageUrl).openConnection() as HttpURLConnection
                        connection.connect()
                        val inputStream = connection.inputStream
                        imageBitmap.value = BitmapFactory.decodeStream(inputStream)
                        connection.disconnect()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            imageBitmap.value?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = coffee.name,
                    modifier = Modifier
                        .size(64.dp)
                        .padding(end = 16.dp)
                )
            } ?: Box(
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Loading...")
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Detail Teks
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = coffee.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = coffee.category,
                    fontSize = 12.sp,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$${coffee.price}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            // Rating dengan bintang
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(EzemGreen) // Hijau sesuai gambar
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = coffee.rating.toString(),
                        fontSize = 12.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


// Data Model
data class Coffee(
    val id: Int,
    val name: String,
    val category: String,
    val rating: Double,
    val price: Double,
    val imagePath: String
)

data class CoffeeCategory(
    val id: Int,
    val name: String
)
