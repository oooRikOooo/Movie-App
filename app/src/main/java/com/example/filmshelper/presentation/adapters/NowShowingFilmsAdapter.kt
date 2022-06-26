package com.example.filmshelper.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.filmshelper.data.models.nowShowingMovies.ItemNowShowingMovies
import com.example.filmshelper.databinding.NowShowingItemListBinding
import com.squareup.picasso.Picasso

class NowShowingFilmsAdapter : RecyclerView.Adapter<NowShowingFilmsAdapter.MainFragmentViewHolder>() {

    var list = listOf<ItemNowShowingMovies>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    class MainFragmentViewHolder(private val binding : NowShowingItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemNowShowingMovies){
            binding.apply {
                textViewFilmName.text = item.title
                textViewReleaseDate.text = item.releaseState
                Picasso.get().load(item.image).fit()
                    .centerCrop().into(imageView)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainFragmentViewHolder {
        val binding = NowShowingItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MainFragmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainFragmentViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}