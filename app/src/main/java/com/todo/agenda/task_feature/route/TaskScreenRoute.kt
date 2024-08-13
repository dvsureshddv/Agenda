package com.todo.agenda.task_feature.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.todo.agenda.task_feature.presentation.ui.TaskScreen
import com.todo.agenda.task_feature.presentation.view_models.TaskViewModel

@Composable
fun TaskScreenRoute(

    navController: NavHostController,
    viewModel: TaskViewModel
) {
    TaskScreen( navController = navController, viewModel = viewModel )
}