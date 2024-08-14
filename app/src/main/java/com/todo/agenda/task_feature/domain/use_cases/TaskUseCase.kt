package com.todo.agenda.task_feature.domain.use_cases

import com.todo.agenda.task_feature.domain.models.TaskModel
import javax.inject.Inject

class TaskUseCase @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase,
    private val getTaskUseCase: GetTaskUseCase,
) {

    //insert a task into local
    suspend fun insertTask(task: TaskModel): Long {
        return addTaskUseCase.addTask(task = task)
    }

    //fetch all tasks which are stored
    suspend fun getAllTask(): List<TaskModel>? {
        return getTaskUseCase.getAllTask()
    }
}