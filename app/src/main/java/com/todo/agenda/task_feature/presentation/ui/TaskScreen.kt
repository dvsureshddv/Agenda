@file:OptIn(ExperimentalMaterial3Api::class)

package com.todo.agenda.task_feature.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.todo.agenda.R
import com.todo.agenda.core.presentation.ui.CustomAppBar
import com.todo.agenda.core.presentation.ui.CustomCircularProgress
import com.todo.agenda.core.presentation.ui.theme.Black
import com.todo.agenda.core.presentation.ui.theme.Primary
import com.todo.agenda.core.presentation.ui.theme.White
import com.todo.agenda.core.util.OperationState
import com.todo.agenda.navigation.Routes
import com.todo.agenda.task_feature.domain.models.TaskModel
import com.todo.agenda.task_feature.presentation.view_models.TaskViewModel


@Composable
fun TaskScreen(navController: NavHostController, viewModel: TaskViewModel) {
    val tasksState by viewModel.taskObs.collectAsState()
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val filteredTasks by viewModel.filteredTasks.collectAsState()
    val errorMessage by viewModel.errorMessageState.collectAsState()


    LaunchedEffect(Unit) {
        //fetch all tasks which are stored
        viewModel.fetchAllTasks()
    }


    Scaffold(
        topBar = {
            //appbar
            CustomAppBar(
                title = stringResource(id = R.string.todo_list),
                isToShowBackButton = false,
                onBackButtonClick = {}
            )
        },
        floatingActionButton = {
            //add task action
            FloatingActionButton(
                containerColor = Primary, // Customize the background color
                contentColor = White,     // Customize the icon color
                onClick = {
                    searchQuery = ""
                    navController.navigate(Routes.ADD_TASK)
                }) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }

            //error alert
            errorMessage?.let { msg ->
                ErrorPopup(
                    errorMessage = msg,
                    onDismiss = {
                        viewModel.clearErrorMessage()
                    }
                )
            }
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {

            //observe the state when changed
            when (tasksState) {
                is OperationState.Loading -> {
                    //progress
                    CustomCircularProgress()
                }

                is OperationState.Failure -> {

                    //failed state
                    Text(
                        text = stringResource(id = R.string.add_data),
                        style = TextStyle(
                            fontSize = 18.sp,
                            color = Black,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is OperationState.Success -> {
                    val tasks = (tasksState as OperationState.Success<List<TaskModel>>).response
                    if (tasks?.isEmpty() == true) {
                        Text(
                            text = stringResource(id = R.string.add_data),
                            style = TextStyle(
                                fontSize = 22.sp,
                                color = Black,
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        Column(modifier = Modifier.fillMaxSize()) {
                            if (tasks?.isNotEmpty() == true) {

                                //search box
                                SearchBar(
                                    searchQuery = searchQuery,
                                    onSearchQueryChange = { query ->
                                        searchQuery = query
                                        viewModel.filterTasksWithDelay(query = query)
                                    },
                                    onCloseSearchClicked = {
                                        searchQuery = ""
                                        viewModel.filterTasksWithDelay("")
                                    }
                                )
                            }
                            (if (searchQuery.isEmpty()) tasks else filteredTasks)?.let {
                                // empty state handling and list
                                TaskContent(
                                    tasks = it
                                )
                            }
                        }
                    }

                }
            }

        }

    }

}


@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onCloseSearchClicked: () -> Unit
) {

    val focusManager = LocalFocusManager.current

    TextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        placeholder = {
            Text(
                text = stringResource(id = R.string.search_todo),
                color = Color.Gray,
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = Color.Gray
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onCloseSearchClicked()
                        focusManager.clearFocus()  // Hide the keyboard
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Icon",
                        tint = Color.Gray
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                focusManager.clearFocus()
                onSearchQueryChange(searchQuery)
            }
        ),
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,  // Remove underline when focused
            unfocusedIndicatorColor = Color.Transparent,  // Remove underline when unfocused
        ),
        textStyle = TextStyle(fontSize = 18.sp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}

@Composable
fun TaskContent(
    tasks: List<TaskModel>
) {
    if (tasks.isEmpty()) {
        //failed state
        Text(
            text = stringResource(id = R.string.no_results_error),
            style = TextStyle(
                fontSize = 18.sp,
                color = Black,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
        )
    } else {
        //list of tasks
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tasks.size) { index ->
                // each task item
                TaskItem(task = tasks[index])
            }
        }
    }
}


@Composable
fun TaskItem(task: TaskModel) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp), // Vertical padding for spacing between rows
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp) // Space between items
        ) {
            Text(
                text = task.taskName,
                style = TextStyle(
                    fontSize = 22.sp, // Set desired font size
                    color = Black, // Set text color
                    fontWeight = FontWeight.Medium // Set font weight
                ),
                modifier = Modifier.weight(1f)
            )
        }
        HorizontalDivider(
            thickness = 0.5.dp,
            color = Color.LightGray,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}



