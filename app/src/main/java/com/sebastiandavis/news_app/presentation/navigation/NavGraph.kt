package com.sebastiandavis.news_app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sebastiandavis.news_app.data.model.Article
import com.sebastiandavis.news_app.presentation.screens.ArticleDetailScreen
import com.sebastiandavis.news_app.presentation.screens.HomeScreen
import com.sebastiandavis.news_app.presentation.screens.PreferencesScreen
import com.sebastiandavis.news_app.presentation.screens.RecommendationsScreen
import com.sebastiandavis.news_app.presentation.screens.SavedArticlesScreen
import com.sebastiandavis.news_app.presentation.viewmodels.NewsViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val newsViewModel: NewsViewModel = viewModel()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = newsViewModel,
                onArticleClick = { article ->
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        key = "article",
                        value = article
                    )
                    navController.navigate(Screen.ArticleDetail.route)
                }
            )
        }
        
        composable(Screen.Recommendations.route) {
            RecommendationsScreen(
                viewModel = newsViewModel,
                onArticleClick = { article ->
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        key = "article",
                        value = article
                    )
                    navController.navigate(Screen.ArticleDetail.route)
                },
                onBackPressed = {
                    navController.navigateUp()
                }
            )
        }
        
        composable(Screen.Saved.route) {
            SavedArticlesScreen(
                viewModel = newsViewModel,
                onArticleClick = { article ->
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        key = "article",
                        value = article
                    )
                    navController.navigate(Screen.ArticleDetail.route)
                },
                onBackPressed = {
                    navController.navigateUp()
                }
            )
        }
        
        composable(Screen.Preferences.route) {
            PreferencesScreen(
                viewModel = newsViewModel,
                onBackPressed = {
                    navController.navigateUp()
                }
            )
        }
        
        composable(Screen.ArticleDetail.route) {
            val article = navController.previousBackStackEntry?.savedStateHandle?.get<Article>("article")
            article?.let {
                ArticleDetailScreen(
                    article = it,
                    viewModel = newsViewModel,
                    onBackPressed = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
} 