package com.todo.db_module.data.repositories

import com.todo.db_module.data.local.dao.TaskDao
import com.todo.db_module.data.local.entities.TaskEntity
import com.todo.db_module.domain.repositories.TaskDbRepo
import javax.inject.Inject

class TaskDbImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskDbRepo {

    //insert task into db
    override suspend fun insert(task: TaskEntity): Long {
        return taskDao.insert(task = task)
    }

    //fetch all saved tasks
    override suspend fun getAllTasks(): List<TaskEntity>? {
        return taskDao.getAllTasks()
    }
}