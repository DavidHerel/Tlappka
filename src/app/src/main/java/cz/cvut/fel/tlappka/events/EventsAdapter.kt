package cz.cvut.fel.tlappka.events

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import cz.cvut.fel.tlappka.R
import kotlinx.android.synthetic.main.event_item.view.*


class EventsAdapter(private var eventsList: ArrayList<EventItem>, private val mCtx: Context) : RecyclerView.Adapter<EventsAdapter.MyViewHolder>() {


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val eventName: TextView = itemView.event_list_name
        val date: TextView = itemView.event_list_date
        val time: TextView = itemView.event_list_time
        val type: TextView = itemView.event_list_type
        val privacy: TextView = itemView.event_list_privacy
        val GPSTracking: TextView = itemView.event_list_GPS
        val description_text: TextView = itemView.event_list_description
        val inProgress: TextView = itemView.in_progress_text
        val options: TextView = itemView.event_options
        val cardView: CardView = itemView.card_view
        val icon: ImageView = itemView.ic_location
        val location: TextView = itemView.location_text
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
        holder.inProgress.text = getProgressString(currentItem.in_progress)
        holder.type.text = currentItem.type
        holder.privacy.text = getPrivacyString(currentItem.private)
        holder.GPSTracking.text = getGPSstring(currentItem.GPS_tracking)
        holder.description_text.text = currentItem.description
        if (holder.description_text.text.equals("")) {
            holder.description_text.visibility = View.GONE
        }
        holder.location.text = currentItem.place
        if (holder.location.text.equals("")) {
            holder.location.visibility = View.GONE
            holder.icon.visibility = View.GONE
        }
        holder.cardView.setTag(position)
        setUpOptions(currentItem, holder)

    }

    private fun setUpOptions(eventItem: EventItem, holder: MyViewHolder) {
        if (eventItem.in_progress!!) {
            holder.options.setOnClickListener(View.OnClickListener {
                val popup =
                    PopupMenu(mCtx, holder.options)
                popup.inflate(R.menu.event_menu)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.end_item -> {
                            moveEventToHistory(eventItem)
                            true
                        }
                        R.id.delete_item -> {
                            deleteItem(eventItem)
                            true
                        }
                        else -> false
                    }
                }
                //displaying the popup
                popup.show()
            })
        }
        else {
            holder.options.setOnClickListener(View.OnClickListener {
                val popup =
                    PopupMenu(mCtx, holder.options)
                popup.inflate(R.menu.history_menu)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.delete_history_item -> {
                            deleteItem(eventItem)
                            true
                        }
                        else -> false
                    }
                }
                //displaying the popup
                popup.show()
            })
        }
    }

    private fun moveEventToHistory(currentItem: EventItem) {
        val builder = AlertDialog.Builder(mCtx)
        builder.setTitle("Chcete ukončit událost?")
        builder.setMessage("Událost se po ukočení přesune do záložky Historie.")
        builder.setPositiveButton("UKONČIT") { dialog, which ->
            val newHistoryItem = FirebaseDatabase.getInstance().getReference("History")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
            val newId = newHistoryItem.push().key!!
            val historyEvent = currentToHistory(currentItem, newId)
            newHistoryItem.child(newId).setValue(historyEvent)
            val db = FirebaseDatabase.getInstance()
            val myRef = db.getReference("Events")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child(currentItem.id!!)
            myRef.removeValue()
            eventsList.clear()
        }
        builder.setNeutralButton("ZRUŠIT") { dialog, which ->  }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun currentToHistory(currentItem: EventItem, id: String): Any {
        val historyItem = HistoryItem()
        historyItem.id = id
        historyItem.name = currentItem.name
        historyItem.date = currentItem.date
        historyItem.start_time = currentItem.time
        historyItem.end_time = System.currentTimeMillis()
        historyItem.description = currentItem.description
        historyItem.type = currentItem.type
        historyItem.private = currentItem.private
        historyItem.GPS_tracking = currentItem.GPS_tracking
        historyItem.place = currentItem.place
        return historyItem
    }


    private fun deleteItem(event: EventItem) {
        val builder = AlertDialog.Builder(mCtx)
        builder.setMessage("Jste si jisti, že chcete smazat tuto naplánovanou událost?")
        builder.setPositiveButton("SMAZAT") { dialog, which ->
            val db = FirebaseDatabase.getInstance()
            val myRef = db.getReference("Events")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child(event.id!!)
            myRef.removeValue()
            eventsList.clear()
        }
        builder.setNeutralButton("ZRUŠIT") { dialog, which ->  }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun getProgressString(inProgress: Boolean?): CharSequence? {
        return when (inProgress) {
            true -> "Právě probíhá"
            else -> ""
        }
    }

    private fun getGPSstring(gpsTracking: Boolean?): CharSequence? {
        return when (gpsTracking) {
            true -> "Sledování pomocí GPS zapnuto"
            else -> "Sledování pomocí GPS vypnuto"
        }
    }

    private fun getPrivacyString(private: Boolean?): CharSequence? {
        return when (private) {
            true -> "Soukromá událost"
            else -> "Veřejná událost"
        }
    }
}