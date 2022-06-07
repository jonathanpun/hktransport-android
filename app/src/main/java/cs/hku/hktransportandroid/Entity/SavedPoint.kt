package cs.hku.hktransportandroid.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SavedPoint(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    @ColumnInfo(name = "type")
    val type:SavedPointType,
    @ColumnInfo(name ="route")
    val route:String,
    @ColumnInfo(name="stop")
    val stop:String
){
    enum class SavedPointType{
         ROUTE_STOP
    }
}