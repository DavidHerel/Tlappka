package cz.cvut.fel.tlappka.model

import cz.cvut.fel.tlappka.events.EventItem
import java.util.*
import kotlin.collections.ArrayList

data class User (var name : String = "", var birthDate : Date = Date(), var place : String = "", var email : String = "", var hobbies : String = "",
                 var about : String = "", var job : String = "", var plannedEvents : ArrayList<EventItem> = arrayListOf()
)