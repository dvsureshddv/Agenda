package com.todo.agenda.task_feature.domain.repositories

import com.todo.agenda.task_feature.domain.models.TaskModel

interface TaskRepo {

    suspend fun insertTask(task: TaskModel) : Long

    suspend fun getAllTasks(): List<TaskModel>?
}