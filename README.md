# KotlinRadar 📡

**KotlinRadar** is an Android app built with Jetpack Compose that lists the most starred Kotlin repositories from GitHub. It supports infinite scrolling, offline caching, orientation changes, and a clean architecture approach with MVVM, StateFlow, and modular responsibilities.

---

## ✨ Features

- 🔍 Fetches top-starred Kotlin repositories using the GitHub API
- ♾️ Infinite scroll pagination
- 🔄 Pull-to-refresh
- 📶 Offline cache support (Coming soon)
- 🔁 Handles orientation changes gracefully
- ⚙️ MVVM architecture with Clean Architecture principles
- ✅ Unit tests for ViewModel and Repository logic

---

## 🧱 Project Architecture

```text
com.kotlinradar.app
│
├── data                  # Data layer: Retrofit, Models, Mappers, Repository impl
│   ├── remote            # GitHub API definition
│   ├── model             # Network models (DTOs)
│   ├── mapper            # Maps data models to domain models
│   └── repository        # GitHubRepository implementation
│
├── domain                # Domain layer: UseCases, Models, Interfaces
│   ├── model             # KotlinRepo, Owner, etc.
│   ├── repository        # GitHubRepository interface
│   └── usecase           # GetKotlinRepositoriesUseCase
│
├── presentation          # UI layer: ViewModel and Screens
│   ├── screen            # Composables for listing repos
│   └── viewmodel         # RepoViewModel
│
├── core                  # Shared core utilities (Result.kt, constants)
└── utils                 # Extensions, helpers (Coming soon)
