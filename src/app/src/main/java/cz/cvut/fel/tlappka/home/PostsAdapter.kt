package cz.cvut.fel.tlappka.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import cz.cvut.fel.tlappka.R
import cz.cvut.fel.tlappka.home.model.Post

class PostsAdapter(val posts: ArrayList<Post>, val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_POST: Int = 0
        const val TYPE_AD: Int = 1
    }

    override fun getItemCount() = posts.size

    //create new views (invoked by the layout manager)
    //constructs ViewHolder and set the view it uses to display its contents
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_POST) {
            //create a new view
            val view: View = LayoutInflater.from(parent.context).inflate(R.layout.activity_post_row, parent, false)
            return PostViewHolder(view)
        }
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.activity_ad_row, parent, false)
        return AdViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        if ((position % 5) == 0 && position != 0) {
            return TYPE_AD
        }
        return TYPE_POST
    }

    //replace the contents of a view (invoked by the layout manager)
    //bind the view holder to its data, fetch the correct data and fill in the view
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_POST) {
            //get element from dataset at this position and replace the contents of the view with it
            (holder as PostViewHolder).dateTime.text = posts[position].date_time
            holder.username.text = posts[position].username
            holder.text.text = posts[position].text
            holder.description.text = posts[position].description
            Picasso.with(context).load(posts[position].user_photo).into(holder.userPhoto)
            Picasso.with(context).load(posts[position].activity_content).into(holder.activityContent)

        } else if (getItemViewType(position) == TYPE_AD) {

        }
    }

    //TODO handle Post data and with_users, make TextView for with users variable
    //TODO prepare with_users into single String instead of an array? in PostContentHandler

    //provide a reference to the views for each data item, linked to activity_post_row
    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTime: TextView = itemView.findViewById(R.id.date_time)
        val username: TextView = itemView.findViewById(R.id.username)
        val text: TextView = itemView.findViewById(R.id.header_text)
        val userPhoto: ImageView = itemView.findViewById(R.id.user_photo)
        val description: TextView = itemView.findViewById(R.id.description)
        val activityContent: ImageView = itemView.findViewById(R.id.activity_content)
    }

    class AdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val photo: ImageView = itemView.findViewById(R.id.photo)
//        this is an ad. Insert ad text here
    }
}