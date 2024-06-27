package com.outerspace.roomdbexperiment.DataLayer

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {
    @Insert
    suspend fun insertPerson(personEntity: PersonEntity)

    @Update(entity= PersonEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePerson(person: PersonEntity)

    @Query("select * from persons where id=:id")
    suspend fun fetchPerson(id: Long): PersonEntity

    @Query("select * from persons")
    fun fetchPersons(): Flow<List<PersonEntity>>

    @Query("select * from persons where occupation=:occupation")
    fun fetchPersons(occupation: String): Flow<List<PersonEntity>>
}

@Database(entities = [PersonEntity::class], version = 1)
abstract class PersonDatabase: RoomDatabase() {
    abstract fun personDao(): PersonDao
}



