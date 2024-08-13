package com.todo.db_module.data.repositories

import com.todo.db_module.data.local.dao.TaskDao
import com.todo.db_module.data.local.entities.TaskEntity
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TaskDbImplTest {

    private lateinit var taskDbImpl: TaskDbImpl
    private val taskDao = mock(TaskDao::class.java)

    @Before
    fun setUp() {
        taskDbImpl = TaskDbImpl(taskDao)
    }

    @Test
    fun testInsertTask() = runBlocking {
        val task = TaskEntity(taskId = 1, taskName = "Test Task", isCompleted = false)
        `when`(taskDao.insert(task)).thenReturn(1L)

        val result = taskDbImpl.insert(task)

        verify(taskDao).insert(task)
        assertEquals(1L, result)
    }

    @Test
    fun testGetAllTasks() = runBlocking {
        val tasks = listOf(TaskEntity(taskId = 1, taskName = "Test Task", isCompleted = false))
        `when`(taskDao.getAllTasks()).thenReturn(tasks)

        val result = taskDbImpl.getAllTasks()

        verify(taskDao).getAllTasks()
        assertEquals(tasks, result)
    }
}