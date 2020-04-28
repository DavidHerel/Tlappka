package cz.cvut.fel.tlappka.home.model

//data class Post(val username: String, val text: String, val photo: String)
data class Post(
    val date_time: String,
    val username: String,
    val with_users: ArrayList<String>,
    val with_users_size: Int,
    val with_users_photo: ArrayList<String>,
    val activity_type: String,
    val text: String,
    val user_photo: String,
    val description: String,
    val activity_content: String,
    val text_all: String)

    //TODO complete more details: num likes, comments etc