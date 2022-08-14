package com.example.filmshelper.presentation.screens

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.filmshelper.appComponent
import com.example.filmshelper.databinding.TrailerActivityBinding
import com.example.filmshelper.presentation.screens.mainFragment.MainFragmentViewModel
import com.example.filmshelper.presentation.screens.mainFragment.MainFragmentViewModelFactory
import com.example.filmshelper.utils.ViewState
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import javax.inject.Inject

class TrailerActivity : AppCompatActivity() {

    private lateinit var binding: TrailerActivityBinding
    private val viewModel: MainFragmentViewModel by viewModels {
        factory.create()
    }


    @Inject
    lateinit var factory: MainFragmentViewModelFactory.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appComponent.inject(this)

        binding = TrailerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        val filmId = intent.getStringExtra("filmId").toString()

        viewModel.getYoutubeTrailerById(filmId)

        viewModel.youtubeTrailer.observe(this) {
            when (it) {
                is ViewState.Error -> {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
                ViewState.Loading -> {

                }
                ViewState.NoData -> {

                }
                is ViewState.Success -> {
                    binding.youtubePlayer.addYouTubePlayerListener(object :
                        AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {

                            youTubePlayer.cueVideo(it.data.videoId, 0F)
                        }
                    })
                }
            }
        }


    }
}