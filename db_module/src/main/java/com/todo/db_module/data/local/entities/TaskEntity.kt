package com.todo.db_module.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_entity")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val taskId: Int = 0,
    val taskName: String,
    val isCompleted: Boolean = false
)