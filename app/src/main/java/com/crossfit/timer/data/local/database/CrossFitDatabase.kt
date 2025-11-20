package com.crossfit.timer.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.crossfit.timer.data.local.dao.SavedWodDao
import com.crossfit.timer.data.local.dao.WorkoutHistoryDao
import com.crossfit.timer.data.local.entity.SavedWod
import com.crossfit.timer.data.local.entity.WorkoutHistory

@Database(
    entities = [
        WorkoutHistory::class,
        SavedWod::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CrossFitDatabase : RoomDatabase() {
    abstract fun workoutHistoryDao(): WorkoutHistoryDao
    abstract fun savedWodDao(): SavedWodDao

    companion object {
        const val DATABASE_NAME = "crossfit_timer_db"
    }
}
