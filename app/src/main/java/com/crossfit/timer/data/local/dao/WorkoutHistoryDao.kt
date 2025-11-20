package com.crossfit.timer.data.local.dao

import androidx.room.*
import com.crossfit.timer.data.local.entity.WorkoutHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutHistoryDao {

    @Query("SELECT * FROM workout_history ORDER BY date DESC")
    fun getAllHistory(): Flow<List<WorkoutHistory>>

    @Query("SELECT * FROM workout_history WHERE mode = :mode ORDER BY date DESC")
    fun getHistoryByMode(mode: String): Flow<List<WorkoutHistory>>

    @Query("SELECT * FROM workout_history WHERE id = :id")
    suspend fun getById(id: Long): WorkoutHistory?

    @Query("SELECT * FROM workout_history WHERE date >= :startDate AND date <= :endDate ORDER BY date DESC")
    fun getHistoryByDateRange(startDate: Long, endDate: Long): Flow<List<WorkoutHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workout: WorkoutHistory): Long

    @Update
    suspend fun update(workout: WorkoutHistory)

    @Delete
    suspend fun delete(workout: WorkoutHistory)

    @Query("DELETE FROM workout_history WHERE id = :id")
    suspend fun deleteById(id: Long)
}
