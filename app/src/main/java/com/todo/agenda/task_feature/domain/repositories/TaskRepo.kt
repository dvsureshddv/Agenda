package com.todo.agenda.task_feature.domain.repositories

import com.todo.agenda.task_feature.domain.models.TaskModel

interface TaskRepo {

    //add task
    suspend fun insertTask(task: TaskModel) : Long

    //fetch all tasks which are saved
    suspend fun getAllTasks(): List<TaskModel>?
}