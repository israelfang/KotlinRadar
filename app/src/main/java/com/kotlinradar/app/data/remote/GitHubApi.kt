package com.kotlinradar.app.data.remote

import com.kotlinradar.app.data.model.RepositoriesItemsEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {
    @GET("search/repositories")
    suspend fun searchGithubRepositories(
        @Query("q") query: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): RepositoriesItemsEntity
}
