package com.kotlinradar.app.presentation

import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kotlinradar.app.R
import com.kotlinradar.app.presentation.ui.theme.kotlinCyan
import com.kotlinradar.app.presentation.ui.theme.kotlinOrange
import com.kotlinradar.app.presentation.ui.theme.kotlinPink
import com.kotlinradar.app.presentation.ui.theme.kotlinPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoListScreen(
    viewModel: RepoViewModel = viewModel()
) {
    val reposState = viewModel.repos.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ), title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier
                                .padding(end = 6.dp)
                                .size(45.dp),
                            painter = painterResource(id = R.drawable.github_logo),
                            contentDescription = null
                        )

                        val kotlinGradientBrush = Brush.horizontalGradient(
                            colors = listOf(
                                kotlinPurple,
                                kotlinPink,
                                kotlinOrange,
                                kotlinCyan
                            )
                        )

                        Text(
                            text = "Kotlin",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                brush = kotlinGradientBrush,
                                fontSize = 25.sp
                            )
                        )
                        Text(
                            text = "Radar",
                            style = TextStyle(
                                fontSize = 25.sp
                            )
                        )

                    }
                })
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                onClick = { viewModel.refreshRepos() }
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Refresh")
            }
        }

    ) { innerPadding ->

        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            when {
                reposState.value.isInitializeFailure -> {
                    val activity = LocalActivity.current
                    AlertDialog(
                        onDismissRequest = {},
                        title = { Text(text = "oops!") },
                        text = { Text(text = "Something went wrong") },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    activity?.finish()
                                }
                            ) {
                                Text(text = "Exit")
                            }
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    viewModel.refreshRepos()
                                }
                            ) {
                                Text(text = "Reload")
                            }
                        }
                    )
                }

                reposState.value.isScreenLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                else -> {
                    val listState = rememberLazyListState()
                    val context = LocalContext.current
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .testTag("repoList")
                            .fillMaxSize()
                    ) {
                        items(reposState.value.repos) { repo ->
                            RepoCard(
                                repo = repo,
                                modifier = Modifier.testTag("repoCard")
                            )
                        }

                        if (reposState.value.isNextPageLoading) {
                            item {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    LinearProgressIndicator(
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }

                        if (reposState.value.isNextPageFailure) {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                        }
                    }

                    LaunchedEffect(listState) {
                        snapshotFlow {
                            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                        }.collect { lastVisibleItem ->
                            if (lastVisibleItem == reposState.value.repos.lastIndex) {
                                viewModel.loadNextPage()
                            }
                        }
                    }
                }
            }
        }
    }
}
