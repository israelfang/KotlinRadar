package com.kotlinradar.app.domain.repository

import com.kotlinradar.app.core.Result
import com.kotlinradar.app.domain.model.RepositoriesItems

interface GitHubRepository {
    suspend fun getKotlinRepos(
        query: String,
        sort: String,
        perPage: Int,
        page: Int
    ): Result<RepositoriesItems>
}
