package com.example.filmshelper.presentation.adapters

import android.util.Log
import com.example.filmshelper.R
import com.example.filmshelper.data.models.DisplayableItem
import com.example.filmshelper.data.models.filmDetails.Actor
import com.example.filmshelper.data.models.filmDetails.CreatorList
import com.example.filmshelper.data.models.filmDetails.Director
import com.example.filmshelper.data.models.filmDetails.Similar
import com.example.filmshelper.databinding.CastItemListBinding
import com.example.filmshelper.databinding.CreatorsListItemBinding
import com.example.filmshelper.databinding.SimilarItemListBinding
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.squareup.picasso.Picasso

class AdapterDelegatesDetails {

    fun castActorsAdapterDelegate() =
        adapterDelegateViewBinding<Actor, DisplayableItem, CastItemListBinding>(
            { layoutInflater, root -> CastItemListBinding.inflate(layoutInflater, root, false) }
        ) {
            bind {
                binding.apply {
                    textViewActorName.text = item.name
                    textViewCharacter.text = binding.root.context.getString(R.string.cast)
                    Picasso.get().load(item.image).fit()
                        .centerCrop().into(imageView3)
                }
            }
        }

    fun castCreatorsAdapterDelegate() =
        adapterDelegateViewBinding<CreatorList, DisplayableItem, CreatorsListItemBinding>(
            { layoutInflater, root -> CreatorsListItemBinding.inflate(layoutInflater, root, false) }
        ) {
            bind {
                binding.apply {
                    textViewActorName.text = item.name
                    textViewCharacter.text = binding.root.context.getString(R.string.director)
                }
            }
        }

    fun castDirectorsAdapterDelegate() =
        adapterDelegateViewBinding<Director, DisplayableItem, CreatorsListItemBinding>(
            { layoutInflater, root -> CreatorsListItemBinding.inflate(layoutInflater, root, false) }
        ) {
            bind {
                binding.apply {
                    textViewActorName.text = item.name
                    textViewCharacter.text = binding.root.context.getString(R.string.director)
                }
            }
        }

    fun similarFilmsAdapterDelegate() =
        adapterDelegateViewBinding<Similar, DisplayableItem, SimilarItemListBinding>(
            { layoutInflater, root -> SimilarItemListBinding.inflate(layoutInflater, root, false) }
        ) {
            bind {
                Log.d("riko", it.size.toString())
                binding.apply {
                    textViewTitle.text = item.title
                    Picasso.get().load(item.image).fit()
                        .centerCrop().into(imageView4)
                }
            }
        }
}