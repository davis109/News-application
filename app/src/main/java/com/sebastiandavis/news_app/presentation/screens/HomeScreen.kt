package com.sebastiandavis.news_app.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sebastiandavis.news_app.data.model.Article
import com.sebastiandavis.news_app.presentation.components.ArticleItem
import com.sebastiandavis.news_app.presentation.viewmodels.NewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: NewsViewModel,
    onArticleClick: (Article) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    
    val articles by viewModel.allArticles.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val userPreferences by viewModel.userPreferences.collectAsState()
    
    var selectedCategoryIndex by remember { mutableStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    
    // Show error message if any
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("News App") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                label = { Text("Search news") },
                trailingIcon = {
                    IconButton(onClick = { viewModel.searchNews(searchQuery) }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                }
            )
            
            // Category tabs
            val categories = viewModel.availableCategories
            val selectedIndex = if (selectedCategoryIndex < categories.size) 
                selectedCategoryIndex else 0
            
            ScrollableTabRow(
                selectedTabIndex = selectedIndex,
                edgePadding = 16.dp
            ) {
                categories.forEachIndexed { index, category ->
                    Tab(
                        selected = selectedIndex == index,
                        onClick = { 
                            selectedCategoryIndex = index
                            // Fetch news for selected category
                            viewModel.refreshNews()
                        },
                        text = { 
                            Text(
                                text = category.replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.bodyMedium
                            ) 
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // News list
            Box(modifier = Modifier.fillMaxSize()) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else if (articles.isEmpty()) {
                    Text(
                        text = "No articles found",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                } else {
                    val filteredArticles = if (selectedCategoryIndex == 0) {
                        articles
                    } else {
                        articles.filter { it.category == categories[selectedCategoryIndex] }
                    }
                    
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(filteredArticles) { article ->
                            ArticleItem(
                                article = article,
                                onArticleClick = {
                                    viewModel.markArticleAsRead(it)
                                    onArticleClick(it)
                                },
                                onBookmarkClick = {
                                    viewModel.toggleSaveArticle(it)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
} 