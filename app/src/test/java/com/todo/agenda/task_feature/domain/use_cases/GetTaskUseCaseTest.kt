package com.todo.agenda.task_feature.domain.use_cases

import com.todo.agenda.task_feature.domain.models.TaskModel
import com.todo.agenda.task_feature.domain.repositories.TaskRepo
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class GetTaskUseCaseTest {

    private lateinit var getTaskUseCase: GetTaskUseCase
    private lateinit var taskRepo: TaskRepo

    @Before
    fun setup() {
        taskRepo = mock(TaskRepo::class.java)
        getTaskUseCase = GetTaskUseCase(taskRepo)
    }

    @Test
    fun getAllTask() = runBlocking {
        val taskModels = listOf(TaskModel(taskId = 1, taskName = "Test Task", isCompleted = false))
        `when`(taskRepo.getAllTasks()).thenReturn(taskModels)

        val result = getTaskUseCase.getAllTask()

        verify(taskRepo).getAllTasks()
        assertEquals(taskModels, result)
    }
}
