package com.example.mobile_ii_00

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mobile_ii_00.ui.theme.EzemGreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun LoginPage(navController: NavController){
    LoginPagePrevew (
        registerBtn = {
            navController.navigate("register")
        },
        login = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginPage(){
    LoginPagePrevew(
        registerBtn = {},login = {}
    )
}

@Composable
fun LoginPagePrevew(registerBtn: () -> Unit, login: () -> Unit) {
    // State to hold the text
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val usernameFocusRequester = FocusRequester()
    val passwordFocusRequester = FocusRequester()
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
            painter = painterResource(id = R.drawable.logo_green_with_icon),
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
        // TextField untuk input username
        OutlinedTextField(
            value = username.value,
            onValueChange = { newText -> username.value = newText },
            singleLine = true,
            label = { Text("Username") }, // Pastikan label menggunakan lambda
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 16.dp)
                .focusRequester(usernameFocusRequester),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { passwordFocusRequester.requestFocus() }
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EzemGreen,
                cursorColor = EzemGreen,
                focusedLabelColor = EzemGreen
            )
        )
        OutlinedTextField(
            value = password.value,
            onValueChange = { newText -> password.value = newText },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("Password") }, // Pastikan label menggunakan lambda
            modifier = Modifier.fillMaxWidth()
                .focusRequester(passwordFocusRequester),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {

                }
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EzemGreen,
                cursorColor = EzemGreen,
                focusedLabelColor = EzemGreen
            )
        )


        Spacer(modifier = Modifier.size(40.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .width(32.dp),
            onClick = {

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = EzemGreen
            )
        ) {
            Text("LOGIN", fontFamily = poppinsBold)
        }

        Spacer(Modifier.size(12.dp))
        Row {
            Text("Don't Have an Account yet?  ")
            Box(modifier = Modifier
                .clickable {
                    registerBtn()
                }){
                Text("Create an Account", color = EzemGreen, fontWeight = FontWeight.Bold)
            }
        }

    }
}


