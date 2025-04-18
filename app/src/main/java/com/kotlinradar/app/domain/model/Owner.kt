package com.kotlinradar.app.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Represents the owner of a GitHub repository.
 */

// Model for repository owner
data class Owner(
    @SerializedName("login")
    val login: String,
    @SerializedName("avatar_url")
    val avatarUrl: String
)