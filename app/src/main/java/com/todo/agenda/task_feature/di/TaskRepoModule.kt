package com.todo.agenda.task_feature.di

import com.todo.agenda.task_feature.data.local.TaskRepoImpl
import com.todo.agenda.task_feature.domain.repositories.TaskRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TaskRepoModule {

    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        taskRepositoryImpl: TaskRepoImpl
    ): TaskRepo
}