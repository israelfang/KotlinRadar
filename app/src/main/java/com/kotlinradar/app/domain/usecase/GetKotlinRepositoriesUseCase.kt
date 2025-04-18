package com.kotlinradar.app.domain.usecase

import com.kotlinradar.app.core.Result
import com.kotlinradar.app.domain.model.RepositoriesItems

interface GetKotlinRepositoriesUseCase {
    suspend operator fun invoke(page: Int): Result<RepositoriesItems>
}
