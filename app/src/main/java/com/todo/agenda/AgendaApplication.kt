package com.todo.agenda

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AgendaApplication  : Application(){

    override fun onCreate() {
        super.onCreate()
    }
}