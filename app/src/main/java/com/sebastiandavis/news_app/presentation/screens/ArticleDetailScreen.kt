package com.sebastiandavis.news_app.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sebastiandavis.news_app.data.model.Article
import com.sebastiandavis.news_app.presentation.viewmodels.NewsViewModel
import com.sebastiandavis.news_app.utils.TextToSpeechManager
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailScreen(
    article: Article,
    viewModel: NewsViewModel,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    var isSpeaking by remember { mutableStateOf(false) }
    val textToSpeechManager = remember { TextToSpeechManager(context) }
    
    // Clean up the TextToSpeechManager when the composable is disposed
    DisposableEffect(key1 = textToSpeechManager) {
        onDispose {
            textToSpeechManager.shutdown()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Article") },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // Bookmark button
                    IconButton(onClick = { viewModel.toggleSaveArticle(article) }) {
                        Icon(
                            imageVector = if (article.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = if (article.isSaved) "Remove from saved" else "Save article"
                        )
                    }
                    
                    // Share button
                    IconButton(onClick = { /* Share functionality */ }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share article"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (isSpeaking) {
                        textToSpeechManager.stop()
                    } else {
                        val textToSpeak = article.content ?: article.description ?: article.title
                        textToSpeechManager.speak(textToSpeak)
                    }
                    isSpeaking = !isSpeaking
                }
            ) {
                Icon(
                    imageVector = if (isSpeaking) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                    contentDescription = if (isSpeaking) "Stop reading" else "Read aloud"
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Article title
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Source and date
                Text(
                    text = "${article.source} • ${formatDate(article.publishedAt)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Article image
                article.urlToImage?.let { imageUrl ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "Article image",
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                // Article description
                article.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                // Article content
                article.content?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Author information
                article.author?.let {
                    Text(
                        text = "By $it",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.height(80.dp)) // Space for FAB
            }
        }
    }
}

private fun formatDate(date: java.util.Date): String {
    val formatter = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
    return formatter.format(date)
}