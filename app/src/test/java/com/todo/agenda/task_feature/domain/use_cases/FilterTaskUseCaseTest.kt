package com.todo.agenda.task_feature.domain.use_cases

import com.todo.agenda.task_feature.domain.models.TaskModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FilterTaskUseCaseTest {

    private lateinit var filterTaskUseCase: FilterTaskUseCase

    @Before
    fun setup() {
        filterTaskUseCase = FilterTaskUseCase()
    }

    @Test
    fun filterAllTasks() {
        val tasks = listOf(TaskModel(taskId = 1, taskName = "Task 1", isCompleted = false))
        val result = filterTaskUseCase.filterTasks(tasks, "")
        assertEquals(tasks, result)
    }

    @Test
    fun filterTasksWhenQueryMatches() {
        val tasks = listOf(
            TaskModel(taskId = 1, taskName = "Task 1", isCompleted = false),
            TaskModel(taskId = 2, taskName = "Another Task", isCompleted = false)
        )
        val query = "Task"
        val result = filterTaskUseCase.filterTasks(tasks, query)
        assertEquals(tasks.filter { it.taskName.contains(query, ignoreCase = true) }, result)
    }

    @Test
    fun filterTasksWhenQueryNotMatches() {
        val tasks = listOf(TaskModel(taskId = 1, taskName = "Task 1", isCompleted = false))
        val query = "Nonexistent"
        val result = filterTaskUseCase.filterTasks(tasks, query)
        assertEquals(emptyList<TaskModel>(), result)
    }
}
