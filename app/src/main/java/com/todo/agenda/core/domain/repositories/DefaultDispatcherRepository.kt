package com.todo.agenda.core.domain.repositories

import kotlinx.coroutines.CoroutineDispatcher

interface DefaultDispatcherRepository {

    val io : CoroutineDispatcher
    val main : CoroutineDispatcher
    val default : CoroutineDispatcher
    val unconfined : CoroutineDispatcher
}