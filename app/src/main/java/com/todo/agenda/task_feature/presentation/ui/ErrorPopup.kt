package com.todo.agenda.task_feature.presentation.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.todo.agenda.R
import com.todo.agenda.core.presentation.ui.theme.Black

@Composable
fun ErrorPopup(errorMessage: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Text(
                text = errorMessage,
                style = TextStyle(
                    fontSize = 18.sp, // Set desired font size
                    color = Black, // Set text color
                    fontWeight = FontWeight.Medium // Set font weight
                )
            )

        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.Black,
                    containerColor = Color.Transparent
                )
            ) {
                Text(
                    text = stringResource(id = R.string.ok),
                    style = TextStyle(
                        fontSize = 16.sp, // Set desired font size
                        color = Black, // Set text color
                        fontWeight = FontWeight.Medium // Set font weight
                    )
                )
            }
        }
    )
}