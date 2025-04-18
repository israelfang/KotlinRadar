package com.kotlinradar.app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinradar.app.core.Result.Error
import com.kotlinradar.app.core.Result.Success
import com.kotlinradar.app.data.remote.RetrofitInstance
import com.kotlinradar.app.data.repository.GitHubRepository
import com.kotlinradar.app.domain.usecase.GetKotlinRepositoriesUseCase
import com.kotlinradar.app.domain.usecase.GetKotlinRepositoriesUseCaseImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RepoViewModel(
    private val getKotlinRepositoriesUseCase: GetKotlinRepositoriesUseCase = GetKotlinRepositoriesUseCaseImpl(
        repository = GitHubRepository(
            api = RetrofitInstance.githubApi
        )
    )
) : ViewModel() {

    private val _repos = MutableStateFlow(KotlinReposUiState())
    val repos: StateFlow<KotlinReposUiState> = _repos

    private val firstPageToBeLoaded = 1
    private var nextPageToBeLoaded = 1
    private var endReached = false

    init {
        fetchInitialRepos()
    }

    private fun fetchInitialRepos() {
        viewModelScope.launch {
            updateToLoadingState()

            when (val result = getKotlinRepositoriesUseCase(firstPageToBeLoaded)) {
                is Success -> {
                    val items = result.data.items
                    _repos.update { currentState ->
                        currentState.copy(
                            repos = items.map {
                                KotlinRepo(
                                    name = it.name,
                                    authorName = it.owner.login,
                                    image = it.owner.avatarUrl,
                                    starGazersCount = it.stargazersCount,
                                    forksCount = it.forksCount
                                )
                            },
                            isScreenLoading = false
                        )
                    }
                    nextPageToBeLoaded++
                }
                is Error -> {
                    _repos.update {
                        it.copy(
                            isInitializeFailure = true
                        )
                    }
                }
            }
        }
    }

    private fun updateToLoadingState() {
        _repos.update {
            it.copy(
                isScreenLoading = true
            )
        }
    }

    fun refreshRepos() {
        nextPageToBeLoaded = 1
        endReached = false
        _repos.update {
            it.copy(
                isInitializeFailure = false,
                isNextPageFailure = false
            )
        }
        fetchInitialRepos()
    }

    fun loadNextPage() {
        if (_repos.value.isNextPageLoading || endReached) return

        _repos.update {
            it.copy(
                isNextPageLoading = true,
                isNextPageFailure = false
            )
        }

        viewModelScope.launch {
            when (val result = getKotlinRepositoriesUseCase(nextPageToBeLoaded)) {
                is Success -> {
                    val items = result.data.items

                    val newKotlinRepos = items.map {
                        KotlinRepo(
                            name = it.name,
                            authorName = it.owner.login,
                            image = it.owner.avatarUrl,
                            starGazersCount = it.stargazersCount,
                            forksCount = it.forksCount
                        )
                    }

                    if (newKotlinRepos.isEmpty()) {
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
                }
                is Error -> {
                    _repos.update {
                        it.copy(
                            isNextPageLoading = false,
                            isNextPageFailure = true
                        )
                    }
                }
            }
        }
    }
}
