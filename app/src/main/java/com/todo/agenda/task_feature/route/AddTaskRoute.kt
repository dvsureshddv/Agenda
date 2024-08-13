package com.todo.agenda.task_feature.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.todo.agenda.task_feature.presentation.ui.AddTaskScreen
import com.todo.agenda.task_feature.presentation.view_models.TaskViewModel

@Composable
fun AddTaskRoute(
    viewModel: TaskViewModel,
    navController: NavHostController,
) {

    AddTaskScreen( navController = navController, viewModel = viewModel )
}