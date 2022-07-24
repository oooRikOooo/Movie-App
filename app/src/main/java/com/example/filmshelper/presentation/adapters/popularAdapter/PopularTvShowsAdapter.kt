package com.example.filmshelper.presentation.adapters.popularAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.filmshelper.R
import com.example.filmshelper.data.models.popular.popularTvShows.ItemPopularTvShows
import com.example.filmshelper.databinding.PopularTvShowItemListBinding
import com.squareup.picasso.Picasso

class PopularTvShowsAdapter: RecyclerView.Adapter<PopularTvShowsAdapter.PopularTvShowsViewHolder>() {

    var list = listOf<ItemPopularTvShows>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    class PopularTvShowsViewHolder(private val binding : PopularTvShowItemListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemPopularTvShows){
            binding.apply {
                textViewTitle.text = item.title
                textViewRating.text = binding.root.context.getString(R.string.rating_imdb, item.imDbRating)
                textViewYear.text = item.description

                Picasso.get().load(item.image).fit()
                    .centerCrop().into(imageView2)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularTvShowsViewHolder {
        val binding = PopularTvShowItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PopularTvShowsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularTvShowsViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}