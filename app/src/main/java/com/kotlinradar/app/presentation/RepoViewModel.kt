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

/**
 * ViewModel responsible for fetching and exposing a list of GitHub repositories.
 */
class RepoViewModel : ViewModel() {

    private val repository = RepoRepository(
        api = RetrofitInstance.githubApi
    )

    private val _repos = MutableStateFlow(KotlinReposUiState())
    val repos: StateFlow<KotlinReposUiState> = _repos

    init {
        fetchRepos()
    }

    /**
     * Fetch repositories for a given page and update UI state.
     *
     * @param page Page number to load.
     */
    private fun fetchRepos(page: Int = 1) {
        viewModelScope.launch {
            _repos.update {
                it.copy(
                    isLoading = true
                )
            }

            try {
                val response = repository.getRepos(page)
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
                            isLoading = false
                        )
                    }
                } else {
                    // TODO: handle error state (e.g., expose error StateFlow)
                }
            } catch (e: Exception) {
                // TODO: handle error state (e.g., expose error StateFlow)
            }
        }
    }

    fun refreshRepos() {
        fetchRepos()
    }
}
