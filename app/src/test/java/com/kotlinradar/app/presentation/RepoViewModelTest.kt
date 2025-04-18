package com.kotlinradar.app.presentation

import com.kotlinradar.app.core.Result
import com.kotlinradar.app.domain.model.Owner
import com.kotlinradar.app.domain.model.RepositoriesItems
import com.kotlinradar.app.domain.model.Repository
import com.kotlinradar.app.domain.usecase.GetKotlinRepositoriesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RepoViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var useCase: GetKotlinRepositoriesUseCase
    private lateinit var viewModel: RepoViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        useCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when fetchInitialRepos is successful, should update state with repos`() = runTest {
        val fakeRepo = Repository(
            name = "KotlinRadar",
            stargazersCount = 123,
            forksCount = 10,
            owner = Owner("user", "avatarUrl")
        )
        val reposResult = Result.Success(RepositoriesItems(listOf(fakeRepo)))
        coEvery { useCase(any()) } returns reposResult

        viewModel = RepoViewModel(useCase)
        advanceUntilIdle()

        val state = viewModel.repos.value
        assertFalse(state.isScreenLoading)
        assertEquals(1, state.repos.size)
        assertEquals("KotlinRadar", state.repos[0].name)
        assertEquals("user", state.repos[0].authorName)
    }

    @Test
    fun `when fetchInitialRepos returns error, should update state with isError`() = runTest {
        coEvery { useCase(any()) } returns Result.Error(Exception("API error"))

        viewModel = RepoViewModel(useCase)
        advanceUntilIdle()

        val state = viewModel.repos.value
        assertTrue(state.isInitializeFailure)
        assertTrue(state.repos.isEmpty())
    }

    @Test
    fun `loadNextPage should append new repos when successful`() = runTest {
        val initialRepo = Repository("Repo1", 1, 1, Owner("author1", "avatar1"))
        val nextPageRepo = Repository("Repo2", 2, 2, Owner("author2", "avatar2"))

        coEvery { useCase(1) } returns Result.Success(RepositoriesItems(listOf(initialRepo)))
        coEvery { useCase(2) } returns Result.Success(RepositoriesItems(listOf(nextPageRepo)))

        viewModel = RepoViewModel(useCase)
        advanceUntilIdle()

        viewModel.loadNextPage()
        advanceUntilIdle()

        val state = viewModel.repos.value
        assertEquals(2, state.repos.size)
        assertEquals("Repo1", state.repos[0].name)
        assertEquals("Repo2", state.repos[1].name)
        assertFalse(state.isNextPageLoading)
        assertFalse(state.isNextPageFailure)
    }


    @Test
    fun `loadNextPage with empty result should mark endReached`() = runTest {
        val initialRepo = Repository("Repo1", 1, 1, Owner("author1", "avatar1"))

        coEvery { useCase(1) } returns Result.Success(RepositoriesItems(listOf(initialRepo)))
        coEvery { useCase(2) } returns Result.Success(RepositoriesItems(emptyList()))

        viewModel = RepoViewModel(useCase)
        advanceUntilIdle()

        viewModel.loadNextPage()
        advanceUntilIdle()

        val state = viewModel.repos.value
        assertEquals(1, state.repos.size)
        assertFalse(state.isNextPageLoading)
        assertFalse(state.isNextPageFailure)

        viewModel.loadNextPage()
        advanceUntilIdle()

        assertEquals(1, viewModel.repos.value.repos.size)
    }

    @Test
    fun `loadNextPage should update state with error on failure`() = runTest {
        val initialRepo = Repository("Repo1", 1, 1, Owner("author1", "avatar1"))

        coEvery { useCase(1) } returns Result.Success(RepositoriesItems(listOf(initialRepo)))
        coEvery { useCase(2) } returns Result.Error(Exception("Pagination failed"))

        viewModel = RepoViewModel(useCase)
        advanceUntilIdle()

        viewModel.loadNextPage()
        advanceUntilIdle()

        val state = viewModel.repos.value
        assertEquals(1, state.repos.size)
        assertTrue(state.isNextPageFailure)
        assertFalse(state.isNextPageLoading)
    }
}
