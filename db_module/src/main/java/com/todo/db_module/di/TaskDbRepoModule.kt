package com.todo.db_module.di

import com.todo.db_module.data.local.dao.TaskDao
import com.todo.db_module.data.repositories.TaskDbImpl
import com.todo.db_module.domain.repositories.TaskDbRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TaskDbRepoModule {

    @Provides
    @Singleton
    fun provideTaskDbRepo(taskDao: TaskDao): TaskDbRepo = TaskDbImpl(taskDao = taskDao)

}