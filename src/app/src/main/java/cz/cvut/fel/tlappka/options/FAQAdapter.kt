package cz.cvut.fel.tlappka.options

import android.content.Context
import android.graphics.ColorSpace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cz.cvut.fel.tlappka.R
import kotlinx.android.synthetic.main.faq_item.view.*

class FAQAdapter(private val FAQList: List<FAQItem>) :
    RecyclerView.Adapter<FAQAdapter.FAQViewHolder>() {

    class FAQViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val questionView : TextView = itemView.question
        val answerView : TextView = itemView.answer
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.faq_item, parent, false)
        return FAQViewHolder(v)
    }

    override fun getItemCount(): Int {
        return FAQList.size
    }

    override fun onBindViewHolder(holder: FAQViewHolder, position: Int) {
        val currentItem = FAQList[position]
        holder.questionView.text = currentItem.q
        holder.answerView.text = currentItem.a
    }
}