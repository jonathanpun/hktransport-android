package cs.hku.hktransportandroid.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import cs.hku.hktransportandroid.Entity.SavedPoint

@Database(entities = [SavedPoint::class], version = 2)
abstract class Database:RoomDatabase() {
    abstract fun savedPointDao():SavedPointDao
}