package com.todo.agenda.task_feature.data.local

import com.todo.agenda.task_feature.domain.mappers.TaskMapper.toTaskEntity
import com.todo.agenda.task_feature.domain.models.TaskModel
import com.todo.db_module.domain.repositories.TaskDbRepo
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class TaskRepoImplTest {

    private lateinit var taskDbRepo: TaskDbRepo
    private lateinit var taskRepoImpl: TaskRepoImpl

    @Before
    fun setup() {
        taskDbRepo = mock(TaskDbRepo::class.java)
        taskRepoImpl = TaskRepoImpl(taskDbRepo)
    }

    @Test
    fun insertTaskRecord() = runBlocking {
        val taskModel = TaskModel(taskId = 1, taskName = "Test Task", isCompleted = false)
        val taskId = 1L
        `when`(taskDbRepo.insert(taskModel.toTaskEntity())).thenReturn(taskId)

        val result = taskRepoImpl.insertTask(taskModel)

        verify(taskDbRepo).insert(taskModel.toTaskEntity())
        assertEquals(taskId, result)
    }

    @Test
    fun getAllTasks() = runBlocking {
        val taskModels = listOf(TaskModel(taskId = 1, taskName = "Test Task", isCompleted = false))
        val taskEntities = taskModels.map { it.toTaskEntity() }
        `when`(taskDbRepo.getAllTasks()).thenReturn(taskEntities)

        val result = taskRepoImpl.getAllTasks()
        verify(taskDbRepo).getAllTasks()
        assertEquals(taskModels, result)
    }
}
