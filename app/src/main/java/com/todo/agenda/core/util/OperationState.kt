package com.todo.agenda.core.util

sealed class OperationState<out T> {
    data object Loading : OperationState<Nothing>()
    data class Success<T>(val response: T?) : OperationState<T>()
    data class Failure(val failureMsg: String) : OperationState<Nothing>()
}