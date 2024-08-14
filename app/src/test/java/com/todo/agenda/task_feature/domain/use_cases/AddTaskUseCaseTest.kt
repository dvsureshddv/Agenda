package com.todo.agenda.task_feature.domain.use_cases

import com.todo.agenda.task_feature.domain.exceptions.FailedToAddTodoException
import com.todo.agenda.task_feature.domain.models.TaskModel
import com.todo.agenda.task_feature.domain.repositories.TaskRepo
import junit.framework.Assert.fail
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class AddTaskUseCaseTest {

    private lateinit var addTaskUseCase: AddTaskUseCase
    private lateinit var taskRepo: TaskRepo

    @Before
    fun setup() {
        taskRepo = mock(TaskRepo::class.java)
        addTaskUseCase = AddTaskUseCase(taskRepo)
    }

    @Test
    fun exceptionWhenTaskNameIsError() = runBlocking {
        val taskModel = TaskModel(1, taskName = "Error", isCompleted = false)

        try {
            addTaskUseCase.addTask(taskModel)
            fail("Expected FailedToAddTodoException to be thrown")
        } catch (e: FailedToAddTodoException) {
            assertEquals("Failed to add TODO", e.message)
        }
    }

    @Test
    fun addTask() = runBlocking {
        val taskModel = TaskModel(taskId = 1, taskName = "Test Task", isCompleted = false)
        val taskId = 1L
        `when`(taskRepo.insertTask(taskModel)).thenReturn(taskId)

        val result = addTaskUseCase.addTask(taskModel)

        verify(taskRepo).insertTask(taskModel)
        assertEquals(taskId, result)
    }

}
