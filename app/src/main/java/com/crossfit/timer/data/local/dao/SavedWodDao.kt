package com.crossfit.timer.data.local.dao

import androidx.room.*
import com.crossfit.timer.data.local.entity.SavedWod
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedWodDao {

    @Query("SELECT * FROM saved_wods ORDER BY lastUsed DESC, createdAt DESC")
    fun getAllSavedWods(): Flow<List<SavedWod>>

    @Query("SELECT * FROM saved_wods WHERE id = :id")
    suspend fun getById(id: Long): SavedWod?

    @Query("SELECT * FROM saved_wods WHERE mode = :mode ORDER BY lastUsed DESC, createdAt DESC")
    fun getWodsByMode(mode: String): Flow<List<SavedWod>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(wod: SavedWod): Long

    @Update
    suspend fun update(wod: SavedWod)

    @Delete
    suspend fun delete(wod: SavedWod)

    @Query("UPDATE saved_wods SET lastUsed = :timestamp WHERE id = :id")
    suspend fun updateLastUsed(id: Long, timestamp: Long)

    @Query("DELETE FROM saved_wods WHERE id = :id")
    suspend fun deleteById(id: Long)
}
