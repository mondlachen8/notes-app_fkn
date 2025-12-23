package com.example.notes_fkn.model

data class Note(
    val id: Long,
    val title: String,
    val content: String,
    val spans: List<TextSpan>,
    val lastModified: Long = System.currentTimeMillis()
)