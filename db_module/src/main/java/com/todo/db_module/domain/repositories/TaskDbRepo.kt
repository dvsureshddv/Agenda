package com.todo.db_module.domain.repositories

import com.todo.db_module.data.local.entities.TaskEntity

interface TaskDbRepo {

    suspend fun insert(task: TaskEntity): Long

    suspend fun getAllTasks(): List<TaskEntity>?
}