package com.varunbarad.distralog.external_services.local_database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.UUID

@Entity(tableName = "LogEntries")
data class DbLogEntry(
    @PrimaryKey @ColumnInfo(name = "id") val id: UUID,
    @ColumnInfo(name = "created_at") val createdAt: LocalDateTime,
)
