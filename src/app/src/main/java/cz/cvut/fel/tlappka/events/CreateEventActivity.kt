package cz.cvut.fel.tlappka.events

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
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
import cz.cvut.fel.tlappka.profile.ProfileFragmentViewModel
import kotlinx.android.synthetic.main.activity_create_event.*
import java.sql.Time
import java.util.*


class CreateEventActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
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
    private val plannedFragment = EventPlannedFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_event)
        val toolbar : Toolbar = binding.createEventToolbar as Toolbar
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
        fillProfilePhoto()
        setRadioButtonsListeners()
        dateAndTime()
        binding.fab.setOnClickListener {
            doneButton()
        }
        binding.locationText.setOnClickListener{
            val mapIntent = Intent(this, MapsActivity::class.java)
            startActivity(mapIntent)
        }

        val imm: InputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(eventName.getWindowToken(), 0)

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
        } else {
            addEvent()
            Toast.makeText(applicationContext, "Událost vytvořena", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun addEvent() {
        val name: String = event_name.text.toString()
        val inProgress: Boolean = radio_now.isChecked
        val date: Date
        val time: Time
        val text: String? = event_description.text.toString()
        val type: String
        if (radio_walk.isChecked) {
            type = "Procházka"
        } else {
            type = eventType.text.toString()
        }
        val private: Boolean = radio_private.isChecked
        var newEvent = EventItem(name, inProgress, text, type, private, false)

        FirebaseDatabase.getInstance().getReference("Events")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).push().setValue(newEvent)

    }

    private fun dateAndTime() {
        val now = Calendar.getInstance()

        date.setOnClickListener {
            val datePicker = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    date.text = "$dayOfMonth/$month/$year"
                },
                now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))
            datePicker.datePicker.minDate = System.currentTimeMillis()
            datePicker.show()
        }
        

        time.setOnClickListener {
        val timePicker = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            time.text = "$hourOfDay:$minute"
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
