package com.example.sosappemergency

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.birjuvachhani.locus.Locus
import com.example.sosappemergency.data.Contact
import com.example.sosappemergency.viewmodels.ManageContactsViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception



class MapsActivity: AppCompatActivity() {
    private lateinit var viewModel: ManageContactsViewModel
    private lateinit var localList: List<Contact>
    private var liveLocation:Location? = null
    private var visited:Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_maps)
        getUserslocation()
        viewModel = ViewModelProvider(this).get(ManageContactsViewModel::class.java)
        viewModel.allContacts.observe(this, Observer { contactsList ->
            contactsList?.let {
                localList = it
            }

        })
        viewModel.userLocation.observe(this, Observer { location->
            liveLocation = location

        } )
        Locus.configure {
            rationaleTitle = getString(R.string.locus_rationale_title)
            rationaleTitle = getString(R.string.locus_rationale_title)
            blockedTitle = getString(R.string.locus_permission_blocked_title)
            blockedText = getString(R.string.locus_permission_blocked_message)
            resolutionTitle = getString(R.string.locus_location_resolution_title)
            resolutionText = getString(R.string.locus_location_resolution_message)
        }
        updateMap()
        sendTextMessages()



    }

    private fun getUserslocation() {
        Locus.getCurrentLocation(this) { result ->
            result.location?.let {
                liveLocation = it
                updateMap()
                viewModel.setLocation(result.location!!)
            }
            result.error?.let {
                Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun sendTextMessages() {
      Handler().postDelayed({
          if (this::localList.isInitialized) {
              try {
                  //if(this::)

                  val obj = SmsManager.getDefault()
                  if (localList.isNotEmpty()) {
                      Toast.makeText(this, "Sending emergency Text messages...", Toast.LENGTH_LONG)
                          .show()
                      getUserslocation()
                      Handler().postDelayed(
                          {
                              for (contact in localList) {
                                  if (liveLocation != null) { //check whether the user's location has been captured.
                                      obj.sendTextMessage(
                                          contact.phoneNumber,
                                          null,
                                          "help! I have been involved in an accident, my location is: https://maps.google.com/?ll=${liveLocation!!.latitude},${liveLocation!!.longitude}",
                                          null,
                                          null
                                      )
                                  } else { //if an error occurred, send the message all the same.
                                      obj.sendTextMessage(
                                          contact.phoneNumber,
                                          null,
                                          " help! I have been involved in an accident ",
                                          null,
                                          null
                                      )
                                  }
                              }
                          }, 3000
                      )

                  } else {
                      Toast.makeText(
                          this,
                          "Sorry, you have not set any emergency contacts.",
                          Toast.LENGTH_LONG
                      ).show()
                  }
              } catch (e:Exception){
                  Toast.makeText(this, "Crashed: exception ${e.message}", Toast.LENGTH_LONG).show()
              }}else{
              Toast.makeText(this, "Contacts list has not been initialized: exception", Toast.LENGTH_LONG).show()
              //sendTextMessages()
          }
      }, 1000)


    }
    private fun updateMap() {
        if (liveLocation != null) {
            val mapFragment = supportFragmentManager.findFragmentById(
                R.id.map
            ) as? SupportMapFragment
            mapFragment?.getMapAsync { googleMap ->
                googleMap.setMinZoomPreference(6.0f)
                val markerOptions = MarkerOptions()
                markerOptions.position(LatLng(liveLocation!!.latitude, liveLocation!!.longitude))
                googleMap.addMarker(markerOptions)
                googleMap.moveCamera(
                    CameraUpdateFactory.newLatLng(
                        LatLng(
                            liveLocation!!.latitude,
                            liveLocation!!.longitude
                        )
                    )
                )
                googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                googleMap.isBuildingsEnabled = false
                val uiSettings = googleMap.uiSettings
                uiSettings.setAllGesturesEnabled(false)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }
}

