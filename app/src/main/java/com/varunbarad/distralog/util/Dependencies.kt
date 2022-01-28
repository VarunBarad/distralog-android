package com.varunbarad.distralog.util

import android.content.Context
import androidx.room.Room
import com.varunbarad.distralog.external_services.local_database.DistralogDatabase
import com.varunbarad.distralog.repositories.LogEntryRepository

object Dependencies {
    private lateinit var distralogDatabase: DistralogDatabase

    fun getDistralogDatabase(context: Context): DistralogDatabase {
        synchronized(this) {
            if (this::distralogDatabase.isInitialized.not()) {
                this.distralogDatabase = Room.databaseBuilder(
                    context.applicationContext,
                    DistralogDatabase::class.java,
                    DistralogDatabase.databaseName,
                ).build()
            }
        }

        return this.distralogDatabase
    }

    fun getLogEntryRepository(context: Context): LogEntryRepository {
        return LogEntryRepository(this.getDistralogDatabase(context).logEntryDao())
    }
}
