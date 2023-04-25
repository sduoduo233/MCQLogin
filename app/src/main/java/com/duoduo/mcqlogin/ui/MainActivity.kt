package com.duoduo.mcqlogin.ui

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoduo.mcqlogin.ui.theme.MCQLoginTheme
import com.duoduo.mcqlogin.viewmodel.MainViewModel
import com.duoduo.mcqlogin.widget.UpdateService
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            AlertDialog.Builder(this)
                .setTitle("Permission Required")
                .setMessage("Accessing background location is required to obtain the SSID of connected wifi.")
                .setPositiveButton("OK") { _, _ ->

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        requestPermissions(
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            1
                        )
                    } else {
                        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
                    }

                }
                .show()
        }

        val intent = Intent(this, UpdateService::class.java)
        intent.action = UpdateService.ACTION_UPDATE_SERVICE
        startService(intent)
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