package com.example.sosappemergency

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sosappemergency.databinding.FragmentHomeBinding
import com.example.sosappemergency.utils.ShakeService



class HomeFragment : Fragment() {
    private lateinit var fragmentHomeBinding: FragmentHomeBinding

    var flag:Int = 1
    private val FORCE_THRESHOLD = 10000;
    private val TIME_THRESHOLD = 75;
    private val SHAKE_TIMEOUT = 500;
    private val SHAKE_DURATION = 150;
    private val SHAKE_COUNT = 1;

   private var sensorManager: SensorManager? = null
   private var accelerometer:Sensor? = null
    private var shakeCount = 0
    private var lastShake:Long = 0
    private var lastForce:Long = 0
    private var lastTime:Long = 0
    private var lastX:Float = -1.0f
    private var lastY:Float = -1.0f
    private var lastZ:Float = -1.0f





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){

            val PERMISSION_ALL = 1
            val PERMISSIONS =
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.SEND_SMS)
            ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, PERMISSION_ALL)

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentHomeBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false )
        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHomeBinding.imageView2.setOnClickListener {
            if (flag==1) {
                Toast.makeText(requireContext(), "ACTIVATED!", Toast.LENGTH_LONG).show()
                //startActivity(Intent(requireContext(), MapsActivity::class.java))
                                                                                          requireContext().startService(
                    Intent(
                        requireContext(),
                        ShakeService::class.java
                    )

               )
                fragmentHomeBinding.textView2.setText("Service Started")
                flag = 0
            }else{
                Toast.makeText(requireContext(), "DEACTIVATED!", Toast.LENGTH_LONG).show()
                requireContext().stopService(Intent(requireContext(), ShakeService::class.java))
                fragmentHomeBinding.textView2.setText("Service Stopped")
                flag = 1
            }
        }
        fragmentHomeBinding.imageView3.setOnClickListener {
            val popupMenu: PopupMenu = PopupMenu(requireContext(), it)
            popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem->
                when(menuItem.itemId){
                    R.id.manage_contacts->{
                        findNavController().navigate(R.id.action_homeFragment_to_manageContactsFragment)
                        return@setOnMenuItemClickListener true
                    }
                    R.id.info->{
                        findNavController().navigate(R.id.action_homeFragment_to_aboutFragment)
                        return@setOnMenuItemClickListener true
                    }
                    else->
                        return@setOnMenuItemClickListener false
                }

            }
            popupMenu.show()


        }

    }

    override fun onPause() {
        super.onPause()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
       inflater.inflate(R.menu.menu, menu)
    }



    companion object {


        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {

            }
    }
}