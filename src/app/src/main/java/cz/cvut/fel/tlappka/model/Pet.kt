package cz.cvut.fel.tlappka.model

import cz.cvut.fel.tlappka.events.EventItem
import java.util.*
import kotlin.collections.ArrayList

data class Pet (var name : String = "", var birthDate : Date = Date(), var place : String = "", var hobbies : String = "",
                 var about : String = "", var owners : ArrayList<String> = java.util.ArrayList(), var uid : String = ""
)