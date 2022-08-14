package com.example.filmshelper.presentation.adapters

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.navigation.findNavController
import com.example.filmshelper.R
import com.example.filmshelper.data.models.DisplayableItem
import com.example.filmshelper.data.models.nowShowingMovies.ItemNowShowingMovies
import com.example.filmshelper.data.models.popular.popularMovies.ItemPopularMovies
import com.example.filmshelper.data.models.popular.popularTvShows.ItemPopularTvShows
import com.example.filmshelper.databinding.NowShowingItemListBinding
import com.example.filmshelper.databinding.PopularItemListBinding
import com.example.filmshelper.databinding.PopularTvShowItemListBinding
import com.example.filmshelper.presentation.screens.mainFragment.MainFragmentDirections
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.squareup.picasso.Picasso

class AdapterDelegatesHome {

    fun popularFilmsAdapterDelegate(itemDetailFragmentContainer: View?) =
        adapterDelegateViewBinding<ItemPopularMovies, DisplayableItem, PopularItemListBinding>(
            { layoutInflater, root -> PopularItemListBinding.inflate(layoutInflater, root, false) }
        ) {
            this.itemView.setOnClickListener { view ->
                val action =
                    MainFragmentDirections.actionMainFragmentToFilmDetailsFragment(item.movieId)
                val bundle = Bundle()
                bundle.putString("filmId", item.movieId)
                if (itemDetailFragmentContainer != null) {
                    itemDetailFragmentContainer.findNavController()
                        .navigate(R.id.filmDetailsFragmentTable, bundle)
                } else {
                    view.findNavController().navigate(action)
                }

            }
            bind {
                binding.apply {
                    ViewCompat.setTransitionName(binding.imageView2, "image${item.movieId}")
                    textViewTitle.text = item.title
                    textViewRating.text =
                        root.context.getString(R.string.rating_imdb, item.imDbRating)
                    textViewYear.text = item.description

                    Picasso.get().load(item.image).fit()
                        .centerCrop().into(binding.imageView2)
                }
            }
        }

    fun popularTvShowsAdapterDelegate(itemDetailFragmentContainer: View?) =
        adapterDelegateViewBinding<ItemPopularTvShows, DisplayableItem, PopularTvShowItemListBinding>(
            { layoutInflater, root ->
                PopularTvShowItemListBinding.inflate(
                    layoutInflater,
                    root,
                    false
                )
            }
        ) {

            this.itemView.setOnClickListener { view ->
                val action =
                    MainFragmentDirections.actionMainFragmentToFilmDetailsFragment(item.movieId)

                val bundle = Bundle()
                bundle.putString("filmId", item.movieId)
                if (itemDetailFragmentContainer != null) {
                    itemDetailFragmentContainer.findNavController()
                        .navigate(R.id.filmDetailsFragmentTable, bundle)
                } else {
                    view.findNavController().navigate(action)
                }
            }

            bind {
                binding.apply {
                    textViewTitle.text = item.title
                    textViewRating.text =
                        binding.root.context.getString(R.string.rating_imdb, item.imDbRating)
                    textViewYear.text = item.description

                    Picasso.get().load(item.image).fit()
                        .centerCrop().into(imageView2)
                }
            }
        }

    fun nowShowingFilmsAdapterDelegate() =
        adapterDelegateViewBinding<ItemNowShowingMovies, DisplayableItem, NowShowingItemListBinding>(
            { layoutInflater, root ->
                NowShowingItemListBinding.inflate(
                    layoutInflater,
                    root,
                    false
                )
            }
        ) {
            bind {
                binding.apply {
                    textViewFilmName.text = item.title
                    textViewReleaseDate.text = item.runtimeStr
                    Picasso.get().load(item.image).fit()
                        .centerCrop().into(imageView)
                }
            }
        }
}