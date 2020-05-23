package cz.cvut.fel.tlappka.events

data class HistoryItem(
    var id: String?,
    var name: String?,
    var date: String?,
    var start_time: String?,
    var end_time: Long?,
//    val with_users: ArrayList<String>,
//    val with_pets: ArrayList<String>,
    var description: String?,
    var type: String?,
    var private: Boolean?,
    var GPS_tracking: Boolean?,
    var place: String?
//    val route: ???
) {
    constructor() : this("", "", "", "", 0,
        "", "", true, false, "")
}