package com.todo.db_module.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.todo.db_module.data.local.entities.TaskEntity

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskEntity): Long

    @Query("SELECT * FROM task_entity")
    suspend fun getAllTasks(): List<TaskEntity>?
}