package com.todo.agenda.task_feature.domain.mappers

import com.todo.agenda.task_feature.domain.models.TaskModel
import com.todo.db_module.data.local.entities.TaskEntity

object TaskMapper {

    fun TaskEntity.toTaskModel() =
        TaskModel(
            taskId = taskId,
            taskName = taskName,
            isCompleted = isCompleted
        )

    fun TaskModel.toTaskEntity() =
        TaskEntity(
            taskId = taskId,
            taskName = taskName,
            isCompleted = isCompleted
        )

    fun List<TaskEntity>.toTaskModelList() = map { it.toTaskModel() }

}