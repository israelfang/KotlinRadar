package com.kotlinradar.app.data

import com.kotlinradar.app.core.Result
import com.kotlinradar.app.data.model.OwnerEntity
import com.kotlinradar.app.data.model.RepositoriesItemsEntity
import com.kotlinradar.app.data.model.RepositoryEntity
import com.kotlinradar.app.data.remote.GithubApi
import com.kotlinradar.app.data.repository.GitHubRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GitHubRepositoryTest {

    private lateinit var githubApi: GithubApi
    private lateinit var gitHubRepository: GitHubRepository

    @Before
    fun setup() {
        githubApi = mockk()
        gitHubRepository = GitHubRepository(githubApi)
    }

    @Test
    fun `should return success when API call is successful`() = runTest {
        val mockResponse = RepositoriesItemsEntity(
            items = listOf(
                RepositoryEntity(
                    name = "KotlinRepo",
                    stargazersCount = 100,
                    forksCount = 50,
                    owner = OwnerEntity(
                        login = "owner",
                        avatarUrl = "https://example.com/avatar.png"
                    )
                )
            )
        )

        coEvery { githubApi.searchGithubRepositories(any(), any(), any(), any()) } returns mockResponse

        val result = gitHubRepository.getKotlinRepos("Kotlin", "stars", 10, 1)

        assertTrue(result is Result.Success)
        val data = (result as Result.Success).data
        assertEquals(1, data.items.size)
        assertEquals("KotlinRepo", data.items[0].name)
        assertEquals("owner", data.items[0].owner.login)
    }

    @Test
    fun `should return error when API call fails`() = runTest {
        coEvery { githubApi.searchGithubRepositories(any(), any(), any(), any()) } throws Exception("API error")

        val result = gitHubRepository.getKotlinRepos("Kotlin", "stars", 10, 1)

        assertTrue(result is Result.Error)
    }
}
