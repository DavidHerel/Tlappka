package cz.cvut.fel.tlappka.events

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.squareup.picasso.Picasso
import cz.cvut.fel.tlappka.MainActivity
import cz.cvut.fel.tlappka.R
import cz.cvut.fel.tlappka.databinding.ActivityCreateEventBinding
import cz.cvut.fel.tlappka.profile.ProfileFragmentViewModel
import kotlinx.android.synthetic.main.activity_create_event.*
import java.util.*


class CreateEventActivity : AppCompatActivity() {
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
        fillProfilePhoto()
        setRadioButtonsListeners()
        dateAndTime()
        binding.fab.setOnClickListener {
            doneButton()
        }
        binding.mapButton.setOnClickListener{
            val mapIntent = Intent(this, MapsActivity::class.java)
            startActivity(mapIntent)
        }
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //add event to list of planned events and show it in Events -> Planned
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

    //TODO - add functionality to "cancel" in upper left corner

    // the function below is copied from SettingsActivity, but here it does not work

    //    override fun onOptionsItemSelected(item: MenuItem): Boolean {
    //        val intent : Intent = NavUtils.getParentActivityIntent(this) as Intent
    //        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    //        NavUtils.navigateUpTo(this, intent)
    //        return true
    //    }
}
