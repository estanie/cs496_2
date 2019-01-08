package com.example.q.cs496_2.models

// Path 가 null이면 서버에서 가져온 아이.
data class Music(
    var id: String,
    val name: String,
    val author: String,
    val path: String?
)