package com.example.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val role: String, // "user" or "jarvix"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "system_logs")
data class SystemLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val category: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Dao
interface JarvixDao {
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity)

    @Query("DELETE FROM chat_messages")
    suspend fun clearChat()

    @Query("SELECT * FROM system_logs ORDER BY timestamp DESC LIMIT 50")
    fun getRecentLogs(): Flow<List<SystemLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: SystemLogEntity)
}

@Database(entities = [ChatMessageEntity::class, SystemLogEntity::class], version = 1, exportSchema = false)
abstract class JarvixDatabase : RoomDatabase() {
    abstract fun jarvixDao(): JarvixDao

    companion object {
        @Volatile
        private var INSTANCE: JarvixDatabase? = null

        fun getDatabase(context: Context): JarvixDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    JarvixDatabase::class.java,
                    "jarvix_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
