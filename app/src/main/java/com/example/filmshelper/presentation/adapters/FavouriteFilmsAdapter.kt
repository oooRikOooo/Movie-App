package com.example.filmshelper.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.filmshelper.data.models.FirebaseUserFavouriteFilms
import com.example.filmshelper.databinding.ItemFavouriteFilmsBinding
import com.squareup.picasso.Picasso

class FavouriteFilmsAdapter: RecyclerView.Adapter<FavouriteFilmsAdapter.FavouriteFilmsViewHolder>() {

    var list = listOf<FirebaseUserFavouriteFilms>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class FavouriteFilmsViewHolder(private val binding: ItemFavouriteFilmsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : FirebaseUserFavouriteFilms){
            binding.apply {
                Log.d("riko", item.imageUrl)
                textViewRating.text = item.rating ?: "0"
                Picasso.get().load(item.imageUrl).fit()
                    .centerCrop().into(imageView)



            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteFilmsViewHolder {
        val binding = ItemFavouriteFilmsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return FavouriteFilmsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavouriteFilmsViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}