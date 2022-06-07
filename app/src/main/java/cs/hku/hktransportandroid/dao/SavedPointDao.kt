package cs.hku.hktransportandroid.dao

import androidx.room.Dao
import androidx.room.Query
import cs.hku.hktransportandroid.Entity.SavedPoint
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedPointDao {
    @Query("SELECT * FROM SavedPoint")
    fun getUserSavedPoints(): Flow<SavedPoint>
}