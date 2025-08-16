package com.stevens.software.qrcraft.ui.toolkit

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import com.stevens.software.qrcraft.R
import com.stevens.software.qrcraft.ui.theme.Text
import com.stevens.software.qrcraft.ui.theme.extendedColours

@Composable
fun QRScannerOverlay() {
    val density = LocalDensity.current
    val overlayColour = MaterialTheme.extendedColours.overlay
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


    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(overlayColour)

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
