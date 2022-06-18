package com.example.filmshelper.presentation.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.filmshelper.R
import com.example.filmshelper.data.models.popularMovies.ItemPopularMovies
import com.example.filmshelper.databinding.PopularItemListBinding
import com.squareup.picasso.Picasso

class PopularMoviesAdapter : RecyclerView.Adapter<PopularMoviesAdapter.PopularFilmsViewHolder>() {

    var list = listOf<ItemPopularMovies>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class PopularFilmsViewHolder(private val binding : PopularItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemPopularMovies){
            binding.apply {
                textViewTitle.text = item.title
                textViewRating.text = binding.root.context.getString(R.string.rating_imdb, item.imDbRating)
                textViewYear.text = item.year

                Picasso.get().load(item.image).fit()
                    .centerCrop().into(imageView2)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularFilmsViewHolder {
        val binding = PopularItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PopularFilmsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularFilmsViewHolder, position: Int) {
        holder.bind(list[position])

        holder.itemView.setOnClickListener {
            it.findNavController().navigate(R.id.action_mainFragment_to_filmDetailsFragment,
                Bundle().apply {
                    putString("filmId", list[position].id)
            })
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}