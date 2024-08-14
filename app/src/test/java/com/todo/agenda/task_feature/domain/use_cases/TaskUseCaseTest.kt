package com.todo.agenda.task_feature.domain.use_cases

import com.todo.agenda.task_feature.domain.models.TaskModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class TaskUseCaseTest {

    private lateinit var taskUseCase: TaskUseCase
    private lateinit var addTaskUseCase: AddTaskUseCase
    private lateinit var getTaskUseCase: GetTaskUseCase

    @Before
    fun setUp() {
        addTaskUseCase = mock(AddTaskUseCase::class.java)
        getTaskUseCase = mock(GetTaskUseCase::class.java)
        taskUseCase = TaskUseCase(addTaskUseCase, getTaskUseCase)
    }

    @Test
    fun insertTask() = runBlocking {
        val taskModel = TaskModel(taskId = 1, taskName = "Test Task", isCompleted = false)
        val taskId = 1L
        `when`(addTaskUseCase.addTask(taskModel)).thenReturn(taskId)

        val result = taskUseCase.insertTask(taskModel)

        verify(addTaskUseCase).addTask(taskModel)
        assertEquals(taskId, result)
    }

    @Test
    fun getAllTask() = runBlocking {
        val taskModels = listOf(TaskModel(taskId = 1, taskName = "Test Task", isCompleted = false))
        `when`(getTaskUseCase.getAllTask()).thenReturn(taskModels)

        val result = taskUseCase.getAllTask()
        verify(getTaskUseCase).getAllTask()
        assertEquals(taskModels, result)
    }
}
