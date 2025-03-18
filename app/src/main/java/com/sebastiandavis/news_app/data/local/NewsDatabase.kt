package com.sebastiandavis.news_app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sebastiandavis.news_app.data.model.Article
import com.sebastiandavis.news_app.data.model.UserPreferences

@Database(
    entities = [Article::class, UserPreferences::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
    abstract fun userPreferencesDao(): UserPreferencesDao
    
    companion object {
        @Volatile
        private var INSTANCE: NewsDatabase? = null
        
        fun getDatabase(context: Context): NewsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NewsDatabase::class.java,
                    "news_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
} 