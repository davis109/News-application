package com.sebastiandavis.news_app.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sebastiandavis.news_app.data.local.NewsDatabase
import com.sebastiandavis.news_app.data.model.Article
import com.sebastiandavis.news_app.data.model.UserPreferences
import com.sebastiandavis.news_app.data.repository.NewsRepository
import com.sebastiandavis.news_app.data.repository.UserPreferencesRepository
import com.sebastiandavis.news_app.utils.RecommendationEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NewsViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database = NewsDatabase.getDatabase(application)
    private val newsRepository = NewsRepository(database.articleDao())
    private val userPreferencesRepository = UserPreferencesRepository(database.userPreferencesDao())
    private val recommendationEngine = RecommendationEngine()
    
    // Current user ID - in a real app, this would come from the authentication system
    private val currentUserId = "user_1"
    
    // UI States
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage
    
    // News data states
    val allArticles = newsRepository.getAllArticles()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    
    val savedArticles = newsRepository.getSavedArticles()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    
    // Search results
    private val _searchResults = MutableStateFlow<List<Article>>(emptyList())
    val searchResults: StateFlow<List<Article>> = _searchResults
    
    // Search query
    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery: StateFlow<String> = _searchQuery
    
    // Search state
    sealed class SearchState {
        object Idle : SearchState()
        object Searching : SearchState()
        data class Results(val articles: List<Article>) : SearchState()
        data class Error(val message: String) : SearchState()
    }
    
    private val _searchState = MutableStateFlow<SearchState>(SearchState.Idle)
    val searchState: StateFlow<SearchState> = _searchState
    
    // User preferences
    val userPreferences = userPreferencesRepository.getUserPreferences(currentUserId)
        .stateIn(viewModelScope, SharingStarted.Lazily, null)
    
    // Recommended articles based on user preferences
    val recommendedArticles = combine(
        allArticles,
        userPreferences
    ) { articles, preferences ->
        recommendationEngine.getRecommendedArticles(
            articles,
            preferences,
            articles.filter { it.isRead }
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    
    // Categories
    val availableCategories = listOf(
        "business", "entertainment", "general", "health", 
        "science", "sports", "technology"
    )
    
    // Initialize with default preferences if needed
    init {
        viewModelScope.launch {
            val prefs = userPreferences.first()
            if (prefs == null) {
                createDefaultUserPreferences()
            }
            refreshNews()
        }
    }
    
    // Fetch news from API
    fun refreshNews() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // First fetch general headlines
                val headlinesResult = newsRepository.fetchTopHeadlines()
                if (headlinesResult.isFailure) {
                    _errorMessage.value = "Failed to load headlines: ${headlinesResult.exceptionOrNull()?.message ?: "Unknown error"}"
                    _isLoading.value = false
                    return@launch
                }
                
                // Then fetch headlines for each preferred category
                userPreferences.first()?.preferredCategories?.forEach { category ->
                    newsRepository.fetchTopHeadlines(category)
                }
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load news: ${e.message ?: "Unknown error"}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    // Search news
    fun searchNews(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                _searchResults.value = emptyList()
                _searchQuery.value = ""
                _searchState.value = SearchState.Idle
                return@launch
            }
            
            _isLoading.value = true
            _searchQuery.value = query
            _searchState.value = SearchState.Searching
            
            try {
                val result = newsRepository.searchNews(query)
                if (result.isFailure) {
                    val errorMsg = "Search failed: ${result.exceptionOrNull()?.message ?: "Unknown error"}"
                    _errorMessage.value = errorMsg
                    _searchResults.value = emptyList()
                    _searchState.value = SearchState.Error(errorMsg)
                } else {
                    _errorMessage.value = null
                    val articles = result.getOrNull() ?: emptyList()
                    _searchResults.value = articles
                    _searchState.value = SearchState.Results(articles)
                    
                    // Cache search results locally
                    if (articles.isNotEmpty()) {
                        newsRepository.cacheArticles(articles)
                    }
                }
            } catch (e: Exception) {
                val errorMsg = "Search failed: ${e.message ?: "Unknown error"}"
                _errorMessage.value = errorMsg
                _searchResults.value = emptyList()
                _searchState.value = SearchState.Error(errorMsg)
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    // Save article for offline reading
    fun toggleSaveArticle(article: Article) {
        viewModelScope.launch {
            newsRepository.toggleSaveArticle(article, !article.isSaved)
        }
    }
    
    // Mark article as read
    fun markArticleAsRead(article: Article) {
        viewModelScope.launch {
            newsRepository.markArticleAsRead(article)
        }
    }
    
    // Update user preferences
    fun updateUserPreferences(
        preferredCategories: List<String>,
        preferredSources: List<String>
    ) {
        viewModelScope.launch {
            val prefs = userPreferences.first()
            if (prefs != null) {
                val updatedPrefs = prefs.copy(
                    preferredCategories = preferredCategories,
                    preferredSources = preferredSources
                )
                userPreferencesRepository.updateUserPreferences(updatedPrefs)
            }
        }
    }
    
    // Create default user preferences
    private suspend fun createDefaultUserPreferences() {
        val defaultPrefs = UserPreferences(
            userId = currentUserId,
            preferredCategories = listOf("general", "technology"),
            preferredSources = emptyList()
        )
        userPreferencesRepository.saveUserPreferences(defaultPrefs)
    }
} 