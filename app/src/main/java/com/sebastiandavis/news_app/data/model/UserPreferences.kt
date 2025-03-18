package com.sebastiandavis.news_app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_preferences")
data class UserPreferences(
    @PrimaryKey
    val userId: String,
    val preferredCategories: List<String>,
    val preferredSources: List<String>,
    val textSize: Int = 16,
    val darkMode: Boolean = false
) 