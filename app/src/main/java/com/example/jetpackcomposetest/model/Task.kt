package com.example.jetpackcomposetest.model

import java.security.InvalidParameterException
import java.time.Duration
import java.time.Instant

class Task(
    val name: String,
    val interval: Interval,
    val duration: Duration,
    val effect: Double,
)

class Interval(
    val start: Instant,
    val end: Instant,
) {
    init {
        val twentyFourHours = Duration.ofDays(1)
        if (Duration.between(start, end).toNanos() > twentyFourHours.toNanos()) {
            throw InvalidParameterException("Interval greater than 24 hours!")
        }
    }
}
