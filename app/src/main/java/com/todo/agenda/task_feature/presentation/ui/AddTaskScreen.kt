@file:OptIn(ExperimentalMaterial3Api::class)

package com.todo.agenda.task_feature.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.todo.agenda.R
import com.todo.agenda.core.presentation.ui.CustomAppBar
import com.todo.agenda.core.presentation.ui.CustomCircularProgress
import com.todo.agenda.core.presentation.ui.theme.Primary
import com.todo.agenda.core.presentation.ui.theme.White
import com.todo.agenda.core.util.OperationState
import com.todo.agenda.navigation.Routes
import com.todo.agenda.task_feature.presentation.view_models.TaskViewModel
import kotlinx.coroutines.flow.collectLatest


@Composable
fun AddTaskScreen(navController: NavHostController, viewModel: TaskViewModel) {
    var taskName by rememberSaveable { mutableStateOf("") }
    var showLoading by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    var validationError by remember { mutableStateOf(false) }


    // Collect events from SharedFlow
    LaunchedEffect(Unit) {
        viewModel.insertTaskObs.collectLatest { state ->
            when (state) {
                is OperationState.Loading -> {
                    showLoading = true
                    validationError = false
                }

                is OperationState.Failure -> {
                    showLoading = false
                    validationError = false
                    focusManager.clearFocus()
                    navController.popBackStack()
                }

                is OperationState.Success -> {
                    showLoading = false
                    focusManager.clearFocus()
                    validationError = false
                    if (state.response == true) {
                        navController.popBackStack(Routes.TASK_SCREEN, inclusive = false)
                    }
                }
            }
        }
    }


    Scaffold(topBar = {

        CustomAppBar(
            title = stringResource(id = R.string.add_todo),
            isToShowBackButton = true,
            onBackButtonClick = {
                navController.popBackStack()
            }
        )
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                TextField(
                    singleLine = true,
                    value = taskName,
                    onValueChange = { newValue ->
                        validationError = false
                        if (isAlphanumericOrSpace(text = newValue)) {
                            taskName = newValue
                        }
                    },
                    textStyle = TextStyle(
                        fontSize = 18.sp, // Increase the font size of the text
                    ),
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.add_task),
                            style = TextStyle(
                                fontSize = 16.sp, // Set desired font size
                                color = Color.Gray, // Set text color
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        if (taskName.isNotEmpty()) {
                            IconButton(onClick = { taskName = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = stringResource(id = R.string.clear_text)
                                )
                            }
                        }
                    },
                )
                if (validationError) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(id = R.string.task_name_required),
                        color = Color.Red,
                        style = TextStyle(fontSize = 14.sp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary
                    ),
                    onClick = {
                        if (taskName.trim().isNotEmpty()) {
                            showLoading = true
                            focusManager.clearFocus()
                            viewModel.insertTasks(taskName)
                        } else {
                            validationError = true
                        }
                    }, modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.add_todo),
                        style = TextStyle(
                            fontSize = 18.sp, // Set desired font size
                            color = White, // Set text color
                            fontWeight = FontWeight.Medium // Set font weight
                        )
                    )
                }
            }

            if (showLoading) {
                CustomCircularProgress()
            }
        }
    }
}

fun isAlphanumericOrSpace(text: String): Boolean {
    return text.all { it.isLetterOrDigit() || it.isWhitespace() }
}

