package com.todo.agenda.core.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.todo.agenda.core.presentation.ui.theme.Black
import com.todo.agenda.core.presentation.ui.theme.Primary

@Composable
fun CustomCircularProgress() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x80000000)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Black
        )
    }
}