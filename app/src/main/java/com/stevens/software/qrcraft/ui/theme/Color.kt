package com.stevens.software.qrcraft.ui.theme

import androidx.compose.ui.graphics.Color

val Primary = Color(0xFFEBFF69)
val Surface = Color(0xFFEDF2F5)
val OnSurface = Color(0xFF273037)
val Error = Color(0xFFF12244)
val SurfaceHigher = Color(0xFFFFFFFF)
val OnSurfaceAlt = Color(0xFF273037)
val OnSurfaceDisabled = Color(0xFF8C99A2)
val Overlay = Color(0xFF000000).copy(alpha = 0.5F)
val OnOverlay = Color(0xFFFFFFFF)
val Link = Color(0xFF373F05)
val LinkBg = Color(0xFFEBFF69)
val Success = Color(0xFF4DDA9D)
val Text = Color(0xFF583DC5)
val TextBg = Color(0xFF583DC5)
val Contact = Color(0xFF259570)
val ContactBg = Color(0xFF259570).copy(alpha = 0.1F)
val Geo = Color(0xFFB51D5C)
val GeoBg = Color(0xFFB51D5C).copy(alpha = 0.1F)
val Phone = Color(0xFFC86017)
val PhoneBg = Color(0xFFC86017).copy(alpha = 0.1F)
val Wifi = Color(0xFF1F44CD)
val WifiBg = Color(0xFF1F44CD).copy(alpha = 0.1F)


val extendedColors = ExtendedColors(
    surfaceHigher = SurfaceHigher,
    onSurfaceAlt = OnSurfaceAlt,
    onSurfaceDisabled = OnSurfaceDisabled,
    overlay = Overlay,
    onOverlay = OnOverlay,
    link = Link,
    linkBg = LinkBg,
    success = Success,
    text = Text,
    textBg = TextBg,
    contact = Contact,
    contactBg = ContactBg,
    geo = Geo,
    geoBg = GeoBg,
    phone = Phone,
    phoneBg = PhoneBg,
    wifi = Wifi,
    wifiBg = WifiBg,
)

data class ExtendedColors(
    val surfaceHigher: Color,
    val onSurfaceAlt: Color,
    val onSurfaceDisabled: Color,
    val overlay: Color,
    val onOverlay: Color,
    val link: Color,
    val linkBg: Color,
    val success: Color,
    val text: Color,
    val textBg: Color,
    val contact: Color,
    val contactBg: Color,
    val geo: Color,
    val geoBg: Color,
    val phone: Color,
    val phoneBg: Color,
    val wifi: Color,
    val wifiBg: Color
)
