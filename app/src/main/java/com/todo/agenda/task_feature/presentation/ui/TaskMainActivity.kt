package com.todo.agenda.task_feature.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.todo.agenda.R
import com.todo.agenda.core.presentation.ui.theme.AgendaTheme
import com.todo.agenda.navigation.APPNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskMainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //set the status bar color
        window.statusBarColor = getColor(R.color.primary)

        setContent {
            AgendaTheme {
                setContent {
                    //common nav host
                    APPNavHost()
                }
            }
        }
    }
}