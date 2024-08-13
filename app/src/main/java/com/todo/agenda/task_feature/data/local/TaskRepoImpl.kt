package com.todo.agenda.task_feature.data.local

import com.todo.agenda.task_feature.domain.mappers.TaskMapper.toTaskEntity
import com.todo.agenda.task_feature.domain.mappers.TaskMapper.toTaskModelList
import com.todo.agenda.task_feature.domain.models.TaskModel
import com.todo.agenda.task_feature.domain.repositories.TaskRepo
import com.todo.db_module.domain.repositories.TaskDbRepo
import javax.inject.Inject

class TaskRepoImpl @Inject constructor(
    private val taskDbRepo: TaskDbRepo
) : TaskRepo {

    override suspend fun insertTask(task: TaskModel): Long {
        return taskDbRepo.insert(task = task.toTaskEntity())
    }

    override suspend fun getAllTasks(): List<TaskModel>? {
        return taskDbRepo.getAllTasks()?.toTaskModelList()
    }
}