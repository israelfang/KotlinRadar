package com.kotlinradar.app.data.repository

import com.kotlinradar.app.data.remote.GithubApi
import com.kotlinradar.app.data.remote.RepoResponse
import com.kotlinradar.app.domain.model.Repo
import retrofit2.Response

/**
 * Repository handling data operations for GitHub repositories.
 */
class RepoRepository(
    private val api: GithubApi
) {
    /**
     * Fetch a list of Kotlin repositories sorted by stars for the given page.
     *
     * @param page Page number to load (defaults to 1).
     * @return     List of [Repo] items.
     */
    suspend fun getRepos(
        page: Int = 1,
        perPage: Int = 15
    ): Response<RepoResponse> =
        api.getRepositories(
            page = page,
            perPage = perPage
        )
}
