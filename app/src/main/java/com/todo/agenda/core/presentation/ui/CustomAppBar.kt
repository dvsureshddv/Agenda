@file:OptIn(ExperimentalMaterial3Api::class)

package com.todo.agenda.core.presentation.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.todo.agenda.R
import com.todo.agenda.core.presentation.ui.theme.Primary
import com.todo.agenda.core.presentation.ui.theme.White


@Composable
fun CustomAppBar(
    title: String,
    isToShowBackButton: Boolean = false,
    onBackButtonClick: () -> Unit
) {
    // custom appbar
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Primary,
            titleContentColor = Color.White,
        ),
        title = {
            Text(
                text = title,
                fontSize = 19.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
            )
        },
        navigationIcon = {
            if (isToShowBackButton) {
                IconButton(onClick = onBackButtonClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back),
                        tint = White
                    )
                }
            }
        }
    )
}