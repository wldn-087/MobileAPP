package com.example.mobile_ii_00

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    // Tampilan layar utama
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
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