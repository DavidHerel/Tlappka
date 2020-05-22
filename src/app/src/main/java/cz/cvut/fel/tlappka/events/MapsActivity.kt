package cz.cvut.fel.tlappka.events

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import cz.cvut.fel.tlappka.R
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback /*, OnConnectionFailedListener*/ {

    private val TAG = "MapsActivity"
    private val LOCATION_PERMISSION_REQUEST_CODE = 1234
    private val DEFAULT_ZOOM = 17F
    private val API_KEY : String = "AIzaSyDSK2duzQcrhgWukFMHU5B14SdbThL6jiY"

    private lateinit var mMap: GoogleMap
    private var mLocationPermissionGranted: Boolean = false
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var placesClient : PlacesClient
    private lateinit var selectedAddress: Address

    //widgets
    private lateinit var mGps: ImageView
    private lateinit var autocompleteSupportFragment: AutocompleteSupportFragment
    private lateinit var doneButton : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        mGps = findViewById(R.id.icon_gps)
        doneButton = findViewById(R.id.button_maps)

        doneButton.setOnClickListener(View.OnClickListener {
            val data = Intent()
            data.putExtra("address", selectedAddress.getAddressLine(0).toString())
            setResult(Activity.RESULT_OK, data)
            finish()
        })

        // Initialize Google Places API library
        if(!Places.isInitialized()) {
            Places.initialize(applicationContext, API_KEY)
        }
        placesClient = Places.createClient(this)

        //Initialize AutocompleteSupportFragment
        autocompleteSupportFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment
        /*
        //specify what kind of place the user can type in
        autocompleteSupportFragment.setTypeFilter(TypeFilter.ADDRESS)
        //location biasing to improve the predictions
        autocompleteSupportFragment.setLocationBias(RectangularBounds.newInstance(LATLNG_BOUNDS))
        autocompleteSupportFragment.setCountries("IN") */

        autocompleteSupportFragment.setHint("Vyhledat adresu")

        //Specify which details of the place we will need about the selected place
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME))

        autocompleteSupportFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                //get info about the selected place
                Log.i(TAG, "Place: " + place.name + ", " + place.id)
                val location = Location(place.name)
                location.latitude = place.latLng!!.latitude
                location.longitude = place.latLng!!.longitude
                //Toast.makeText(applicationContext, "Moved to a place: " + place?.name + " " + place.id, Toast.LENGTH_SHORT).show()
                //customMoveCamera(LatLng(place.latLng!!.latitude, place.latLng!!.longitude), DEFAULT_ZOOM, place.name.toString())

                //update the name in the searchbox, update current marker position and recenter map
                geoLocate(location)
            }
            override fun onError(p0: Status) {
                Log.i(TAG, "An error occurred: $p0")
            }
        })

        //obtain the SupportMapFragment and get notified when the map is ready to be used
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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

        mGps.setOnClickListener( View.OnClickListener {
            getDeviceLocation()
        })

    }

    private fun geoLocate(searchLoc: Location) {
        val geocoder = Geocoder(this)
        val list: List<Address> = geocoder.getFromLocation(searchLoc.latitude, searchLoc.longitude, 1)
        if(list.size > 0) {
            val address: Address = list.get(0)
            Toast.makeText(this, "Moved to address:\n" + address.getAddressLine(0), Toast.LENGTH_SHORT).show()
            customMoveCamera(LatLng(address.latitude, address.longitude), DEFAULT_ZOOM, address.getAddressLine(0))
            autocompleteSupportFragment.setText(address.getAddressLine(0))
            selectedAddress = address
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
                    val currentLocation : Location = task.getResult() as Location
                    geoLocate(currentLocation)
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

}
