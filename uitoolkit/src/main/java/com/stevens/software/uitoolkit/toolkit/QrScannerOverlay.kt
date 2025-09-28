package com.stevens.software.uitoolkit.toolkit

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.stevens.software.uitoolkit.R
import com.stevens.software.uitoolkit.theme.QRCraftTheme
import com.stevens.software.uitoolkit.theme.extendedColours

@Composable
fun QRScannerOverlay(
    isFlashEnabled: Boolean = true,
    onFlashToggled: (Boolean) -> Unit = {}
) {
    val density = LocalDensity.current
    val strokeWidth = 4.dp
    val boxSize = 300.dp
    val cornerRadius = 18.dp
    val cornerLength = 60.dp

    val boxSizePx = with(density) { Size(boxSize.toPx(), boxSize.toPx()) }
    val strokeWidthPx = with(density) { strokeWidth.toPx() }
    val cornerLengthPx = with(density) { cornerLength.toPx() }
    val borderColour = MaterialTheme.colorScheme.primary
    val cornerRadiusPx = with(density) { cornerRadius.toPx()}

    val stroke = remember(strokeWidthPx) {
        Stroke(
            width = strokeWidthPx,
            join = StrokeJoin.Round,
            cap = StrokeCap.Round
        )
    }


    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.extendedColours.overlay)
    ) {
        FlashButton(
            isFlashEnabled = isFlashEnabled,
            onFlashToggled = onFlashToggled
        )
        Canvas(modifier = Modifier.fillMaxSize()) {
            val left = (size.width - boxSizePx.width) / 2f
            val top = (size.height - boxSizePx.height) / 2f
            val right = left + boxSizePx.width
            val bottom = top + boxSizePx.height


            drawRoundRect(
                color = Color.Transparent,
                topLeft = Offset(left, top),
                size = boxSizePx,
                blendMode = BlendMode.Clear,
                cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
            )

            fun drawCornerBorder(
                rect: Rect,
                arcStartAngle: Float,
                line1Start: Offset,
                line1End: Offset,
                line2Start: Offset,
                line2End: Offset
            ){
                val path = Path().apply {
                    arcTo(
                        rect = rect,
                        startAngleDegrees = arcStartAngle,
                        sweepAngleDegrees = 90f,
                        forceMoveTo = true
                    )

                    drawLine(
                        color = borderColour,
                        start = line1Start,
                        end = line1End,
                        cap = StrokeCap.Round,
                        strokeWidth = strokeWidthPx
                    )
                    drawLine(
                        color = borderColour,
                        start = line2Start,
                        end = line2End,
                        cap = StrokeCap.Round,
                        strokeWidth = strokeWidthPx
                    )
                }
                drawPath(path = path, color = borderColour, style = stroke)
            }

            drawCornerBorder(
                rect = Rect(
                    left = Offset(left + cornerRadiusPx, top + cornerRadiusPx).x - cornerRadiusPx,
                    top = Offset(left + cornerRadiusPx, top + cornerRadiusPx).y - cornerRadiusPx,
                    right = Offset(left + cornerRadiusPx, top + cornerRadiusPx).x + cornerRadiusPx,
                    bottom = Offset(left + cornerRadiusPx, top + cornerRadiusPx).y + cornerRadiusPx
                ),
                arcStartAngle = 180f,
                line1Start = Offset(left + cornerRadiusPx, top),
                line1End = Offset(left + cornerLengthPx, top),
                line2Start = Offset(left, top + cornerRadiusPx),
                line2End = Offset(left, top + cornerLengthPx)
            )

            drawCornerBorder(
                rect = Rect(
                    left = Offset(right - cornerRadiusPx, top + cornerRadiusPx).x - cornerRadiusPx,
                    top = Offset(right - cornerRadiusPx, top + cornerRadiusPx).y - cornerRadiusPx,
                    right = Offset(right - cornerRadiusPx, top + cornerRadiusPx).x + cornerRadiusPx,
                    bottom = Offset(right - cornerRadiusPx, top + cornerRadiusPx).y + cornerRadiusPx
                ),
                arcStartAngle = 270f,
                line1Start = Offset(right - cornerRadiusPx, top),
                line1End = Offset(right - cornerLengthPx, top),
                line2Start = Offset(right, top + cornerRadiusPx),
                line2End = Offset(right, top + cornerLengthPx)
            )

            drawCornerBorder(
                rect = Rect(
                    left = Offset(left + cornerRadiusPx, bottom - cornerRadiusPx).x - cornerRadiusPx,
                    top = Offset(left + cornerRadiusPx, bottom - cornerRadiusPx).y - cornerRadiusPx,
                    right = Offset(left + cornerRadiusPx, bottom - cornerRadiusPx).x + cornerRadiusPx,
                    bottom = Offset(left + cornerRadiusPx, bottom - cornerRadiusPx).y + cornerRadiusPx
                ),
                arcStartAngle = 90f,
                line1Start = Offset(left + cornerRadiusPx, bottom),
                line1End = Offset(left + cornerLengthPx, bottom),
                line2Start = Offset(left, bottom - cornerRadiusPx),
                line2End = Offset(left, bottom - cornerLengthPx)
            )

            drawCornerBorder(
                rect = Rect(
                    left = Offset(right - cornerRadiusPx, bottom - cornerRadiusPx).x - cornerRadiusPx,
                    top = Offset(right - cornerRadiusPx, bottom - cornerRadiusPx).y - cornerRadiusPx,
                    right = Offset(right - cornerRadiusPx, bottom - cornerRadiusPx).x + cornerRadiusPx,
                    bottom = Offset(right - cornerRadiusPx, bottom - cornerRadiusPx).y + cornerRadiusPx
                ),
                arcStartAngle = 0f,
                line1Start = Offset(right - cornerRadiusPx, bottom),
                line1End = Offset(right - cornerLengthPx, bottom),
                line2Start = Offset(right, bottom - cornerRadiusPx),
                line2End = Offset(right, bottom - cornerLengthPx)
            )

        }
        Text(
            text = stringResource(R.string.qr_overlay_text),
            color = Color.White,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = boxSize + 100.dp)
        )
    }
}

@Composable
private fun FlashButton(
    isFlashEnabled: Boolean,
    onFlashToggled: (Boolean) -> Unit
){
    val flashIcon = when(isFlashEnabled) {
        true -> painterResource(R.drawable.enabled_flash_icon)
        false -> painterResource(R.drawable.disabled_flash_icon)
    }
    val flashButtonBackground = when (isFlashEnabled) {
        true -> MaterialTheme.colorScheme.primary
        false -> MaterialTheme.extendedColours.surfaceHigher
    }

    val flashIconContentDescription  = when(isFlashEnabled) {
        true -> stringResource(R.string.disable_flash)
        false -> stringResource(R.string.enable_flash)
    }

    Button(
        onClick = {
            onFlashToggled(isFlashEnabled.not())
        },
        modifier = Modifier
            .padding(start = 24.dp, top = 19.dp)
            .size(44.dp),
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = flashButtonBackground
        )
    ) {
        Icon(
            painter = flashIcon,
            tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = flashIconContentDescription
        )
    }
}

@Preview
@Composable
private fun QrScannerOverlayPreview(){
    QRCraftTheme {
        QRScannerOverlay(
            false,
            {}
        )
    }
}