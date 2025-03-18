package com.sebastiandavis.news_app.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.sebastiandavis.news_app.presentation.viewmodels.NewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesScreen(
    viewModel: NewsViewModel,
    onBackPressed: () -> Unit
) {
    val userPreferences by viewModel.userPreferences.collectAsState()
    val availableCategories = viewModel.availableCategories
    
    // Initialize selected categories with the current user preferences
    val selectedCategories = remember {
        mutableStateListOf<String>().apply {
            userPreferences?.preferredCategories?.let { addAll(it) }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Preferences") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Select news categories you're interested in:",
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(availableCategories) { category ->
                    val isSelected = selectedCategories.contains(category)
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .toggleable(
                                value = isSelected,
                                role = Role.Checkbox,
                                onValueChange = {
                                    if (isSelected) {
                                        selectedCategories.remove(category)
                                    } else {
                                        selectedCategories.add(category)
                                    }
                                }
                            )
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = null
                        )
                        
                        Text(
                            text = category.replaceFirstChar { it.uppercase() },
                            modifier = Modifier.padding(start = 16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = {
                    viewModel.updateUserPreferences(
                        preferredCategories = selectedCategories.toList(),
                        preferredSources = userPreferences?.preferredSources ?: emptyList()
                    )
                    onBackPressed()
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null
                )
                Text(text = "Save Preferences")
            }
        }
    }
} 