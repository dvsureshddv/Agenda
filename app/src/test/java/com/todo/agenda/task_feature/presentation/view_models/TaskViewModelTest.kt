package com.todo.agenda.task_feature.presentation.view_models

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.todo.agenda.R
import com.todo.agenda.core.domain.repositories.DefaultDispatcherRepository
import com.todo.agenda.core.resources.ResourceProvider
import com.todo.agenda.core.util.OperationState
import com.todo.agenda.task_feature.domain.models.TaskModel
import com.todo.agenda.task_feature.domain.use_cases.FilterTaskUseCase
import com.todo.agenda.task_feature.domain.use_cases.TaskUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class TaskViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var resourceProvider: ResourceProvider

    @Mock
    private lateinit var filterTaskUseCase: FilterTaskUseCase

    @Mock
    private lateinit var dispatcherRepo: DefaultDispatcherRepository


    // Use Dispatcher for testing coroutines
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Mock
    private lateinit var taskUseCase: TaskUseCase
    private lateinit var taskViewModel: TaskViewModel


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        `when`(dispatcherRepo.io).thenReturn(testDispatcher)
        taskViewModel = TaskViewModel(resourceProvider, taskUseCase, filterTaskUseCase, dispatcherRepo)
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cancel()
    }

    @Test
    fun fetchAllTasksSuccessCase() = runTest {
        // Given
        val taskList = listOf(
            TaskModel(taskId = 1, taskName = "Test Task1"),
            TaskModel(taskId = 2, taskName = "Test Task2")
        )
        `when`(taskUseCase.getAllTask()).thenReturn(taskList)

        // When
        taskViewModel.fetchAllTasks()

        // Then
        advanceUntilIdle() // Ensure all coroutines complete
        assertEquals(OperationState.Success(taskList), taskViewModel.taskObs.value)
    }


    @Test
    fun fetchAllTasksFailureWithNoData() = runTest {
        // Given
        `when`(taskUseCase.getAllTask()).thenReturn(emptyList())
        `when`(resourceProvider.getString(R.string.add_data))
            .thenReturn("No data available")
        // When
        taskViewModel.fetchAllTasks()

        // Then
        advanceUntilIdle() // Ensure all coroutines complete
        assertEquals(OperationState.Failure("No data available"), taskViewModel.taskObs.value)
    }


    @Test
    fun filterTasksWithDelayTest() = runTest {
        // Arrange
        val query = "Task"
        val taskList = listOf(
            TaskModel(taskId = 1, taskName = "Test Task"),
            TaskModel(taskId = 2, taskName = "Another Task")
        )
        val filteredList = taskList.filter { it.taskName.contains(query, ignoreCase = true) }

        `when`(taskUseCase.getAllTask()).thenReturn(taskList)
        `when`(filterTaskUseCase.filterTasks(taskList, query)).thenReturn(filteredList)

        taskViewModel.fetchAllTasks() // Assume it fetches the tasks and sets the state
        advanceUntilIdle()

        // Act
        taskViewModel.filterTasksWithDelay(query)
        advanceTimeBy(2000) // Simulate debounce delay
        advanceUntilIdle()

        // Assert
        assertEquals(filteredList, taskViewModel.filteredTasks.value)
    }

}
