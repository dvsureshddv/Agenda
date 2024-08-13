package com.todo.agenda.core.di

import android.content.Context
import com.todo.agenda.core.data.repositories.DefaultDispatcherImpl
import com.todo.agenda.core.domain.repositories.DefaultDispatcherRepository
import com.todo.agenda.core.resources.ResourceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Singleton
    @Provides
    fun dispatcherProvider(): DefaultDispatcherRepository = DefaultDispatcherImpl()

    @Singleton
    @Provides
    fun provideResourceProvider(@ApplicationContext context: Context): ResourceProvider {
        return ResourceProvider(context)
    }

}