package com.todo.db_module.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.todo.db_module.data.local.dao.TaskDao
import com.todo.db_module.data.local.entities.TaskEntity
import com.todo.db_module.util.DbConstants.DATABASE_VERSION

@Database(
    entities = [TaskEntity::class],
    version = DATABASE_VERSION
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

}