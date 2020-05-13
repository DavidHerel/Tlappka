package cz.cvut.fel.tlappka.events

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import cz.cvut.fel.tlappka.R
import cz.cvut.fel.tlappka.profile.ProfileFragmentViewModel
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_create_event.*
import kotlinx.android.synthetic.main.event_item.view.*
import androidx.fragment.app.viewModels

class EventsAdapter(private val eventsList: List<EventItem>) : RecyclerView.Adapter<EventsAdapter.MyViewHolder>() {


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val eventName: TextView = itemView.event_list_name
        val date: TextView = itemView.event_list_date
        val time: TextView = itemView.event_list_time
        val type: TextView = itemView.event_list_type
        val privacy: TextView = itemView.event_list_privacy
        val GPSTracking: TextView = itemView.event_list_GPS
        val description_text: TextView = itemView.event_list_description
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        return MyViewHolder(v)
    }
    override fun getItemCount(): Int {
        return eventsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = eventsList[position]
        holder.eventName.text = currentItem.name
        holder.date.text = currentItem.date.toString()
        holder.time.text = currentItem.time.toString()
        holder.type.text = currentItem.type
        holder.privacy.text = getPrivacyString(currentItem.private)
        holder.GPSTracking.text = getGPSstring(currentItem.GPS_tracking)
        holder.description_text.text = currentItem.description
    }

    private fun getGPSstring(gpsTracking: Boolean): CharSequence? {
        return when (gpsTracking) {
            true -> "Sledování pomocí GPS zapnuto"
            else -> "Sledování pomocí GPS vypnuto"
        }
    }

    private fun getPrivacyString(private: Boolean): CharSequence? {
        return when (private) {
            true -> "Soukromý"
            else -> "Veřejný"
        }
    }
}