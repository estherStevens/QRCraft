package com.stevens.software.uitoolkit.toolkit

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.stevens.software.uitoolkit.R
import com.stevens.software.uitoolkit.theme.extendedColours

@Composable
fun Favourite(
    isFavourite: Boolean,
    onFavouriteToggle: (Boolean) -> Unit,
    iconColour: Color
){
    val favouriteIcon = when(isFavourite) {
        true -> painterResource(R.drawable.favourited_icon)
        false -> painterResource(R.drawable.favourite_icon)
    }
    val contentDescription = when(isFavourite) {
        true -> stringResource(R.string.qr_unfavourite)
        false -> stringResource(R.string.qr_favourite)
    }
    IconButton(
        onClick = {
            onFavouriteToggle(isFavourite.not())
        }
    ) {
        Icon(
            painter = favouriteIcon,
            tint = iconColour,
            contentDescription = contentDescription
        )
    }
}