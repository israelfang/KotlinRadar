package com.kotlinradar.app.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinradar.app.data.remote.RetrofitInstance
import com.kotlinradar.app.data.repository.RepoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RepoViewModel : ViewModel() {
    private val repository = RepoRepository(
        api = RetrofitInstance.githubApi
    )

    private val _repos = MutableStateFlow(KotlinReposUiState())
    val repos: StateFlow<KotlinReposUiState> = _repos

    private val firstPageToBeLoaded = 1
    private var nextPageToBeLoaded = 1
    private var endReached = false

    init {
        updateLoadingState(true)
        fetchInitialRepos()
    }

    private fun fetchInitialRepos() {
        viewModelScope.launch {
            updateLoadingState(true)

            try {
                val response = repository.getRepos(firstPageToBeLoaded)
                Log.i("RepoViewModel", "response: ${response.body()}")

                val items = response.body()?.items

                if (response.isSuccessful) {
                    _repos.update { currentState ->
                        currentState.copy(
                            repos = items?.map {
                                KotlinRepo(
                                    name = it.name,
                                    authorName = it.owner.login,
                                    image = it.owner.avatarUrl,
                                    starGazersCount = it.stargazersCount,
                                    forksCount = it.forksCount
                                )
                            } ?: emptyList(),
                            isScreenLoading = false)
                    }
                    nextPageToBeLoaded++
                } else {
                    _repos.update {
                        it.copy(
                            isError = true
                        )
                    }
                }
            } catch (e: Exception) {
                _repos.update {
                    it.copy(
                        isError = true
                    )
                }
            }
        }
    }

    private fun updateLoadingState(isLoading: Boolean) {
        _repos.update {
            it.copy(
                isScreenLoading = isLoading
            )
        }
    }

    fun refreshRepos() {
        nextPageToBeLoaded = 1
        endReached = false
        _repos.update {
            it.copy(
                isError = false
            )
        }
        fetchInitialRepos()
    }

    fun loadNextPage() {
        if (_repos.value.isNextPageLoading || endReached) return

        _repos.update {
            it.copy(
                isNextPageLoading = true
            )
        }

        viewModelScope.launch {
            try {
                val response = repository.getRepos(nextPageToBeLoaded)
                if (response.isSuccessful) {
                    Log.i("RepoViewModel", "loadNextPageResponse: ${response.body()}")
                    val newKotlinRepos = response.body()?.items?.map {
                        KotlinRepo(
                            name = it.name,
                            authorName = it.owner.login,
                            image = it.owner.avatarUrl,
                            starGazersCount = it.stargazersCount,
                            forksCount = it.forksCount
                        )
                    }

                    if (newKotlinRepos.isNullOrEmpty()) {
                        endReached = true
                        _repos.update { currentState ->
                            currentState.copy(
                                isNextPageLoading = false
                            )
                        }

                    } else {
                        val allKotlinRepos = _repos.value.repos + newKotlinRepos

                        _repos.update { currentState ->
                            currentState.copy(
                                repos = allKotlinRepos, isNextPageLoading = false
                            )
                        }

                        nextPageToBeLoaded++
                    }

                } else {
                    Log.i("RepoViewModel", "loadNextPageResponse: $response")
                    _repos.update {
                        it.copy(
                            isError = true
                        )
                    }
                }
            } catch (e: Exception) {
                Log.i("RepoViewModel", "loadNextPageResponse: ${e.message + e.stackTrace + e.cause}")
                _repos.update {
                    it.copy(
                        isError = true
                    )
                }
            }
        }
    }
}
