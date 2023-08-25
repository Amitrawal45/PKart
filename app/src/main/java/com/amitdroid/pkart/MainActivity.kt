package com.amitdroid.pkart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.PopupMenu
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import com.amitdroid.pkart.activity.LoginActivity
import com.amitdroid.pkart.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var i=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(FirebaseAuth.getInstance().currentUser == null){
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        val navController = navHostFragment!!.findNavController()

        val popUpMenu = PopupMenu(this, null)
        popUpMenu.inflate(R.menu.bottom_nav)

        binding.bottomBar.setupWithNavController(popUpMenu.menu, navController)

      binding.bottomBar.onItemSelected = {

          when(it){
              0 ->{
                  i = 0;
                  navController.navigate(R.id.homeFragment)
              }
              1 -> i=1
              2 -> i=2

          }
      }

        navController.addOnDestinationChangedListener(object :
            NavController.OnDestinationChangedListener {
            override fun onDestinationChanged(
                controller: NavController,
                destination: NavDestination,
                arguments: Bundle?
            ) {

                Log.d("Destination", "ID: ${destination.id}")


                title = when (destination.id) {
                    R.id.moreFragment -> "My Dashboard"
                    R.id.cartFragment -> "My Cart"
                    else -> "P-Kart"
                }
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()

        if(i == 0){
            finish()
        }
    }
}
