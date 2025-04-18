package com.kotlinradar.app.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Provides a singleton Retrofit instance and the GithubApi interface.
 */
object RetrofitInstance {

    private const val BASE_URL = "https://api.github.com/"

    val githubApi: GithubApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GithubApi::class.java)
    }
}
