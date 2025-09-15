package com.stevens.software.core

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "qrCodes")
data class QrCode(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val qrBitmapPath: String,
    val parsedData: QrResult?,
    val dateCreated: String
)
