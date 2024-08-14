package com.todo.agenda.core.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DebounceHelper(
    private val coroutineScope: CoroutineScope,
    private val delayMillis: Long
) {
    private var debounceJob: Job? = null

    fun debounce(action: suspend () -> Unit) {
        //cancel any ongoing job
        debounceJob?.cancel()
        //create a new job
        debounceJob = coroutineScope.launch {
            //delay the action
            delay(delayMillis)
            //perform the action
            action()
        }
    }
}
