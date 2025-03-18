package com.sebastiandavis.news_app.data.repository

import com.sebastiandavis.news_app.data.local.UserPreferencesDao
import com.sebastiandavis.news_app.data.model.UserPreferences
import kotlinx.coroutines.flow.Flow

class UserPreferencesRepository(private val userPreferencesDao: UserPreferencesDao) {
    
    fun getUserPreferences(userId: String): Flow<UserPreferences?> {
        return userPreferencesDao.getUserPreferences(userId)
    }
    
    suspend fun saveUserPreferences(userPreferences: UserPreferences) {
        userPreferencesDao.insertUserPreferences(userPreferences)
    }
    
    suspend fun updateUserPreferences(userPreferences: UserPreferences) {
        userPreferencesDao.updateUserPreferences(userPreferences)
    }
} 