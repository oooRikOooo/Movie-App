package com.example.filmshelper

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.example.filmshelper.databinding.ActivityMainBinding
import com.example.filmshelper.presentation.screens.mainFragment.sendFilmWorker.SendFilmWorker
import com.example.filmshelper.presentation.screens.mainFragment.updateDataWorker.UpdateDataWorker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*
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

        //startService()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            createWorkManagerSendNotificationWithMovie()
        } else {
            createAlarmManager()
        }

        createWorkManagerUpdateData()
    }

    private fun startService() {
        val prefs =
            applicationContext.getSharedPreferences("isShowNotification", Context.MODE_PRIVATE)

        prefs.edit().putBoolean("isShowNotification", true).apply()

        if (getNotificationsState(applicationContext) && !BootService.isRunning) {

            val serviceIntent = Intent(applicationContext, BootService::class.java)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                applicationContext.startForegroundService(serviceIntent)
            } else {
                applicationContext.startService(serviceIntent)
            }
        }

    }

    private fun createWorkManagerUpdateData() {
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()

        dueDate.set(Calendar.HOUR_OF_DAY, 23)
        dueDate.set(Calendar.MINUTE, 0)
        dueDate.set(Calendar.SECOND, 0)

        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }

        val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis

        val data = Data.Builder().putInt("NOTIFICATION_ID", 0).build()
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val myWorkRequest = PeriodicWorkRequestBuilder<UpdateDataWorker>(
            12, TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS).setInputData(data).build()


        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "notification",
            ExistingPeriodicWorkPolicy.REPLACE,
            myWorkRequest
        )
    }

    private fun createWorkManagerSendNotificationWithMovie() {
        val data = Data.Builder().putInt("NOTIFICATION_FILM_ID", 10).build()
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val myWorkRequest = PeriodicWorkRequestBuilder<SendFilmWorker>(
            4, TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .setInitialDelay(30 * 60 * 1000, TimeUnit.MILLISECONDS).setInputData(data).build()


        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "notificationWithFilm",
            ExistingPeriodicWorkPolicy.REPLACE,
            myWorkRequest
        )
    }

    private fun createAlarmManager() {
        val intent = Intent(this, BootService::class.java)

        val pIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        val alarm = getSystemService(ALARM_SERVICE) as AlarmManager
        alarm.setRepeating(
            AlarmManager.RTC_WAKEUP,
            30 * 60 * 1000, //10 * 60 * 1000
            (4 * 60 * 60 * 1000).toLong(),//4 * 60 * 60 * 1000
            pIntent
        )
    }

    companion object {
        private const val IS_SHOW_NOTIFICATION = "isShowNotification"

        fun getNotificationsState(context: Context): Boolean {
            val pref = context.getSharedPreferences(
                IS_SHOW_NOTIFICATION,
                Context.MODE_PRIVATE
            )

            return pref.getBoolean(IS_SHOW_NOTIFICATION, false)
        }
    }


}