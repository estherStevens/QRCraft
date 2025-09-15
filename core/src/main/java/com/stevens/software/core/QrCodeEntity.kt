package com.stevens.software.qrcraft.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.OffsetDateTime

@Entity(tableName = "qrCodes")
data class QrCode(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val qrBitmapPath: String,
    val parsedData: QrResult?,
    val dateCreated: String
)
