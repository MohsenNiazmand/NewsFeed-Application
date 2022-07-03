package com.example.newsfeed.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "news")
@Parcelize
data class NewsResult(
    val apiUrl: String,
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val isHosted: Boolean,
    val pillarId: String,
    val pillarName: String,
    val sectionId: String,
    val sectionName: String,
    val type: String,
    val webPublicationDate: String,
    val webTitle: String,
    val webUrl: String
) : Parcelable{
    var isFavorite: Boolean = false
}