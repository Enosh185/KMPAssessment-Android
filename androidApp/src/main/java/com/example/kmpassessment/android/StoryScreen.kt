package com.example.kmpassessment.android

import androidx.compose.ui.Alignment
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.kmpassessment.Story
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material3.MaterialTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color

@Composable
fun StoryScreen(vm: MainViewModel) {
    val stories by vm.filteredStories.collectAsState(initial = emptyList())
    val query by vm.query.collectAsState()
    val isRefreshing by vm.isRefreshing.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Show Snackbar if flag is true
    LaunchedEffect(vm.showOfflineSnackbar) {
        vm.showOfflineSnackbar.collect { show ->
            if (show) {
                snackbarHostState.showSnackbar("Offline mode: showing cached stories.")
                vm.clearOfflineSnackbar()
            }
        }
    }

    Surface(color = MaterialTheme.colorScheme.background) {
        Scaffold(
            contentWindowInsets = WindowInsets.systemBars,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                OutlinedTextField(
                    value = query,
                    onValueChange = vm::onQueryChanged,
                    placeholder = { Text("Filter by headline") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        ) { innerPadding ->
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = { vm.refresh() },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (isRefreshing && stories.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        items(stories) { story -> StoryCard(story = story, query = query) }
                    }
                }
            }
        }
    }
}

@Composable
private fun StoryCard(story: Story, query: String) {
    val highlightStyle = SpanStyle(
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold
    )

    val annotatedHeadline = remember(story.headline to query) {
        buildAnnotatedString {
            val startIndex = story.headline.indexOf(query, ignoreCase = true)
            if (startIndex == -1 || query.isBlank()) {
                append(story.headline)
            } else {
                val endIndex = startIndex + query.length
                append(story.headline.substring(0, startIndex))
                withStyle(highlightStyle) {
                    append(story.headline.substring(startIndex, endIndex))
                }
                append(story.headline.substring(endIndex))
            }
        }
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = story.imageUrl,
                contentDescription = story.headline,
                modifier = Modifier
                    .size(72.dp)
                    .padding(end = 8.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = annotatedHeadline,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                val date = remember(story.published) {
                    try {
                        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                        sdf.format(Date(story.published))
                    } catch (e: Exception) {
                        "Unknown Date"
                    }
                }

                Text(
                    text = date,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}