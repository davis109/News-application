package com.sebastiandavis.news_app.data.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class NewsResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("totalResults")
    val totalResults: Int,
    @SerializedName("articles")
    val articles: List<ArticleDto>
)

data class ArticleDto(
    @SerializedName("source")
    val source: SourceDto,
    @SerializedName("author")
    val author: String?,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("url")
    val url: String,
    @SerializedName("urlToImage")
    val urlToImage: String?,
    @SerializedName("publishedAt")
    val publishedAt: String,
    @SerializedName("content")
    val content: String?
) {
    fun toArticle(category: String): Article {
        return Article(
            url = url,
            title = title,
            author = author,
            source = source.name,
            description = description,
            content = content,
            urlToImage = urlToImage,
            publishedAt = try {
                // Parse ISO 8601 date format from API
                val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", java.util.Locale.getDefault())
                dateFormat.timeZone = java.util.TimeZone.getTimeZone("UTC")
                dateFormat.parse(publishedAt) ?: Date()
            } catch (e: Exception) {
                Date()
            },
            category = category
        )
    }
}

data class SourceDto(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String
) 