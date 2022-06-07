package cs.hku.hktransportandroid.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import cs.hku.hktransportandroid.dao.Database
import cs.hku.hktransportandroid.dao.SavedPointDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Module

@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext applicationContext: Context):Database{
        return Room.databaseBuilder(applicationContext,Database::class.java,"database").build()
    }
    @Provides
    @Singleton
    fun provideSavedPointDao(database: Database): SavedPointDao {
        return database.savedPointDao()
    }
}