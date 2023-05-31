package com.mobg5.g56080.foodee

import android.util.Log
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.mobg5.g56080.foodee.database.ApplicationDatabase
import com.mobg5.g56080.foodee.fragment.login.UserForm
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Instant
import java.util.*

@RunWith(AndroidJUnit4::class)
class FoodeeDatabaseTest {

    private lateinit var userDao: UserDAO
    private lateinit var db: ApplicationDatabase

    @Before
    fun init(){
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, ApplicationDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        userDao = db.userDao
    }

    @After
    fun cleanUp(){
        db.close()
    }

    @Test
    fun insertAndGetUser(){
        val email = "maxou.info@gmail.com"
        val userForm = UserForm(email = email)
        val id = userDao.insert(userForm)
        val retrieval = userDao.get(email)
        assert(retrieval != null && id == retrieval.id)
    }

    @Test
    fun insertAuto(){
        val email = "test@gmail.com"
        val userForm = UserForm(email = email, createTime = Date.from(Instant.now()), updateTime = Date.from(
            Instant.now()))
        userDao.insert(userForm)
        val retrieval = userDao.get(email)
        Log.i("FoodeeDatabaseTest", String.format("%s - %s", retrieval?.updateTime?.toInstant().toString(), retrieval?.createTime?.toString()))
    }
}