package com.example.newsfeed.data.models

data class Response(
    val currentPage: Int,
    val orderBy: String,
    val pageSize: Int,
    val pages: Int,
    val results: List<NewsResult>,
    val startIndex: Int,
    val status: String,
    val total: Int,
    val userTier: String
)