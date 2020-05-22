package cz.cvut.fel.tlappka.events

import java.io.Serializable

data class EventItem(
    val name: String?,
    var in_progress: Boolean?,
    val date: String?,
    val time: String?,
//    val with_users: ArrayList<String>,
//    val with_pets: ArrayList<String>,
    val description: String?,
    val type: String?,
    val private: Boolean?,
    val GPS_tracking: Boolean?
): Serializable {
    constructor() : this("", true, "", "",
        "", "", true, false)
}
