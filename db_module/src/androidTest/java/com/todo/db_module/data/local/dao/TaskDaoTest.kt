package com.todo.db_module.data.local.dao


import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.todo.db_module.data.local.AppDatabase
import com.todo.db_module.data.local.entities.TaskEntity
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@SmallTest
class TaskDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var taskDao: TaskDao

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        taskDao = database.taskDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertAndGetTask() = runBlocking {
        val task = TaskEntity(taskName = "Test Task", isCompleted = false)

        // Insert Task
        val insertResult = taskDao.insert(task)
        assertEquals(1L, insertResult)

        // Retrieve Task
        val loaded = taskDao.getAllTasks()
        assertNotNull(loaded)
        assertEquals(1, loaded?.size)
        assertEquals("Test Task", loaded?.get(0)?.taskName)
        assertEquals(false, loaded?.get(0)?.isCompleted)
    }

    @Test
    fun getAllTasks() = runBlocking {
        val task1 = TaskEntity(taskName = "Task 1", isCompleted = false)
        val task2 = TaskEntity(taskName = "Task 2", isCompleted = false)

        taskDao.insert(task1)
        taskDao.insert(task2)

        val tasks = taskDao.getAllTasks()
        assertEquals(2, tasks?.size)
        assertEquals("Task 1", tasks?.get(0)?.taskName)
        assertEquals("Task 2", tasks?.get(1)?.taskName)
    }
}
