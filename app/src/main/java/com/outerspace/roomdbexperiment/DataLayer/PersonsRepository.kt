package com.outerspace.roomdbexperiment.DataLayer

import android.app.Application
import androidx.room.Room
import com.outerspace.roomdbexperiment.RoomDBExperimentApplication
import kotlinx.coroutines.flow.Flow

const val DB_NAME = "RoomDBExperiment"

class PersonsRepository {

    val context = RoomDBExperimentApplication.appContext()
    val db = Room.databaseBuilder(context, PersonDatabase::class.java, DB_NAME).build()
    val dao = db.personDao()

    // One-Shot and Observable Read Queries (Flow)
    // check: https://developer.android.com/training/data-storage/room/async-queries

    suspend fun addPerson(person: PersonEntity) = dao.insertPerson(person)

    suspend fun updatePerson(person: PersonEntity) = dao.updatePerson(person)

    suspend fun fetchPerson(id: Long): PersonEntity = dao.fetchPerson(id)

    fun fetchPersons(): Flow<List<PersonEntity>> = dao.fetchPersons()

    fun fetchPersons(occupation: String): Flow<List<PersonEntity>> = dao.fetchPersons(occupation)
}