package cz.cvut.fel.tlappka.events

import android.os.Build
import androidx.annotation.RequiresApi
import java.sql.Date
import java.sql.Time
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

data class EventItem(
    val name: String?,
    val in_progress: Boolean?,
    val date: String?,
    val time: String?,
//    val with_users: ArrayList<String>,
//    val with_pets: ArrayList<String>,
    val description: String?,
    val type: String?,
    val private: Boolean?,
    val GPS_tracking: Boolean?
) {
    constructor() : this("", true, "", "",
        "", "", true, false)
}