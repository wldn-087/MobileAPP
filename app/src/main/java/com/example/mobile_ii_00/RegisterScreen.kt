package com.example.mobile_ii_00

import androidx.compose.foundation.Image
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mobile_ii_00.ui.theme.EzemGreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun RegisterScreen(navController: NavController){
    RegisterScreenPreview(
        loginBtn = {
            navController.navigate("login")
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewRegisterScreen(){
    RegisterScreenPreview(
        loginBtn = {}
    )
}

@Composable
fun RegisterScreenPreview(loginBtn: () -> Unit){

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
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            label = { Text("Username") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EzemGreen,
                cursorColor = EzemGreen,
                focusedLabelColor = EzemGreen
            )
        )
        OutlinedTextField(
            value = fullName.value,
            onValueChange = {
                    newText -> fullName.value = newText
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            label = { Text("Full Name") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EzemGreen,
                cursorColor = EzemGreen,
                focusedLabelColor = EzemGreen
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
            onValueChange = { newText -> password.value = newText },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EzemGreen,
                cursorColor = EzemGreen,
                focusedLabelColor = EzemGreen
            )
        )
        OutlinedTextField(
            value = confirmPass.value,
            onValueChange = { newText -> confirmPass.value = newText },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EzemGreen,
                cursorColor = EzemGreen,
                focusedLabelColor = EzemGreen
            )
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .width(32.dp),
            onClick = {},
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
                    loginBtn()
                }){
                Text("Login Here", color = EzemGreen, fontWeight = FontWeight.Bold)
            }
        }
    }


}