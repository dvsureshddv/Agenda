package com.todo.db_module.domain.repositories

import com.todo.db_module.data.local.entities.TaskEntity

interface TaskDbRepo {
    //to insert a task into db
    suspend fun insert(task: TaskEntity): Long

    //fetch all saved tasks
    suspend fun getAllTasks(): List<TaskEntity>?
}