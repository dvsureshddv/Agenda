package com.todo.agenda.navigation


import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.todo.agenda.task_feature.presentation.view_models.TaskViewModel
import com.todo.agenda.task_feature.route.AddTaskRoute
import com.todo.agenda.task_feature.route.TaskScreenRoute


fun NavGraphBuilder.taskScreenRoute(
    navController: NavHostController,
    viewModel: TaskViewModel,
) {
    composable(route = Routes.TASK_SCREEN) {
        TaskScreenRoute(
            viewModel = viewModel,
            navController = navController
        )
    }
}

fun NavGraphBuilder.addTaskRoute(
    viewModel: TaskViewModel,
    navController: NavHostController,
) {
    composable(route = Routes.ADD_TASK) {
        AddTaskRoute(
            viewModel = viewModel,
            navController = navController
        )
    }
}

