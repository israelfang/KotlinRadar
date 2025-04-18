package com.kotlinradar.app.data.model

import com.google.gson.annotations.SerializedName

data class RepositoriesItemsEntity(
    @SerializedName("items")
    val items: List<RepositoryEntity>
)

data class RepositoryEntity(
    @SerializedName("name")
    val name: String,
    @SerializedName("stargazers_count")
    val stargazersCount: Int,
    @SerializedName("forks_count")
    val forksCount: Int,
    @SerializedName("owner")
    val owner: OwnerEntity
)

data class OwnerEntity(
    @SerializedName("login")
    val login: String,
    @SerializedName("avatar_url")
    val avatarUrl: String
)
