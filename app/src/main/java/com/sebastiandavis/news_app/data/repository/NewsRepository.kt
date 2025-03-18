package com.sebastiandavis.news_app.data.repository

import com.sebastiandavis.news_app.data.api.RetrofitClient
import com.sebastiandavis.news_app.data.local.ArticleDao
import com.sebastiandavis.news_app.data.model.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class NewsRepository(private val articleDao: ArticleDao) {
    private val newsApiService = RetrofitClient.newsApiService
    private val apiKey = RetrofitClient.apiKey
    
    // Network operations
    suspend fun fetchTopHeadlines(category: String? = null): Result<List<Article>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = newsApiService.getTopHeadlines(
                    category = category,
                    apiKey = apiKey
                )
                if (response.status == "ok") {
                    val articles = response.articles.map { it.toArticle(category ?: "general") }
                    // Cache the fetched articles
                    articleDao.insertArticles(articles)
                    Result.success(articles)
                } else {
                    Result.failure(Exception("API error: ${response.status}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    suspend fun searchNews(query: String): Result<List<Article>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = newsApiService.searchNews(
                    query = query,
                    apiKey = apiKey
                )
                if (response.status == "ok") {
                    val articles = response.articles.map { it.toArticle("search") }
                    Result.success(articles)
                } else {
                    Result.failure(Exception("API error: ${response.status}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    // Local database operations
    fun getAllArticles(): Flow<List<Article>> {
        return articleDao.getAllArticles()
    }
    
    fun getArticlesByCategory(category: String): Flow<List<Article>> {
        return articleDao.getArticlesByCategory(category)
    }
    
    fun getSavedArticles(): Flow<List<Article>> {
        return articleDao.getSavedArticles()
    }
    
    suspend fun toggleSaveArticle(article: Article, isSaved: Boolean) {
        articleDao.updateSavedStatus(article.url, isSaved)
    }
    
    suspend fun markArticleAsRead(article: Article) {
        articleDao.updateReadStatus(article.url, true)
    }
    
    suspend fun cacheArticles(articles: List<Article>) {
        articleDao.insertArticles(articles)
    }
} 