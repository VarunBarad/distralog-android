package com.varunbarad.distralog.repositories

import com.varunbarad.distralog.external_services.local_database.LogEntryDao
import com.varunbarad.distralog.external_services.local_database.models.DbLogEntry
import com.varunbarad.distralog.util.ThreadSchedulers
import io.reactivex.Completable
import io.reactivex.Observable

class LogEntryRepository(
    private val logEntryDao: LogEntryDao,
) {
    fun insertNewLogEntry(logEntry: DbLogEntry): Completable {
        return logEntryDao.insertLogEntry(entry = logEntry)
            .subscribeOn(ThreadSchedulers.io)
            .observeOn(ThreadSchedulers.main)
    }

    fun getAllEntriesSortedReverseChronologicallyByTimestamp(): Observable<List<DbLogEntry>> {
        return logEntryDao.getAllEntriesSortedReverseChronologicallyByTimestamp()
            .subscribeOn(ThreadSchedulers.io)
            .observeOn(ThreadSchedulers.main)
    }

    fun deleteEntry(entry: DbLogEntry): Completable {
        return logEntryDao.deleteEntry(entry = entry)
            .subscribeOn(ThreadSchedulers.io)
            .observeOn(ThreadSchedulers.main)
    }
}
