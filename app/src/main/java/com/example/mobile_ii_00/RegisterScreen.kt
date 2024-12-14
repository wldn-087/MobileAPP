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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.input.KeyboardType
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
fun RegisterScreen(navController: NavController){
    val systemUiController = rememberSystemUiController()

    LaunchedEffect(key1 = systemUiController) {
        systemUiController.setStatusBarColor(
            color = EzemGreen, // Ganti dengan warna hijau yang Anda inginkan
        )
    }

    val username = remember { mutableStateOf("") }
    val fullName = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val confirmPass = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val erorMessage = remember { mutableStateOf("") }
    val isError = remember { mutableStateOf(false) }

    val poppinsBold = FontFamily(
        Font(R.font.poppins_bold, FontWeight.Bold)
    )
    Column(
        modifier = Modifier.fillMaxSize().fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally, // Mengatur agar semua elemen berada di tengah horizontal

    ) {
        Image(
            painter = painterResource(if(isSystemInDarkTheme()) R.drawable.logo_white else R.drawable.logo_green_with_icon),
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
        Spacer(Modifier.size(24.dp))
        Text(
            "Create Account",
            fontSize = 22.sp,
            fontFamily = poppinsBold,
            modifier = Modifier
                .fillMaxWidth())
        Text("Register yourself to become our member and \n" +
                "enjoy all the benefits!",
            modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.size(24.dp))


        OutlinedTextField(
            value = username.value,
            onValueChange = {
                    newText -> username.value = newText
                isError.value = false
                erorMessage.value = ""
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            label = { Text("Username") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isError.value) Color.Red else EzemGreen,
                unfocusedBorderColor = if (isError.value) Color.Red else Color.Gray,
                cursorColor = if (isError.value) Color.Red else EzemGreen,
                focusedLabelColor = if (isError.value) Color.Red else EzemGreen
            )
        )
        OutlinedTextField(
            value = fullName.value,
            onValueChange = {
                    newText -> fullName.value = newText
                isError.value = false
                erorMessage.value = ""
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            label = { Text("Full Name") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isError.value) Color.Red else EzemGreen,
                unfocusedBorderColor = if (isError.value) Color.Red else Color.Gray,
                cursorColor = if (isError.value) Color.Red else EzemGreen,
                focusedLabelColor = if (isError.value) Color.Red else EzemGreen
            )
        )

        val emailRegex = android.util.Patterns.EMAIL_ADDRESS
        var isEmailError by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = email.value,
            onValueChange = { newText ->
                email.value = newText
                isEmailError = !emailRegex.matcher(newText).matches()
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email).copy(imeAction = ImeAction.Next),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EzemGreen,
                cursorColor = EzemGreen,
                focusedLabelColor = EzemGreen
            ),
            isError = isEmailError,
            supportingText = {
                if (isEmailError) {
                    Text(
                        text = "Invalid email format",
                        color = Color.Red
                    )
                }
            }
        )
        OutlinedTextField(
            value = password.value,
            onValueChange = { newText -> password.value = newText
                isError.value = false
                erorMessage.value = ""},
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isError.value) Color.Red else EzemGreen,
                unfocusedBorderColor = if (isError.value) Color.Red else Color.Gray,
                cursorColor = if (isError.value) Color.Red else EzemGreen,
                focusedLabelColor = if (isError.value) Color.Red else EzemGreen
            )
        )
        OutlinedTextField(
            value = confirmPass.value,
            onValueChange = { newText ->
                confirmPass.value = newText
                isError.value = false
                erorMessage.value = ""
                            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isError.value) Color.Red else EzemGreen,
                unfocusedBorderColor = if (isError.value) Color.Red else Color.Gray,
                cursorColor = if (isError.value) Color.Red else EzemGreen,
                focusedLabelColor = if (isError.value) Color.Red else EzemGreen
            )
        )

        Text(erorMessage.value, color = Color.Red)
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .width(32.dp),
            onClick = {
                if (password.value != confirmPass.value) {
                    isError.value = true
                    erorMessage.value = "Passwords do not match"
                    return@Button
                }

                CoroutineScope(Dispatchers.IO).launch {
                    val apiUrl = "http://$domain:5000/api/register/"
                    try {
                        val url = URL(apiUrl)
                        val connection = url.openConnection() as HttpURLConnection
                        connection.requestMethod = "POST"
                        connection.setRequestProperty("Content-Type", "application/json")
                        connection.doOutput = true

                        val requestBody = "{" +
                                "\"username\": \"${username.value}\"," +
                                "\"fullname\": \"${fullName.value}\"," +
                                "\"email\": \"${email.value}\"," +
                                "\"password\": \"${password.value}\"" +
                                "}"

                        val outputStream: OutputStream = connection.outputStream
                        outputStream.write(requestBody.toByteArray())
                        outputStream.flush()

                        val responseCode = connection.responseCode
                        if (responseCode == 200) {
                            val reader = BufferedReader(InputStreamReader(connection.inputStream))
                            val responseBody = reader.readLine()
                            reader.close()

                            withContext(Dispatchers.Main) {
                                isError.value = false
                                token = responseBody.toString()
                                navController.navigate("main")
                            }

                        } else if (responseCode == 400) {
                            withContext(Dispatchers.Main) {
                                isError.value = true
                                erorMessage.value = "User already exists"
                                println("Eror")
                            }
                        } else {
                            println("Error: Unexpected response code $responseCode")
                        }
                        connection.disconnect()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        println("Fatal Error")
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = EzemGreen
            )
        ) {
            Text("REGISTER", fontFamily = poppinsBold)
        }

        Spacer(Modifier.size(12.dp))
        Row {
            Text("Alreaady have an Account?  ")
            Box(modifier = Modifier
                .clickable {
                    navController.navigate("login")
                }){
                Text("Login Here", color = EzemGreen, fontWeight = FontWeight.Bold)
            }
        }
    }
}
