package cz.cvut.fel.tlappka.options

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cz.cvut.fel.tlappka.R
import kotlinx.android.synthetic.main.settings_list_item.view.*

class MyListAdapter(private val settingsList: List<String>) : RecyclerView.Adapter<MyListAdapter.MyViewHolder>() {

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val myTextView : TextView = itemView.settings_item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.settings_list_item, parent, false)
        return MyViewHolder(v)
    }
    override fun getItemCount(): Int {
        return settingsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = settingsList[position]
        holder.myTextView.text = currentItem
    }
}