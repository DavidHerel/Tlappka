package cz.cvut.fel.tlappka.events

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.location.LocationManager
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import cz.cvut.fel.tlappka.MainActivity
import cz.cvut.fel.tlappka.R
import cz.cvut.fel.tlappka.databinding.ActivityCreateEventBinding
import cz.cvut.fel.tlappka.events.tracking.WalkEventActivity
import cz.cvut.fel.tlappka.profile.ProfileFragmentViewModel
import kotlinx.android.synthetic.main.activity_create_event.*
import kotlinx.android.synthetic.main.activity_create_event.location_text
import kotlinx.android.synthetic.main.event_item.*
import java.lang.Exception
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


private lateinit var eventCreated: EventItem

class CreateEventActivity : AppCompatActivity() {

    private val LOCATION_REQUEST_CODE = 100
    private val profileFragmentViewModel: ProfileFragmentViewModel by viewModels()
    private lateinit var binding: ActivityCreateEventBinding
    private lateinit var date : TextView
    private lateinit var time: TextView
    private lateinit var dateText: TextView
    private lateinit var timeText: TextView
    private lateinit var walkRadio: RadioButton
    private lateinit var otherRadio: RadioButton
    private lateinit var nowRadio: RadioButton
    private lateinit var laterRadio: RadioButton
    private lateinit var eventType: EditText
    private lateinit var eventName: EditText
    private lateinit var locationText: EditText
    private var dateChosen: Boolean = false
    private var timeChosen: Boolean = false
    private var locationChosen: Boolean = false
    private var typeChosen: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_event)
        var toolbar : Toolbar = binding.createEventToolbar as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(R.string.create_event_toolbar_text)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp)
        date = findViewById(R.id.custom_date)
        time = findViewById(R.id.custom_time)
        walkRadio = findViewById(R.id.radio_walk)
        otherRadio = findViewById(R.id.radio_other)
        eventType = findViewById(R.id.custom_type_event)
        nowRadio = findViewById(R.id.radio_now)
        laterRadio = findViewById(R.id.radio_choose_time)
        dateText = findViewById(R.id.custom_date)
        timeText = findViewById(R.id.custom_time)
        eventName = findViewById(R.id.event_name)
        locationText = findViewById(R.id.location_text)
        fillProfilePhoto()
        setRadioButtonsListeners()
        dateAndTime()
        binding.fab.setOnClickListener {
            doneButton()
        }
        binding.locationText.setOnClickListener{
            val lm = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            var gps_enabled = false
            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
            } catch (ex: Exception) {}
            if (gps_enabled) {
                val mapIntent = Intent(this, MapsActivity::class.java)
                startActivityForResult(mapIntent, LOCATION_REQUEST_CODE)
                locationChosen = true
            } else {
                Toast.makeText(this, "Pro výběr místa zapněte sledování polohy", Toast.LENGTH_LONG).show()
            }
        }

        binding.customTypeEvent.setOnFocusChangeListener { v, hasFocus ->
            typeChosen = true
        }
        val imm: InputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(eventName.getWindowToken(), 0)
    }

    //handle result from map search
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                //initialize place
                var place = data?.extras?.getString("address")
                //set the address to editText
                binding.locationText.setText(place)
                Toast.makeText(applicationContext, "Address updates to: " + place, Toast.LENGTH_SHORT).show()
            } else if (resultCode == Activity.RESULT_CANCELED) {
                //handle the error
                Toast.makeText(applicationContext, "Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // edit text loses focus when is clicked anywhere else (works for any edit text in activity)
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            if (event.action == MotionEvent.ACTION_DOWN) {
                val v: View? = currentFocus
                if (v is EditText){
                    val outRect: Rect = Rect()
                    v.getGlobalVisibleRect(outRect)
                    if (!outRect.contains(event.getRawX().toInt(), event.getRawY().toInt())) {
                        v.clearFocus()
                        val inputMethodManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
                    }

                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun doneButton() {
        val eventName = event_name.text.toString()
        if (eventName.isEmpty()) {
            Toast.makeText(applicationContext, "Vyplňte název události", Toast.LENGTH_LONG).show()
            return
        } else if (radio_choose_time.isChecked && (!dateChosen || !timeChosen)) {
//            (custom_date.text.toString() == "Vybrat datum" || custom_time.text.toString().equals("Vybrat čas"))) {
            Toast.makeText(applicationContext, "Vyplňte datum a čas události", Toast.LENGTH_LONG).show()
            return
        } else if (radio_other.isChecked && !typeChosen) {
//            (custom_date.text.toString() == "Vybrat datum" || custom_time.text.toString().equals("Vybrat čas"))) {
        Toast.makeText(applicationContext, "Vyplňte typ události", Toast.LENGTH_LONG).show()
        return
        }
        else {
            addEvent()
            //Toast.makeText(applicationContext, "Událost vytvořena", Toast.LENGTH_LONG).show()
            if (walkRadio.isChecked() && nowRadio.isChecked() && binding.locationSwitch.isChecked()) {
                Toast.makeText(applicationContext, "Zacina prochazka", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, WalkEventActivity::class.java))
            } else {
                Toast.makeText(applicationContext, "Událost vytvořena", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }


    private fun addEvent() {
        val name: String = event_name.text.toString()
        val inProgress: Boolean = radio_now.isChecked
        var date: String = custom_date.text.toString()
        var time: String = custom_time.text.toString()
        val text: String? = event_description.text.toString()
        val type: String
        if (radio_walk.isChecked) {
            type = "Procházka"
        } else {
            type = eventType.text.toString()
        }

        if (inProgress) {
            val now = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.US)
            val timeFormat = SimpleDateFormat("HH:mm", Locale.US)
            date = dateFormat.format(now.time)
            time = timeFormat.format(now.time)
        }
        val private: Boolean = radio_private.isChecked
        val GPS_tracking: Boolean = location_switch.isChecked
        val userEvents = FirebaseDatabase.getInstance().getReference("Events")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
        val id = userEvents.push().key!!
        val location: String
        if (locationChosen) {
            location = location_text.text.toString()
        } else {
            location = ""
        }
        val newEvent = EventItem(id, name, inProgress, date, time, text, type, private, GPS_tracking, location)
        userEvents.child(id).setValue(newEvent)


        if (!inProgress) {
            val alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val notifIntent = Intent(this, AlarmReceiver::class.java)
            notifIntent.putExtra("eventId", id)
            val calendar = Calendar.getInstance()
            val dateItems = date.split(" ")
            val timeItem = time.split(":")
            calendar.set(Calendar.YEAR, dateItems[2].toInt())
            calendar.set(Calendar.MONTH, getMonth(dateItems[1]))
            calendar.set(Calendar.DAY_OF_MONTH, dateItems[0].toInt())
            calendar.set(Calendar.HOUR_OF_DAY, timeItem[0].toInt())
            calendar.set(Calendar.MINUTE, timeItem[1].toInt())
            calendar.set(Calendar.SECOND, 0)
            val broadcast = PendingIntent.getBroadcast(
                this,
                100,
                notifIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, broadcast)
        }
    }

    private fun getMonth(month: String): Int {
        return when (month) {
            "Jan" -> {0}
            "Feb" -> {1}
            "Mar" -> {2}
            "Apr" -> {3}
            "May" -> {4}
            "Jun" -> {5}
            "Jul" -> {6}
            "Aug" -> {7}
            "Sep" -> {8}
            "Oct" -> {9}
            "Nov" -> {10}
            else -> {11}
        }
    }

    private fun dateAndTime() {
        val now = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.US)
        val timeFormat = SimpleDateFormat("HH:mm", Locale.US)

        date.setOnClickListener {
            if (timeChosen) {
                val datePicker = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        val dateCalendar = Calendar.getInstance()
                        dateCalendar.set(Calendar.YEAR, year)
                        dateCalendar.set(Calendar.MONTH, month)
                        dateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        date.text = dateFormat.format(dateCalendar.time)
                        dateChosen = true
                    },
                    now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
                )
                val timeItem = custom_time.text.split(":")
                val calendar = Calendar.getInstance()
                val now = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, timeItem[0].toInt())
                calendar.set(Calendar.MINUTE, timeItem[1].toInt())
                if (calendar.before(now)) {
                    datePicker.datePicker.minDate = System.currentTimeMillis() + (60*60*24*1000)
                } else {
                    datePicker.datePicker.minDate = System.currentTimeMillis()
                }
                datePicker.show()
            } else {
                Toast.makeText(this, "Nejprve vyberte čas", Toast.LENGTH_SHORT).show()
            }
        }


        time.setOnClickListener {
        val timePicker = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            val timeCalendar = Calendar.getInstance()
            timeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            timeCalendar.set(Calendar.MINUTE, minute)
            time.text = timeFormat.format(timeCalendar.time)
            timeChosen = true
        },
            now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true)
            timePicker.show()
        }
    }


    private fun setRadioButtonsListeners() {
        walkRadio.setOnClickListener {
            eventType.visibility = View.INVISIBLE
        }
        otherRadio.setOnClickListener {
            eventType.visibility = View.VISIBLE
        }


        nowRadio.setOnClickListener {
            dateText.visibility = View.INVISIBLE
            timeText.visibility = View.INVISIBLE
        }
        laterRadio.setOnClickListener {
            dateText.visibility = View.VISIBLE
            timeText.visibility = View.VISIBLE
        }
    }

    private fun fillProfilePhoto(){
        profileFragmentViewModel.getUri().observe(this) { uri ->
            // update UI
            Picasso.with(this).load(uri).into(profile_icon_event)
        }
    }



    override fun onBackPressed(): Unit {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
            //additional code
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}
