package com.sebastiandavis.news_app.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName = "articles")
data class Article(
    @PrimaryKey
    val url: String,
    val title: String,
    val author: String?,
    val source: String,
    val description: String?,
    val content: String?,
    val urlToImage: String?,
    val publishedAt: Date,
    val category: String,
    val isSaved: Boolean = false,
    val isRead: Boolean = false
) : Parcelable 