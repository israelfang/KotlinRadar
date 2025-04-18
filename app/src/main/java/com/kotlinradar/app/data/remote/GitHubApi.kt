package com.kotlinradar.app.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface for GitHub repository search.
 */

interface GithubApi {

    /**
     * Fetches repositories written in Kotlin, sorted by star count.
     *
     * @param query   Search query (defaults to "language:kotlin").
     * @param sort    Sort criteria (defaults to "stars").
     * @param page    Page number to fetch (starts at 1).
     * @return        [RepoResponse] with a list of [Repo].
     */
    @GET("search/repositories")
    suspend fun getRepositories(
        @Query("q") query: String = "language:kotlin",
        @Query("sort") sort: String = "stars",
        @Query("page") page: Int
    ): Response<RepoResponse>
}