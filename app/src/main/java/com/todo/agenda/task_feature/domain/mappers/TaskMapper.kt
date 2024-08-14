package com.todo.agenda.task_feature.domain.mappers

import com.todo.agenda.task_feature.domain.models.TaskModel
import com.todo.db_module.data.local.entities.TaskEntity

object TaskMapper {

    //convert Task entity to task model
    private fun TaskEntity.toTaskModel() =
        TaskModel(
            taskId = taskId,
            taskName = taskName,
            isCompleted = isCompleted
        )

    //convert task model to Entity
    fun TaskModel.toTaskEntity() =
        TaskEntity(
            taskId = taskId,
            taskName = taskName,
            isCompleted = isCompleted
        )

    //convert list of entity list to model list
    fun List<TaskEntity>.toTaskModelList() = map { it.toTaskModel() }

}