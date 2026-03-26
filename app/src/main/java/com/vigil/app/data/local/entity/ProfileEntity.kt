package com.vigil.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.UUID

@Entity(
    tableName = "profiles",
    indices = [
        Index(value = ["nfc_uid"], unique = true),
    ],
)
data class ProfileEntity(
    @PrimaryKey
    val id: UUID,
    val name: String,
    @ColumnInfo(name = "nfc_uid")
    val nfcUid: String,
    @ColumnInfo(name = "is_active")
    val isActive: Boolean,
    @ColumnInfo(name = "created_at")
    val createdAt: Instant,
)
