package com.kotlinradar.app.data.remote

import com.google.gson.annotations.SerializedName
import com.kotlinradar.app.domain.model.Repo

/**
 * Wrapper for the GitHub search response.
 */

// DTO for the API response
data class RepoResponse(
    @SerializedName("items")
    val items: List<Repo>
)