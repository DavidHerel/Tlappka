package cz.cvut.fel.tlappka.model

import java.util.*

data class User (var name : String = "", var birthDate : Date = Date(), var place : String = "", var email : String = "", var hobbies : String = "",
                 var about : String = "", var job : String = "")