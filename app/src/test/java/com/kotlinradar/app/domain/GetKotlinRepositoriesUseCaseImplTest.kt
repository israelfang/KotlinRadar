package com.kotlinradar.app.domain

import com.kotlinradar.app.domain.repository.GitHubRepository
import com.kotlinradar.app.domain.usecase.GetKotlinRepositoriesUseCaseImpl
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import com.kotlinradar.app.core.Result.Success
import com.kotlinradar.app.core.Result.Error
import com.kotlinradar.app.domain.model.RepositoriesItems
import junit.framework.TestCase.assertTrue
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

class GetKotlinRepositoriesUseCaseImplTest {

    private val repository = mock<GitHubRepository>()
    private val useCase = GetKotlinRepositoriesUseCaseImpl(repository)

    @Test
    fun `invoke should return success when repository returns success`() = runTest {
        val fakeResult = Success(RepositoriesItems(emptyList()))
        whenever(repository.getKotlinRepos(any(), any(), any(), any())).thenReturn(fakeResult)

        val result = useCase(1)

        assertTrue(result is Success)
    }

    @Test
    fun `invoke should return error when repository returns error`() = runTest {
        val error = Error(Exception("Network error"))
        whenever(repository.getKotlinRepos(any(), any(), any(), any())).thenReturn(error)

        val result = useCase(1)

        assertTrue(result is Error)
    }
}