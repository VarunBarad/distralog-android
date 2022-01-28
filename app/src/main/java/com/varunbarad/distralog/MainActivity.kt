package com.varunbarad.distralog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.varunbarad.distralog.databinding.ActivityMainBinding
import com.varunbarad.distralog.external_services.local_database.models.DbLogEntry
import com.varunbarad.distralog.util.Dependencies
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.UUID

class MainActivity : AppCompatActivity() {
    private val serviceDisposables = CompositeDisposable()

    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(viewBinding.toolbar)
    }

    override fun onStart() {
        super.onStart()

        val repository = Dependencies.getLogEntryRepository(context = this)

        viewBinding.buttonNoteDistraction.setOnClickListener {
            val now = LocalDateTime.now()
            serviceDisposables.add(
                repository.insertNewLogEntry(
                    logEntry = DbLogEntry(
                        id = UUID.randomUUID(),
                        createdAt = now,
                    ),
                ).subscribeBy {
                    Snackbar.make(
                        this.viewBinding.root,
                        "Noted",
                        Snackbar.LENGTH_SHORT,
                    ).show()
                },
            )
        }

        serviceDisposables.add(
            repository.getAllEntriesSortedReverseChronologicallyByTimestamp()
                .subscribeBy { entries ->
                    val now = LocalDateTime.now()
                    val today = now.toLocalDate()

                    val last24HoursEntries = entries.filter { it.createdAt > now.minusDays(1) }
                    val todayEntries = last24HoursEntries
                        .filter { it.createdAt.toLocalDate() == today }

                    viewBinding.textViewToday.text = if (todayEntries.size == 1) {
                        "Distracted today: ${todayEntries.size} time"
                    } else {
                        "Distracted today: ${todayEntries.size} times"
                    }
                    viewBinding.textView24Hours.text = if (last24HoursEntries.size == 1) {
                        "Distracted in last 24 hours: ${last24HoursEntries.size} time"
                    } else {
                        "Distracted in last 24 hours: ${last24HoursEntries.size} times"
                    }

                    val lastEntry = entries.maxByOrNull { it.createdAt }

                    val lastEntryText = if (lastEntry != null) {
                        val lastEntryWasToday = lastEntry.createdAt.toLocalDate() == today
                        val lastEntryWasYesterday = lastEntry.createdAt.toLocalDate().plusDays(1) == today

                        when {
                            lastEntryWasToday -> timeFormatter.format(lastEntry.createdAt)
                            lastEntryWasYesterday -> "Yesterday, ${timeFormatter.format(lastEntry.createdAt)}"
                            else -> {
                                val daysOfDifference = ChronoUnit.DAYS.between(
                                    lastEntry.createdAt.toLocalDate(),
                                    now.toLocalDate(),
                                )

                                "$daysOfDifference days ago, ${timeFormatter.format(lastEntry.createdAt)}"
                            }
                        }
                    } else {
                        "No distractions noted"
                    }

                    viewBinding.textViewLastDistractionTime.text = "Last distracted: $lastEntryText"
                }
        )
    }

    override fun onStop() {
        super.onStop()

        viewBinding.buttonNoteDistraction.setOnClickListener(null)

        serviceDisposables.clear()
    }
}
