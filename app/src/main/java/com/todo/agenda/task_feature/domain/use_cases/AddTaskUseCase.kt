package com.todo.agenda.task_feature.domain.use_cases

import com.todo.agenda.task_feature.domain.models.TaskModel
import com.todo.agenda.task_feature.domain.repositories.TaskRepo
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(
    private val taskRepo: TaskRepo
){
    suspend fun addTask(task : TaskModel) : Long {
        return taskRepo.insertTask(task = task)
    }
}