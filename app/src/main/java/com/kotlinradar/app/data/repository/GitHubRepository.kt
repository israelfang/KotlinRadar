package com.kotlinradar.app.data.repository

import com.kotlinradar.app.core.Result
import com.kotlinradar.app.data.mapper.toDomain
import com.kotlinradar.app.data.remote.GithubApi
import com.kotlinradar.app.domain.model.RepositoriesItems
import com.kotlinradar.app.domain.repository.GitHubRepository

class GitHubRepository(
    private val api: GithubApi
) : GitHubRepository {
    override suspend fun getKotlinRepos(
        query: String,
        sort: String,
        perPage: Int,
        page: Int
    ): Result<RepositoriesItems> {
        return try {
            val response = api.searchGithubRepositories(
                query = query,
                sort = sort,
                perPage = perPage,
                page = page
            )
            Result.Success(response.toDomain())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
