package com.kotlinradar.app.domain.usecase

import com.kotlinradar.app.core.Result
import com.kotlinradar.app.domain.model.RepositoriesItems
import com.kotlinradar.app.domain.repository.GitHubRepository

class GetKotlinRepositoriesUseCaseImpl(
    private val repository: GitHubRepository
) : GetKotlinRepositoriesUseCase {
    override suspend fun invoke(page: Int): Result<RepositoriesItems> {
        return repository.getKotlinRepos(
            query = "language:kotlin",
            sort = "stars",
            perPage = 10,
            page = page
        )
    }
}
