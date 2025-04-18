package com.kotlinradar.app.domain.model

data class Repository(
    val name: String,
    val stargazersCount: Int,
    val forksCount: Int,
    val owner: Owner
)
