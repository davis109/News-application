package com.sebastiandavis.news_app.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sebastiandavis.news_app.data.model.Article
import com.sebastiandavis.news_app.presentation.components.ArticleItem
import com.sebastiandavis.news_app.presentation.viewmodels.NewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedArticlesScreen(
    viewModel: NewsViewModel,
    onArticleClick: (Article) -> Unit,
    onBackPressed: () -> Unit
) {
    val savedArticles by viewModel.savedArticles.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Saved Articles") },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (savedArticles.isEmpty()) {
                Text(
                    text = "No saved articles yet. Save articles to read them offline.",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(savedArticles) { article ->
                        ArticleItem(
                            article = article,
                            onArticleClick = { 
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