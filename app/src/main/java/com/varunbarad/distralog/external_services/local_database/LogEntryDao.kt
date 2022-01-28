package com.varunbarad.distralog.external_services.local_database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.varunbarad.distralog.external_services.local_database.models.DbLogEntry
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface LogEntryDao {
    @Insert
    fun insertLogEntry(entry: DbLogEntry): Completable

    @Query(
        """
        select id, created_at
        from LogEntries
        order by created_at desc
        """
    )
    fun getAllEntriesSortedReverseChronologicallyByTimestamp(): Observable<List<DbLogEntry>>

    @Delete
    fun deleteEntry(entry: DbLogEntry): Completable
}
