package com.crossfit.timer.di

import android.content.Context
import androidx.room.Room
import com.crossfit.timer.data.local.dao.SavedWodDao
import com.crossfit.timer.data.local.dao.WorkoutHistoryDao
import com.crossfit.timer.data.local.database.CrossFitDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideCrossFitDatabase(
        @ApplicationContext context: Context
    ): CrossFitDatabase {
        return Room.databaseBuilder(
            context,
            CrossFitDatabase::class.java,
            CrossFitDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideWorkoutHistoryDao(database: CrossFitDatabase): WorkoutHistoryDao {
        return database.workoutHistoryDao()
    }

    @Provides
    @Singleton
    fun provideSavedWodDao(database: CrossFitDatabase): SavedWodDao {
        return database.savedWodDao()
    }
}
