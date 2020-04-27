package cz.cvut.fel.tlappka.home.model

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PostContentHandler {

    companion object {
        const val TYPE_PHOTO: Int = 0
        const val TYPE_VIDEO: Int = 1
        const val TYPE_WALK: Int = 2
    }

    //so far just generates random content for a single post
    //TODO replace with DB data handling
    fun generateContent(idx: Int): Post {
        val range: Int
        val type_range = (0..2).random()
        val with_users: ArrayList<String> = ArrayList()
        val with_users_photos: ArrayList<String> = ArrayList()
        if (type_range == TYPE_WALK) {
            range = (0..3).random()
            for (i in 0..range) {
                if (i % 2 == 0) with_users.add("Friend" + i)
                else with_users.add("Pet" + i)
            }
            assignWithUsersPhotos(range, with_users_photos)
        } else range = 0

        return Post(
            getCurrentDateTime().toString("dd.MM HH:mm"), "Username" + idx,
            with_users,
            with_users.size,
            with_users_photos,
            type_range.toString(), assignText(type_range),
            "https://picsum.photos/600/300?random&" + (idx*4),
            "Tady bude popis eventu nebo fotky :)",
            "https://picsum.photos/600/300?random&" + idx
        )
    }

    fun assignWithUsersPhotos(range: Int, list: ArrayList<String>) {
        for (j in (0..range)) {
            list.add("https://picsum.photos/600/300?random&" + (j*5))
        }
    }

    fun assignText(type_id: Int): String {
        when (type_id) {
            TYPE_PHOTO -> {
                return "přidal novou fotku."
            }
            TYPE_VIDEO -> {
                return "přidal nové video."
            }
            TYPE_WALK -> {
                return "byl na procházce s"
                /*
                val sb = StringBuilder()
                sb.append("byl na procházce s ")
                for (i in (0..with_users.size)) {
                    sb.append(with_users[i])
                    if (i != with_users.size) sb.append(", ")
                    else sb.append(".")
                }
                return sb.toString() */
            }
        }
        return "přidal novou fotku."
    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

}