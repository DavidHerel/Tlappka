package cz.cvut.fel.tlappka.events

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import cz.cvut.fel.tlappka.R
import kotlinx.android.synthetic.main.fragment_event_planned.*
import java.time.LocalDate
import java.time.LocalTime

class EventPlannedFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_planned, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getPlannedEvents()
    }

    private fun getPlannedEvents() {

        val eventsFolder = FirebaseDatabase.getInstance().getReference("Events")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)

        val listener = object : ValueEventListener {
            var eventList = mutableListOf<EventItem>()

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Planned events", "loadPost:onCancelled", databaseError.toException())
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataSnapshot.children.forEach {

                    val text = it.child("description").getValue(String::class.java)
                    val gpsTracking = it.child("gps_tracking").getValue(Boolean::class.java)
                    val inProgress = it.child("in_progress").getValue(Boolean::class.java)
                    val name = it.child("name").getValue(String::class.java)
                    val private = it.child("private").getValue(Boolean::class.java)
                    val type = it.child("type").getValue(String::class.java)
                    val date = it.child("date").getValue(String::class.java)
                    val time = it.child("time").getValue(String::class.java)

                    val event = EventItem(name, inProgress, date, time, text, type, private, gpsTracking)
                    eventList.add(event)
                }
                recycler_planned_events.layoutManager = LinearLayoutManager(activity)
                recycler_planned_events.adapter = EventsAdapter(eventList, requireActivity())
            }
        }

        eventsFolder.addValueEventListener(listener)
    }
}


