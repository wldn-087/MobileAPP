package com.example.mobile_ii_00

import android.content.Context
import android.content.res.Resources
import android.provider.Settings.Global.getString
import android.provider.Settings.Global.putString
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import androidx.navigation.NavArgs
import androidx.navigation.NavController
import androidx.navigation.navArgument
import com.example.mobile_ii_00.model.AuthDTO
import com.example.mobile_ii_00.model.LoginResponse
import com.example.mobile_ii_00.network.RetrofitInstance
import com.example.mobile_ii_00.ui.theme.EzemGreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun LoginPage(navController: NavController) {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val isError = remember { mutableStateOf(false) }
    val usernameFocusRequester = remember { FocusRequester() }
    val erorMassage = remember { mutableStateOf("") }

    val poppinsBold = FontFamily(
        Font(R.font.poppins_bold, FontWeight.Bold)
    )

    val systemUiController = rememberSystemUiController()

    LaunchedEffect(key1 = systemUiController) {
        systemUiController.setStatusBarColor(
            color = EzemGreen, // Ganti dengan warna hijau yang Anda inginkan
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally, // Mengatur agar semua elemen berada di tengah horizontal

    ) {
        Image(
            painter = painterResource(if (isSystemInDarkTheme()) R.drawable.logo_white else R.drawable.logo_green_with_icon),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 16.dp)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp, 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "LOGIN",
            fontSize = 24.sp,
            fontFamily = poppinsBold,
            modifier = Modifier
                .fillMaxWidth() // Stretch horizontally (optional for full width)

        )
        Text(
            "Login to Your Account!",
            modifier = Modifier
                .fillMaxWidth() // Stretch horizontally (optional for full width)

        )
        Spacer(modifier = Modifier.size(40.dp))
        OutlinedTextField(
            value = username.value,
            onValueChange = {
                username.value = it
                erorMassage.value = ""
                isError.value = false // Reset error when user types
            },
            singleLine = true,
            label = { Text("Username") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(usernameFocusRequester)
                .padding(bottom = 16.dp),
            isError = isError.value,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isError.value) Color.Red else EzemGreen,
                unfocusedBorderColor = if (isError.value) Color.Red else Color.Gray,
                cursorColor = if (isError.value) Color.Red else EzemGreen,
                focusedLabelColor = if (isError.value) Color.Red else EzemGreen
            )
        )

        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it
                erorMassage.value = ""
                isError.value = false // Reset error when user types
                },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isError.value) Color.Red else EzemGreen,
                unfocusedBorderColor = if (isError.value) Color.Red else Color.Gray,
                cursorColor = if (isError.value) Color.Red else EzemGreen,
                focusedLabelColor = if (isError.value) Color.Red else EzemGreen
            )
        )

        Spacer(modifier = Modifier.size(40.dp))
        Text(erorMassage.value, color = Color.Red)
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    val apiUrl = "http://$domain:5000/api/auth/"
                    try {
                        val url = URL(apiUrl)
                        val connection = url.openConnection() as HttpURLConnection
                        connection.requestMethod = "POST"
                        connection.setRequestProperty("Content-Type", "application/json")
                        connection.doOutput = true

                        val requestBody = "{\"username\": \"${username.value}\", \"password\": \"${password.value}\"}"
                        val outputStream: OutputStream = connection.outputStream
                        outputStream.write(requestBody.toByteArray())
                        outputStream.flush()

                        val responseCode = connection.responseCode
                        if (responseCode == 200) {
                            val reader = BufferedReader(InputStreamReader(connection.inputStream))
                            val responseBody = reader.readLine()
                            reader.close()

                            token = responseBody.toString()

                            withContext(Dispatchers.Main) {
                                isError.value = false
                                navController.navigate("main")
                            }

                        } else if (responseCode == 404) {
                            withContext(Dispatchers.Main) {
                                isError.value = true
                                erorMassage.value = "Make sure Username and Password are correct!"
                                usernameFocusRequester.requestFocus()
                            }
                        } else {
                            println("Error")
                        }
                        connection.disconnect()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        println("Fatal Error")
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = EzemGreen)
        ) {
            Text("LOGIN")
        }

        Spacer(Modifier.size(12.dp))
        Row {
            Text("Don't Have an Account yet?  ")
            Text(
                "Create an Account",
                color = EzemGreen,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    navController.navigate("register")
                }
            )
        }
    }
}
