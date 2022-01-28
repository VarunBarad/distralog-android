package com.varunbarad.distralog.external_services.local_database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.varunbarad.distralog.external_services.local_database.models.DbLogEntry

@Database(
    entities = [
        DbLogEntry::class,
    ],
    version = DistralogDatabase.databaseVersion,
    exportSchema = false,
)
@TypeConverters(RoomTypeConverters::class)
abstract class DistralogDatabase : RoomDatabase() {
    abstract fun logEntryDao(): LogEntryDao

    companion object {
        const val databaseVersion = 1
        const val databaseName = "Distralog-Database"
    }
}
