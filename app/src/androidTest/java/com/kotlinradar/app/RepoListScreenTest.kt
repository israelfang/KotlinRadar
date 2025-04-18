package com.kotlinradar.app

import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import com.kotlinradar.app.presentation.RepoListScreen
import com.kotlinradar.app.presentation.ui.theme.KotlinRadarTheme
import org.junit.Rule
import org.junit.Test

class RepoListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testRepoListIsDisplayed() {
        composeTestRule.setContent {
            KotlinRadarTheme(
                darkTheme = true,
                dynamicColor = false
            ) {
                RepoListScreen()
            }
        }

        composeTestRule.waitUntil(3000) {
            composeTestRule.onAllNodesWithTag("repoList").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onAllNodesWithTag("repoCard").assertAny(hasTestTag("repoCard"))
    }
}
