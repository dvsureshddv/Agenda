package com.todo.agenda.task_feature.domain.use_cases

import com.todo.agenda.task_feature.domain.exceptions.FailedToAddTodoException
import com.todo.agenda.task_feature.domain.models.TaskModel
import com.todo.agenda.task_feature.domain.repositories.TaskRepo
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(
    private val taskRepo: TaskRepo
) {
    @Throws(FailedToAddTodoException::class)
    suspend fun addTask(task: TaskModel): Long {
        //if user enters error as a input throw an exception
        if (task.taskName.equals("Error", true))
            throw FailedToAddTodoException("Failed to add TODO")

        //insert the task
        return taskRepo.insertTask(task = task)
    }
}