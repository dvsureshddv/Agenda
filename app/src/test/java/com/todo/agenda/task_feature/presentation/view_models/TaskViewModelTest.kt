package com.todo.agenda.task_feature.presentation.view_models

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.todo.agenda.R
import com.todo.agenda.core.domain.repositories.DefaultDispatcherRepository
import com.todo.agenda.core.resources.ResourceProvider
import com.todo.agenda.core.util.OperationState
import com.todo.agenda.task_feature.domain.models.TaskModel
import com.todo.agenda.task_feature.domain.use_cases.FilterTaskUseCase
import com.todo.agenda.task_feature.domain.use_cases.TaskUseCase
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any

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
        taskViewModel =
            TaskViewModel(resourceProvider, taskUseCase, filterTaskUseCase, dispatcherRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cancel()
    }

    @Test
    fun fetchAllTasksSuccessCase() = testScope.runTest {
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

        val actualState = taskViewModel.taskObs.value

        assert(actualState is OperationState.Success) {
            "Expected Success state but got $actualState"
        }
        assert((actualState as OperationState.Success).response == taskList) {
            "Expected task list $taskList but got ${actualState.response}"
        }
    }

    @Test
    fun fetchAllTasksFailureWithNoData() = testScope.runTest {
        // Given
        `when`(taskUseCase.getAllTask()).thenReturn(emptyList())
        `when`(resourceProvider.getString(R.string.add_data)).thenReturn("No data available")

        // When
        taskViewModel.fetchAllTasks()

        // Then
        advanceUntilIdle() // Ensure all coroutines complete

        val actualState = taskViewModel.taskObs.value

        assert(actualState is OperationState.Failure) {
            "Expected Failure state but got $actualState"
        }
        assert((actualState as OperationState.Failure).failureMsg == "No data available") {
            "Expected error message 'No data available' but got ${actualState.failureMsg}"
        }
    }

    @Test
    fun insertTasksEmitsSuccessCase() = testScope.runTest {
        // Mock the use case
        `when`(taskUseCase.insertTask(any())).thenReturn(1L)

        // Collect emissions
        val emissions = mutableListOf<OperationState<Boolean>>()
        val job = launch {
            taskViewModel.insertTaskObs.collect {
                emissions.add(it)
            }
        }

        // Invoke the ViewModel method
        taskViewModel.insertTasks("Test Task")

        // Advance the time to allow for the delay to complete
        advanceTimeBy(3000)

        // Ensure emissions are captured after advancing time
        withTimeoutOrNull(1000) {
            while (emissions.size < 2) {
                delay(100)
            }
        }

        // Verify the last emission
        val successEmission = emissions.last()
        assert(successEmission is OperationState.Success && successEmission.response == true)

        // Verify interaction with the mocked use case
        verify(taskUseCase).insertTask(any())

        // Cancel the collection job
        job.cancel()
    }

    @Test
    fun insertTasksEmitsFailureCaseOnStatusZero() = testScope.runTest {
        // Mock the use case to return 0, indicating failure
        `when`(taskUseCase.insertTask(any())).thenReturn(0L)

        // Setup mock error message
        `when`(resourceProvider.getString(R.string.add_error)).thenReturn("Failed to add TODO")

        // Collect emissions
        val emissions = mutableListOf<OperationState<Boolean>>()
        val job = launch {
            taskViewModel.insertTaskObs.collect {
                emissions.add(it)
                println(it)  // Debug print emissions
            }
        }

        taskViewModel.insertTasks("Test Task")

        // Advance the time to account for the delay in insertTasks
        advanceTimeBy(3000)

        // Ensure emissions are captured after advancing time
        withTimeoutOrNull(1000) {
            while (emissions.size < 2) {
                delay(100)
            }
        }

        // Verify the last emission is failure
        val failureEmission = emissions.last()
        assert(failureEmission is OperationState.Failure) {
            "Expected Failure state but got $failureEmission"
        }
        assert((failureEmission as OperationState.Failure).failureMsg == "Failed to add TODO") {
            "Expected error message 'Add task error' but got ${failureEmission.failureMsg}"
        }

        // Verify interaction with the mocked use case
        verify(taskUseCase).insertTask(any())

        // Cancel the collection job
        job.cancel()
    }

    @Test
    fun insertTasksEmitsFailureCaseOnException() = testScope.runTest {
        // Mock the use case to throw an exception
        `when`(taskUseCase.insertTask(any())).thenThrow(RuntimeException("Failed to add TODO"))

        // Setup mock error message
        `when`(resourceProvider.getString(R.string.add_error)).thenReturn("Failed to add TODO")

        // Collect emissions
        val emissions = mutableListOf<OperationState<Boolean>>()
        val job = launch {
            taskViewModel.insertTaskObs.collect {
                emissions.add(it)
                println(it)  // Debug print emissions
            }
        }

        taskViewModel.insertTasks("Error")

        // Advance the time to account for the delay in insertTasks
        advanceTimeBy(3000)

        // Ensure emissions are captured after advancing time
        withTimeoutOrNull(1000) {
            while (emissions.size < 2) {
                delay(100)
            }
        }

        // Verify the last emission is failure
        val failureEmission = emissions.last()
        assert(failureEmission is OperationState.Failure) {
            "Expected Failure state but got $failureEmission"
        }
        assert((failureEmission as OperationState.Failure).failureMsg == "Failed to add TODO") {
            "Expected error message 'Add task error' but got ${failureEmission.failureMsg}"
        }

        // Verify interaction with the mocked use case
        verify(taskUseCase).insertTask(any())

        // Cancel the collection job
        job.cancel()
    }

    @Test
    fun filterTasksWithDelayCase() = testScope.runTest {
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