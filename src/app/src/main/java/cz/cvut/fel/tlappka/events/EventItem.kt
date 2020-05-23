package cz.cvut.fel.tlappka.events

import android.os.Parcel
import android.os.Parcelable

data class EventItem(
    var id: String?,
    val name: String?,
    var in_progress: Boolean?,
    val date: String?,
    val time: String?,
//    val with_users: ArrayList<String>,
//    val with_pets: ArrayList<String>,
    val description: String?,
    val type: String?,
    val private: Boolean?,
    val GPS_tracking: Boolean?,
    val place: String?
) {
    constructor() : this("", "", true, "", "",
        "", "", true, false, "")

}
