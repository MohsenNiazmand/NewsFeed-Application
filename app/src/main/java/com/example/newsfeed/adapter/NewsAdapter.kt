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

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    var newsEventListener:NewsEventListener?=null
    val news:MutableList<NewsResult> = mutableListOf()
    fun addNews(newss:MutableList<NewsResult>){
        this.news.addAll(newss)
    }


    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title=itemView.findViewById<TextView>(R.id.titleTv)
        val category=itemView.findViewById<TextView>(R.id.categoryTv)
        val isFavorite=itemView.findViewById<ImageView>(R.id.ic_favorite)

        fun bind(result: NewsResult){


            itemView.setOnTouchListener(object : View.OnTouchListener{
                @SuppressLint("ClickableViewAccessibility")
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    itemView.implementSpringAnimationTrait()
                    return false
                }

            })
            itemView.setOnClickListener {
                newsEventListener?.onNewsClicked(result)
            }
            itemView.apply {
                title.text=result.webTitle
                category.text=result.pillarName
                isFavorite.setOnClickListener {
                    newsEventListener?.addToFavorites(result)
                    result.isFavorite=!result.isFavorite
                    notifyItemChanged(adapterPosition)
                }

                if (result.isFavorite){
                    isFavorite.setImageResource(R.drawable.ic_favorite)
                }else
                    isFavorite.setImageResource(R.drawable.ic_favorite_border)

            }


        }

    }
    interface NewsEventListener{
        fun addToFavorites(result: NewsResult)
        fun onNewsClicked(result: NewsResult)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_news,parent,false))
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(news[position])
    }

    override fun getItemCount(): Int {
        return news.size
    }
}