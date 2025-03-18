package com.sebastiandavis.news_app.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Recommendations : Screen("recommendations")
    object Saved : Screen("saved")
    object Preferences : Screen("preferences")
    object ArticleDetail : Screen("article_detail")
} 