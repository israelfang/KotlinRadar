package com.kotlinradar.app.presentation

data class KotlinReposUiState(
    val repos: List<KotlinRepo> = emptyList(),
    val isLoading: Boolean = true
)

data class KotlinRepo(
    val name: String = "",
    val authorName: String = "",
    val image: String = "",
    val starGazersCount: Int = 0,
    val forksCount: Int = 0,
)
