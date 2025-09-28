package com.stevens.software.uitoolkit.toolkit

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBar(
    title: String,
    backgroundColor: Color,
    titleColour: Color,
    navigationIcon: @Composable () -> Unit = {},
    trailingIcon: @Composable () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                color = titleColour,
            )
        },
        navigationIcon = navigationIcon,
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = backgroundColor
        ),
        actions = {
            trailingIcon()
        }
    )
}