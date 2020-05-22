package cz.cvut.fel.tlappka.events

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import cz.cvut.fel.tlappka.MainActivity
import cz.cvut.fel.tlappka.R

public class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationIntent = Intent(context, MainActivity::class.java)
        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addParentStack(MainActivity::class.java)
        stackBuilder.addNextIntent(notificationIntent)

        val pendingIntent = stackBuilder.getPendingIntent(100, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(context)

        val notification = builder.setContentTitle("Začátek události!")
            .setContentText("Začíná Vám naplánovaná událost")
            .setTicker("New Message Alert!")
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_logo)
            .setContentIntent(pendingIntent).build()

        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0,notification)

//        var eventItem = intent?.getSerializableExtra("event") as EventItem
//        eventItem.in_progress = true
//        FirebaseDatabase.getInstance().getReference("Events")
//            .child(FirebaseAuth.getInstance().currentUser!!.uid).push().setValue(eventItem)
    }

}