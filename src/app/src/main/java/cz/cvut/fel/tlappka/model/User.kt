package cz.cvut.fel.tlappka.model

import java.util.*

data class User (val name : String = "", val birthDate : Date = Date(), val place : String = "", val email : String = "", val hobbies : String = "",
                 val about : String = "", val job : String = "")