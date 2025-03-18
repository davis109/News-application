package com.sebastiandavis.news_app.utils

import com.sebastiandavis.news_app.data.model.Article
import com.sebastiandavis.news_app.data.model.UserPreferences

/**
 * A simple recommendation engine that recommends articles based on user preferences
 */
class RecommendationEngine {
    
    /**
     * Recommends articles based on user preferences and reading history
     * 
     * @param articles All available articles
     * @param userPreferences User preferences containing preferred categories and sources
     * @param readArticles Articles that have been read by the user
     * @return List of recommended articles sorted by relevance
     */
    fun getRecommendedArticles(
        articles: List<Article>,
        userPreferences: UserPreferences?,
        readArticles: List<Article>
    ): List<Article> {
        if (userPreferences == null) {
            return articles.sortedByDescending { it.publishedAt }
        }
        
        // Calculate a score for each article based on user preferences
        return articles.sortedByDescending { article ->
            calculateRecommendationScore(article, userPreferences, readArticles)
        }
    }
    
    /**
     * Calculate a score for an article based on how well it matches user preferences
     */
    private fun calculateRecommendationScore(
        article: Article,
        userPreferences: UserPreferences,
        readArticles: List<Article>
    ): Double {
        var score = 0.0
        
        // Preferred categories boost score
        if (article.category in userPreferences.preferredCategories) {
            score += 10.0
        }
        
        // Preferred sources boost score
        if (article.source in userPreferences.preferredSources) {
            score += 5.0
        }
        
        // Recent articles get higher scores
        val currentTimeMillis = System.currentTimeMillis()
        val articleAgeHours = (currentTimeMillis - article.publishedAt.time) / (1000 * 60 * 60)
        score += (24.0 / (articleAgeHours + 1.0)) // Score decreases with age
        
        // Previously read articles from the same source get a boost
        val sourceReadCount = readArticles.count { it.source == article.source }
        score += sourceReadCount * 0.5
        
        // Previously read articles from the same category get a boost
        val categoryReadCount = readArticles.count { it.category == article.category }
        score += categoryReadCount * 0.5
        
        // Avoid recommending articles that are already read
        if (readArticles.any { it.url == article.url }) {
            score -= 50.0 // Large penalty for already read articles
        }
        
        return score
    }
} 