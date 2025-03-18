package com.sebastiandavis.news_app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sebastiandavis.news_app.data.model.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles ORDER BY publishedAt DESC")
    fun getAllArticles(): Flow<List<Article>>
    
    @Query("SELECT * FROM articles WHERE category = :category ORDER BY publishedAt DESC")
    fun getArticlesByCategory(category: String): Flow<List<Article>>
    
    @Query("SELECT * FROM articles WHERE isSaved = 1 ORDER BY publishedAt DESC")
    fun getSavedArticles(): Flow<List<Article>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<Article>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article)
    
    @Update
    suspend fun updateArticle(article: Article)
    
    @Delete
    suspend fun deleteArticle(article: Article)
    
    @Query("UPDATE articles SET isSaved = :isSaved WHERE url = :url")
    suspend fun updateSavedStatus(url: String, isSaved: Boolean)
    
    @Query("UPDATE articles SET isRead = :isRead WHERE url = :url")
    suspend fun updateReadStatus(url: String, isRead: Boolean)
} 