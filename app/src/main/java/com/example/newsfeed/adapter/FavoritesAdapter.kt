package com.example.newsfeed.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newsfeed.R
import com.example.newsfeed.data.models.NewsResult
import com.example.newsfeed.utils.implementSpringAnimationTrait

class FavoritesAdapter(var favorites:MutableList<NewsResult>) : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {
    var onFavoritesClickEvent:OnFavoritesClickEvent?=null

    @SuppressLint("NotifyDataSetChanged")
    set(value) {
        field=value
        notifyDataSetChanged()
    }
    inner class FavoritesViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        private val title: TextView =itemView.findViewById(R.id.titleTv)
        private val category: TextView =itemView.findViewById(R.id.categoryTv)
        private val isFavorite: ImageView =itemView.findViewById(R.id.ic_favorite)

        @SuppressLint("ClickableViewAccessibility")
        fun bind(result: NewsResult){
            isFavorite.setImageResource(R.drawable.ic_favorite)
            title.text=result.webTitle
            category.text=result.pillarName
            isFavorite.setOnClickListener {
                onFavoritesClickEvent?.removeFromFavorites(result)
                notifyItemRemoved(adapterPosition)
                favorites.remove(result)
                result.isFavorite=result.isFavorite
            }
            itemView.setOnClickListener {
                onFavoritesClickEvent?.onFavoritesClicked(result)
            }
            itemView.setOnTouchListener { _, _ ->
                itemView.implementSpringAnimationTrait()
                false
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        return FavoritesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_news,parent,false))
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.bind(favorites[position])
    }
    override fun getItemCount(): Int =favorites.size

    interface OnFavoritesClickEvent{
        fun removeFromFavorites(result: NewsResult)
        fun onFavoritesClicked(result: NewsResult)
    }
}