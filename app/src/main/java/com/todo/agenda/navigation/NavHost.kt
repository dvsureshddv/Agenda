package com.todo.agenda.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.todo.agenda.task_feature.presentation.view_models.TaskViewModel

@Composable
fun APPNavHost() {
    val navController = rememberNavController()
    val taskViewModel: TaskViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = Routes.TASK_SCREEN) {
        //route to task screen
        taskScreenRoute(
            viewModel = taskViewModel,
            navController = navController
        )
        // route to add task
        addTaskRoute(
            viewModel = taskViewModel,
            navController = navController
        )
    }
}


