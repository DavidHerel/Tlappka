package cz.cvut.fel.tlappka.events

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import cz.cvut.fel.tlappka.R
import kotlinx.android.synthetic.main.fragment_event_history.*
import kotlinx.android.synthetic.main.fragment_event_planned.*

class EventHistoryFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_history, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getHistoryEvents()
    }

    private fun getHistoryEvents() {
        val historyFolder = FirebaseDatabase.getInstance().getReference("History")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
        val listener = object : ValueEventListener {
            var historyList = arrayListOf<HistoryItem>()

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Planned events", "loadPost:onCancelled", databaseError.toException())
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                historyList.clear()
                dataSnapshot.children.forEach {
                    val id = it.child("id").getValue(String::class.java)
                    val text = it.child("description").getValue(String::class.java)
                    val gpsTracking = it.child("gps_tracking").getValue(Boolean::class.java)
                    val name = it.child("name").getValue(String::class.java)
                    val private = it.child("private").getValue(Boolean::class.java)
                    val type = it.child("type").getValue(String::class.java)
                    val date = it.child("date").getValue(String::class.java)
                    val time = it.child("start_time").getValue(String::class.java)
                    val endTime = it.child("end_time").getValue(Long::class.java)
                    val place = it.child("place").getValue(String::class.java)

                    val event = HistoryItem(id, name, date, time, endTime, text, type, private, gpsTracking, place)
                    historyList.add(event)

                }
                if (historyList.isEmpty()) {
                    empty_history_text.visibility = View.VISIBLE
                }
                else {
                    empty_history_text.visibility = View.GONE
                }
                recycler_history_events.layoutManager = LinearLayoutManager(activity)
                recycler_history_events.adapter = HistoryAdapter(historyList, requireActivity())
            }
        }

        historyFolder.addValueEventListener(listener)
    }
}
