package com.kotlinradar.app.presentation

data class KotlinReposUiState(
    val repos: List<KotlinRepo> = emptyList(),
    val isScreenLoading: Boolean = false,
    val isNextPageLoading: Boolean = false,
    val isInitializeFailure: Boolean = false,
    val isNextPageFailure: Boolean = false
)

data class KotlinRepo(
    val name: String = "",
    val authorName: String = "",
    val image: String = "",
    val starGazersCount: Int = 0,
    val forksCount: Int = 0,
)
