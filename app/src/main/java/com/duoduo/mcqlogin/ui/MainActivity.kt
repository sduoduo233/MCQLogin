package com.duoduo.mcqlogin.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoduo.mcqlogin.ui.theme.MCQLoginTheme
import com.duoduo.mcqlogin.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MCQLoginTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Login()
                }
            }
        }
    }
}

@Composable
fun Login(
    model: MainViewModel = viewModel()
) {
    val username = model.username.observeAsState("")
    val password = model.password.observeAsState("")
    val ipAddress = model.ipAddress.observeAsState("")

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(title = {
                Text(text = "MCQLogin")
            })
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
        ) {
            OutlinedTextField(
                value = ipAddress.value,
                onValueChange = {
                    model.onIPAddressChanged(it)
                },
                label = { Text(text = "IP Address") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = username.value,
                onValueChange = {
                    model.onUsernameChanged(it)
                },
                label = { Text(text = "Username") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = password.value,
                onValueChange = {
                    model.onPasswordChanged(it)
                },
                label = { Text(text = "Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Login button
            Button(onClick = {
                model.onLogin()
            }, enabled = !model.isLoading.observeAsState().value!!) {
                // Loading animation
                if (model.isLoading.observeAsState().value!!) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier.size(12.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                }

                Text(text = "Login")
            }

            // Error
            val error = model.error.observeAsState().value!!
            if (error.isNotEmpty()) {
                model.onErrorShown()
                LaunchedEffect(scaffoldState.snackbarHostState) {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar("Error: $error", "OK")
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MCQLoginTheme {

    }
}