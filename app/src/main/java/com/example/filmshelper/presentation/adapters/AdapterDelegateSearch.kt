package com.example.filmshelper.presentation.adapters

import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import com.example.filmshelper.R
import com.example.filmshelper.data.models.DisplayableItem
import com.example.filmshelper.data.models.allFilmsWithQuery.ItemFilmsWithQuery
import com.example.filmshelper.databinding.ItemSearchFilmsBinding
import com.example.filmshelper.presentation.screens.searchFragments.SearchFragmentDirections
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.squareup.picasso.Picasso

class AdapterDelegateSearch {
    fun filmsWithQueryAdapterDelegate(itemDetailFragmentContainer: View?) =
        adapterDelegateViewBinding<ItemFilmsWithQuery, DisplayableItem, ItemSearchFilmsBinding>(
            { layoutInflater, parent ->
                ItemSearchFilmsBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            }
        )
        {
            this.itemView.setOnClickListener { view ->
                val action =
                    SearchFragmentDirections.actionSearchFragmentToFilmDetailsFragment(item.id)

                val bundle = Bundle()
                bundle.putString("filmId", item.id)
                if (itemDetailFragmentContainer != null) {
                    itemDetailFragmentContainer.findNavController()
                        .navigate(R.id.filmDetailsFragmentTable, bundle)
                } else {
                    view.findNavController().navigate(action)
                }

            }
            bind {
                binding.textViewRating.text = item.imDbRating
                Picasso.get().load(item.image).fit()
                    .centerCrop().into(binding.imageView)
            }
        }
}