package com.sebastiandavis.news_app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sebastiandavis.news_app.data.model.UserPreferences
import kotlinx.coroutines.flow.Flow

@Dao
interface UserPreferencesDao {
    @Query("SELECT * FROM user_preferences WHERE userId = :userId")
    fun getUserPreferences(userId: String): Flow<UserPreferences?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserPreferences(userPreferences: UserPreferences)
    
    @Update
    suspend fun updateUserPreferences(userPreferences: UserPreferences)
} 