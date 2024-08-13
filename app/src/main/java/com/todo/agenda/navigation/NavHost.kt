package com.todo.agenda.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.todo.agenda.task_feature.presentation.view_models.TaskViewModel

@Composable
fun APPNavHost() {
    val navController = rememberNavController()
    val activity = LocalContext.current as? Activity
    val taskViewModel: TaskViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = Routes.TASK_SCREEN) {

        taskScreenRoute(
            viewModel = taskViewModel,
            navController = navController
        )

        addTaskRoute(
            viewModel = taskViewModel,
            navController = navController
        )
    }
}


