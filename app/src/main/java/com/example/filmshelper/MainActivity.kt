package com.example.filmshelper

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.example.filmshelper.databinding.ActivityMainBinding
import com.example.filmshelper.presentation.screens.mainFragment.updateDataWorker.UpdateDataWorker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit


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

        navController.addOnDestinationChangedListener { controller, destination, bundle ->
            when (destination.id) {
                R.id.profileFragment -> {
                    if (auth.currentUser == null) {
                        controller.navigate(R.id.action_profileFragment_to_profileSignUpFragment)
                    }
                }
            }

        }

        createWorkManager()

    }

    private fun createWorkManager() {

        val data = Data.Builder().putInt("NOTIFICATION_ID", 0).build()
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_ROAMING)
            .setRequiresStorageNotLow(true)
            .build()
        val myWorkRequest = OneTimeWorkRequest.Builder(UpdateDataWorker::class.java)
            .setInitialDelay(30000, TimeUnit.MILLISECONDS).setInputData(data).build()

        WorkManager.getInstance(this).enqueue(myWorkRequest)
    }

}