package cz.cvut.fel.tlappka.events

import java.sql.Date
import java.sql.Time

data class EventItem(
    val name: String,
    val in_progress: Boolean,
    val date: Date,
    val time: Time?,
    val with_users: ArrayList<String>,
    val with_pets: ArrayList<String>,
    val description: String?,
    val type: String,
    val private: Boolean,
    val GPS_tracking: Boolean
)