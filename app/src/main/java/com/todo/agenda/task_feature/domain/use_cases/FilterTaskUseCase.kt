package com.todo.agenda.task_feature.domain.use_cases

import com.todo.agenda.task_feature.domain.models.TaskModel
import javax.inject.Inject

class FilterTaskUseCase @Inject constructor() {

    fun filterTasks(tasks: List<TaskModel>, query: String): List<TaskModel> {
        //search for the input entry in the list
        return if (query.isEmpty()) {
            tasks
        } else {
            tasks.filter { it.taskName.contains(query, ignoreCase = true) }
        }
    }
}