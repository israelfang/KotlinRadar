package com.kotlinradar.app.data.mapper

import com.kotlinradar.app.data.model.OwnerEntity
import com.kotlinradar.app.data.model.RepositoriesItemsEntity
import com.kotlinradar.app.data.model.RepositoryEntity
import com.kotlinradar.app.domain.model.Owner
import com.kotlinradar.app.domain.model.RepositoriesItems
import com.kotlinradar.app.domain.model.Repository

fun RepositoriesItemsEntity.toDomain(): RepositoriesItems = RepositoriesItems(
    items = items.map { it.toDomain() })

fun RepositoryEntity.toDomain(): Repository = Repository(
    name = name,
    stargazersCount = stargazersCount,
    forksCount = forksCount,
    owner = owner.toDomain()
)

fun OwnerEntity.toDomain(): Owner = Owner(
    login = login, avatarUrl = avatarUrl
)
