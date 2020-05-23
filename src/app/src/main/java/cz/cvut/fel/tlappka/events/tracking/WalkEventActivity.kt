package cz.cvut.fel.tlappka.events.tracking

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import cz.cvut.fel.tlappka.MainActivity
import cz.cvut.fel.tlappka.R
import java.util.concurrent.TimeUnit

class WalkEventActivity : AppCompatActivity(), OnMapReadyCallback {

    private val LOCATION_PERMISSION_REQUEST_CODE = 1234
    private val DEFAULT_ZOOM = 17F

    //private lateinit var binding: ActivityWalkEventBinding

    private lateinit var mMap: GoogleMap
    private var mLocationPermissionGranted: Boolean = false

    // FusedLocationProviderClient - Main class for receiving location updates.
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient

    // LocationRequest - Requirements for the location updates, i.e., how often you
    // should receive updates, the priority, etc.
    private lateinit var locationRequest: LocationRequest

    // LocationCallback - Called when FusedLocationProviderClient has a new Location.
    private lateinit var locationCallback: LocationCallback

    // used for updating the current time
    private lateinit var chronometer: MyChronometer

    private lateinit var autocompleteSupportFragment: AutocompleteSupportFragment
    private lateinit var pauseButton : ImageButton
    private lateinit var stopButton: ImageButton
    private lateinit var currentTime : TextView
    private lateinit var currentPace : TextView
    private lateinit var currentDistance : TextView

    private lateinit var t : Thread
    private var firstLoop: Boolean = true

    private lateinit var currentLocation : Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walk_event)

        //TODO fix toolbar
        //setSupportActionBar(R.id.activity_walk_toolbar as Toolbar)
        /*
        setSupportActionBar(R.id.activity_walk_toolbar as Toolbar)
        supportActionBar?.setTitle(R.string.title_activity_walk_event)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp) */
        /*
        binding = DataBindingUtil.setContentView(this, R.layout.activity_walk_event)
        val toolbar : Toolbar = binding.createEventToolbar as Toolbar
        setSupportActionBar(binding.activity_walk_toolbar) */

        pauseButton = findViewById(R.id.walk_pause_button)
        stopButton = findViewById(R.id.walk_stop_button)

        currentDistance = findViewById(R.id.walk_current_distance)
        currentPace = findViewById(R.id.walk_current_pace)
        currentTime = findViewById(R.id.walk_current_time)

        chronometer = MyChronometer(this)
        chronometer.setMsElapsed(1000)

        pauseButton.setOnClickListener(View.OnClickListener {
            if(!chronometer.isRunning) { //clicked on start button
                pauseButton.setImageResource(R.drawable.ic_pause_white_24dp)
                chronometer.start()
                if(firstLoop) {
                    startClock()
                    firstLoop = false
                }
            } else { //clicked on pause button
                pauseButton.setImageResource(R.drawable.ic_play_arrow_white_24dp)
                chronometer.stop()
            }
        })

        stopButton.setOnClickListener(View.OnClickListener {
            chronometer.stop()
            Toast.makeText(this, "Activity finished", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
        })

        //obtain the SupportMapFragment and get notified when the map is ready to be used
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_walk) as SupportMapFragment
        mapFragment.getMapAsync(this)

        /* //TODO fix current time updates
        Timer().schedule(object : TimerTask() {
            override fun run() {
                updateTime()
            }
        }, 1000) */

        /*
        locationRequest = LocationRequest().apply {
            // Sets the desired interval for active location updates. This interval is inexact. You
            // may not receive updates at all if no location sources are available, or you may
            // receive them less frequently than requested. You may also receive updates more
            // frequently than requested if other applications are requesting location at a more
            // frequent interval.
            interval = TimeUnit.SECONDS.toMillis(60)
            // Sets the fastest rate for active location updates. This interval is exact, and your
            // application will never receive updates more frequently than this value.
            fastestInterval = TimeUnit.SECONDS.toMillis(30)
            // Sets the maximum time when batched location updates are delivered. Updates may be
            // delivered sooner than this interval.
            maxWaitTime = TimeUnit.MINUTES.toMillis(2)

            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        } */

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {

        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show()
        mMap = googleMap

        //get permission to use location info from the user
        getLocationPermission()

        if(mLocationPermissionGranted) {
            getDeviceLocation()
            mMap.isMyLocationEnabled = true //this adds a location button to the UI
            mMap.uiSettings.isMyLocationButtonEnabled = false //disables the default location icon
        } else {
            //add a marker in Prague and move the camera
            val prague = LatLng(50.073658, 14.418540)
            customMoveCamera(prague, DEFAULT_ZOOM, "Praha")
        }

    }

    private fun getDeviceLocation() {
        //Toast.makeText(this, "Getting devices current location", Toast.LENGTH_SHORT).show()

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if(mLocationPermissionGranted) {
            val location: Task<Location> = mFusedLocationProviderClient.lastLocation
            location.addOnCompleteListener { task ->
                if(task.isSuccessful()) {
                    //Toast.makeText(this, "Current location found", Toast.LENGTH_SHORT).show()
                    currentLocation = task.getResult() as Location
                    customMoveCamera(LatLng(currentLocation.latitude, currentLocation.longitude), DEFAULT_ZOOM, "My location")
                } else {
                    //Toast.makeText(this, "Unable to find current location", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun customMoveCamera(LatLng: LatLng, zoom: Float, title: String) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng, zoom))

        //code for dropping the marker/ pin on the map
        val markerOptions: MarkerOptions = MarkerOptions().position(LatLng).title(title)
        mMap.clear() //replace current marker, do not create new ones
        mMap.addMarker(markerOptions)
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            //permission is granted
            mLocationPermissionGranted = true
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE)
            }

            // Permission to access the location is missing. Show rationale and request permission
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(50.07682455055432, 14.4195556640625), DEFAULT_ZOOM))

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    mMap.isMyLocationEnabled = true
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun updateTime() {
        val millis = chronometer.getMsElapsed()
        val curTime = String.format(
            "%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(millis),
            TimeUnit.MILLISECONDS.toMinutes(millis),
            TimeUnit.MILLISECONDS.toSeconds(millis)
        )
        currentTime.setText(curTime) //change clock to your textview
    }

    private fun startClock() {
        t = object : Thread() {
            override fun run() {
                try {
                    while (!isInterrupted) {
                        sleep(100)
                        runOnUiThread {
                            val millis = chronometer.getMsElapsed()
                            val curTime = String.format(
                                "%02d:%02d:%02d",
                                TimeUnit.MILLISECONDS.toHours(millis),
                                TimeUnit.MILLISECONDS.toMinutes(millis),
                                TimeUnit.MILLISECONDS.toSeconds(millis)
                            )
                            currentTime.setText(curTime) //change clock to your textview
                        }
                    }
                } catch (e: InterruptedException) {
                }
            }
        }
        t.start()
    }

}
