package com.example.filmshelper.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.filmshelper.data.models.FilmDetails.Genre
import com.example.filmshelper.data.models.popularMovies.ItemPopularMovies
import com.example.filmshelper.databinding.CategoryListItemBinding

class CategoriesAdapter : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    var list = listOf<Genre>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class CategoriesViewHolder(private val binding: CategoryListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : Genre){
            binding.textView.text = item.value
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val binding = CategoryListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CategoriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}