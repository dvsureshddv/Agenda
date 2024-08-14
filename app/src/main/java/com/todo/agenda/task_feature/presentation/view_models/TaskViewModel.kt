package com.todo.agenda.task_feature.presentation.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todo.agenda.R
import com.todo.agenda.core.domain.repositories.DefaultDispatcherRepository
import com.todo.agenda.core.resources.ResourceProvider
import com.todo.agenda.core.util.DebounceHelper
import com.todo.agenda.core.util.OperationState
import com.todo.agenda.task_feature.domain.exceptions.FailedToAddTodoException
import com.todo.agenda.task_feature.domain.models.TaskModel
import com.todo.agenda.task_feature.domain.use_cases.FilterTaskUseCase
import com.todo.agenda.task_feature.domain.use_cases.TaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val taskUseCase: TaskUseCase,
    private val filterTaskUseCase: FilterTaskUseCase,
    private val dispatcherRepo: DefaultDispatcherRepository
) : ViewModel() {

    private val _taskObs: MutableStateFlow<OperationState<List<TaskModel>>> =
        MutableStateFlow(OperationState.Loading)
    val taskObs: StateFlow<OperationState<List<TaskModel>>> = _taskObs.asStateFlow()

    private val _insertTaskObs = MutableSharedFlow<OperationState<Boolean>>()
    val insertTaskObs: SharedFlow<OperationState<Boolean>> = _insertTaskObs

    private val _errorMessageState = MutableStateFlow<String?>(null)
    val errorMessageState: StateFlow<String?> = _errorMessageState

    private val _filteredTasks = MutableStateFlow(emptyList<TaskModel>())
    val filteredTasks: StateFlow<List<TaskModel>> = _filteredTasks

    private val debounceHelper = DebounceHelper(viewModelScope, 2000L)

    fun fetchAllTasks() {
        viewModelScope.launch(dispatcherRepo.io) {
            _taskObs.value = OperationState.Loading
            try {
                val taskList = taskUseCase.getAllTask()
                if (taskList.isNullOrEmpty()) {
                    _taskObs.value =
                        OperationState.Failure(failureMsg = resourceProvider.getString(R.string.add_data))
                } else {
                    _taskObs.value = OperationState.Success(taskList)
                }
            } catch (e: Exception) {
                _taskObs.value = OperationState.Failure(
                    failureMsg = e.message ?: resourceProvider.getString(R.string.add_data)
                )
            }

        }
    }

    //add task state
    fun insertTasks(data: String) {
        viewModelScope.launch(dispatcherRepo.io) {
            _insertTaskObs.emit(OperationState.Loading)
            delay(3000)
            try {
                val taskModel = TaskModel(
                    taskName = data.trim()
                )
                val status = taskUseCase.insertTask(task = taskModel)

                if (status <= 0) {
                    _insertTaskObs.emit(
                        OperationState.Failure(
                            failureMsg = resourceProvider.getString(
                                R.string.add_error
                            )
                        )
                    )
                } else {
                    _insertTaskObs.emit(OperationState.Success(true))
                }
            } catch (e: FailedToAddTodoException) {
                e.printStackTrace()
                _insertTaskObs.emit(
                    OperationState.Failure(failureMsg = resourceProvider.getString(R.string.add_error))
                )
                setErrorMessage(resourceProvider.getString(R.string.add_error))
            } catch (e: Exception) {
                e.printStackTrace()
                _insertTaskObs.emit(
                    OperationState.Failure(failureMsg = resourceProvider.getString(R.string.add_error))
                )
                setErrorMessage(resourceProvider.getString(R.string.add_error))
            }

        }
    }

    //error state
    private fun setErrorMessage(message: String) {
        viewModelScope.launch {
            _errorMessageState.value = message
        }
    }

    //clear the error state
    fun clearErrorMessage() {
        viewModelScope.launch {
            _errorMessageState.value = null
        }
    }

    //search for tasks
    fun filterTasksWithDelay(query: String) {
        //debounce for delaying the query hit
        debounceHelper.debounce {
            val taskList =
                (taskObs.replayCache.lastOrNull() as? OperationState.Success)?.response.orEmpty()
            val result = filterTaskUseCase.filterTasks(tasks = taskList, query = query)
            _filteredTasks.value = result
        }
    }

}