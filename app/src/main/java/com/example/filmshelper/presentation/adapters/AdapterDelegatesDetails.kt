package com.example.filmshelper.presentation.adapters

import com.example.filmshelper.data.models.DisplayableItem
import com.example.filmshelper.data.models.filmDetails.Actor
import com.example.filmshelper.data.models.filmDetails.CreatorList
import com.example.filmshelper.data.models.filmDetails.Director
import com.example.filmshelper.databinding.CastItemListBinding
import com.example.filmshelper.databinding.CreatorsListItemBinding
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
                    textViewCharacter.text = item.asCharacter
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
                    textViewCharacter.text = "Director"
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
                    textViewCharacter.text = "Director"
                }
            }
        }
}