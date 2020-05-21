package cz.cvut.fel.tlappka.events

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import cz.cvut.fel.tlappka.R

const val TAG = "MapsActivity"
private const val LOCATION_PERMISSION_REQUEST_CODE = 1234
private const val DEFAULT_ZOOM = 17F

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var mLocationPermissionGranted: Boolean = false
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient

    //widgets
    private lateinit var mSearchText: EditText
    private lateinit var mGps: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        mSearchText = findViewById(R.id.input_search)
        mGps = findViewById(R.id.icon_gps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {

        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show()
        mMap = googleMap

        /*
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney)) */

        getLocationPermission()

        if(mLocationPermissionGranted) {
            getDeviceLocation()
            mMap.isMyLocationEnabled = true //this adds a location button to the UI
            mMap.uiSettings.isMyLocationButtonEnabled = false //disables the default location icon
        }

        locateAddressInit()

    }

    private fun locateAddressInit() {
        //override enter next line key into confimation action
        mSearchText.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
                    event.action == KeyEvent.ACTION_DOWN ||
                    event.action == KeyEvent.KEYCODE_ENTER) {
                geoLocate()
                true
            }
            false
        }

        mGps.setOnClickListener( View.OnClickListener {
            getDeviceLocation()
        })

    }

    private fun geoLocate() {
        var searchStr: String = mSearchText.text.toString()
        var geocoder = Geocoder(this)
        var list: List<Address> = geocoder.getFromLocationName(searchStr, 1)
        if(list.size > 0) {
            var address: Address = list.get(0)

            Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show()

            customMoveCamera(LatLng(address.latitude, address.longitude), DEFAULT_ZOOM, address.getAddressLine(0))
        }

    }

    //TODO fix the map is blurry, only load the central part of the map
    private fun getDeviceLocation() {
        Toast.makeText(this, "Getting devices current location", Toast.LENGTH_SHORT).show()

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if(mLocationPermissionGranted) {
            var location: Task<Location> = mFusedLocationProviderClient.lastLocation
            location.addOnCompleteListener { task ->
                if(task.isSuccessful()) {
                    Toast.makeText(this, "Current location found", Toast.LENGTH_SHORT).show()
                    var currentLocation : Location = task.getResult() as Location
                    customMoveCamera(LatLng(currentLocation.latitude, currentLocation.longitude), DEFAULT_ZOOM, "My Location")
                } else {
                    Toast.makeText(this, "Unable to find current location", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun customMoveCamera(LatLng: LatLng, zoom: Float, title: String) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng, zoom))

        //code for dropping the pin
        //dropping the marker/ pin on the map
        var markerOptions: MarkerOptions = MarkerOptions().position(LatLng).title(title)
        mMap.clear() //replace current marker, do not create new ones
        mMap.addMarker(markerOptions)
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            //permission is granted
            mLocationPermissionGranted = true
            //mMap.setMyLocationEnabled(true)

            //mMap.isMyLocationEnabled = true
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE)
            }
            /*
            // Permission to access the location is missing. Show rationale and request permission
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE) */
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



}
