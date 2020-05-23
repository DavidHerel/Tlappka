package cz.cvut.fel.tlappka.events

import android.app.AlertDialog
import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import cz.cvut.fel.tlappka.R
import kotlinx.android.synthetic.main.history_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HistoryAdapter(private var historyList: ArrayList<HistoryItem>, private val mCtx: Context) : RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventName: TextView = itemView.history_list_name
        val date: TextView = itemView.history_list_begin_date
        val time: TextView = itemView.history_list_time
        val type: TextView = itemView.history_list_type
        val endTime: TextView = itemView.history_list_end
        val privacy: TextView = itemView.history_list_privacy
        val GPSTracking: TextView = itemView.history_list_GPS
        val description_text: TextView = itemView.history_list_description
        val options: TextView = itemView.history_options
        val icon: ImageView = itemView.history_icon
        val location: TextView = itemView.history_location
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = historyList[position]
        holder.eventName.text = currentItem.name
        holder.date.text = currentItem.date.toString()
        holder.time.text = currentItem.start_time
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
        holder.endTime.text = getTime(currentItem.end_time)
        holder.options.setOnClickListener(View.OnClickListener {
            val popup =
                PopupMenu(mCtx, holder.options)
            popup.inflate(R.menu.history_menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.delete_history_item -> {
                        deleteItem(currentItem)
                        true
                    }
                    else -> false
                }
            }
            //displaying the popup
            popup.show()
        })
    }

    private fun deleteItem(currentItem: HistoryItem) {
        val builder = AlertDialog.Builder(mCtx)
        builder.setMessage("Jste si jisti, že chcete smazat tuto událost z historie?")
        builder.setPositiveButton("SMAZAT") { dialog, which ->
            val db = FirebaseDatabase.getInstance()
            val myRef = db.getReference("History")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child(currentItem.id!!)
            myRef.removeValue()
            historyList.clear()
        }
        builder.setNeutralButton("ZRUŠIT") { dialog, which ->  }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun getTime(time: Long?): CharSequence? {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time!!
        val formatter = SimpleDateFormat("dd MMM YYYY  HH:mm", Locale.US)
        return formatter.format(calendar.time)
    }

    private fun getGPSstring(gpsTracking: Boolean?): CharSequence? {
        return when (gpsTracking) {
            true -> "Sledování pomocí GPS bylo zapnuto"
            else -> "Sledování pomocí GPS bylo vypnuto"
        }
    }

    private fun getPrivacyString(private: Boolean?): CharSequence? {
        return when (private) {
            true -> "Soukromá událost"
            else -> "Veřejná událost"
        }
    }
}