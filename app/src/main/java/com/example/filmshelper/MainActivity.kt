package com.example.filmshelper

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.filmshelper.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    private var auth: FirebaseAuth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
        /*binding.bottomNavigationView.setOnItemSelectedListener {
            Log.d("riko", it.toString())
            when(it.itemId){
                R.id.profileSignUpFragment ->{
                    return@setOnItemSelectedListener true
                }
            }
            false

        }*/

        navController.addOnDestinationChangedListener{ controller, destination, bundle ->
            when(destination.id){
                R.id.profileFragment -> {
                    if (auth.currentUser == null){
                        controller.navigate(R.id.action_profileFragment_to_profileSignUpFragment)
                    }
                }
            }

        }
    }

}