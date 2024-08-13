package com.todo.agenda.task_feature.domain.models

data class TaskModel(
    val taskId: Int = 0,
    val taskName: String,
    val isCompleted: Boolean = false
)
