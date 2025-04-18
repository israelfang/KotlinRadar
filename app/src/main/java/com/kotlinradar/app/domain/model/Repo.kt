package com.kotlinradar.app.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Represents a GitHub repository returned by the search API.
 */

// Model for a GitHub repository
data class Repo(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("stargazers_count")
    val stargazersCount: Int,
    @SerializedName("forks_count")
    val forksCount: Int,
    @SerializedName("owner")
    val owner: Owner
)