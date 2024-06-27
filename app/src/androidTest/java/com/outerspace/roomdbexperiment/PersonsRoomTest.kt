package com.outerspace.roomdbexperiment

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.outerspace.roomdbexperiment.DataLayer.PersonDao
import com.outerspace.roomdbexperiment.DataLayer.PersonDatabase
import com.outerspace.roomdbexperiment.DataLayer.PersonEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.transformWhile
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class PersonsRoomTest {
    lateinit var db: PersonDatabase
    lateinit var dao: PersonDao

    @Before
    fun initializeDB() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, PersonDatabase::class.java).build()
        dao = db.personDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    private fun bunchOfPersons(): List<PersonEntity> {
        return listOf(
            PersonEntity(null, "Luis", "Viruena", "Painter"),
            PersonEntity(null, "Elvi", "Chavez", "Teacher"),
            PersonEntity(null, "Sofi", "Viruena-Chavez", "Artist"),
            PersonEntity(null, "Aldo", "Viruena-Chavez", "Couch"),
        )
    }

    @Test
    fun insertPersonTest1() {
        runTest {
            val persons = bunchOfPersons()
            persons.forEach {
                Log.d("PERSON TEST", "PERSON: Insert to table: ${it.firstName} ${it.lastName}")
                dao.insertPerson(it)
            }

            val personFlow = dao.fetchPersons().take(1)

            personFlow.collect {
                it.forEach {personEntity ->
                    Log.d("PERSON TEST", "PERSON: read from flow: ${personEntity.firstName} ${personEntity.lastName}")
                    assertTrue(persons.firstOrNull { person ->
                                personEntity.firstName == person.firstName &&
                                personEntity.lastName == person.lastName } != null
                    )
                }
            }
        }
    }


    @Test
    fun insertPersonTest2() {
        runTest {
            val persons = bunchOfPersons()
            persons.forEach {
                Log.d("PERSON TEST", "PERSON: Insert to table: ${it.firstName} ${it.lastName}")
                dao.insertPerson(it)
            }

            val personFlow = dao.fetchPersons().stateIn(TestScope())

            personFlow.collect {
                it.forEach {personEntity ->
                    Log.d("PERSON TEST", "PERSON: read from flow: ${personEntity.firstName} ${personEntity.lastName}")
                    assertTrue(persons.firstOrNull { person ->
                        personEntity.firstName == person.firstName &&
                                personEntity.lastName == person.lastName } != null
                    )
                }
            }
        }
    }
}
