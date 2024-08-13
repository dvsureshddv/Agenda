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
        // Cancel any ongoing job
        debounceJob?.cancel()
        // Create a new job
        debounceJob = coroutineScope.launch {
            // Delay the action
            delay(delayMillis)
            // Perform the action
            action()
        }
    }
}
