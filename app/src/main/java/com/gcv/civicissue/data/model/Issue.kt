package com.gcv.civicissue.data.model

data class Issue(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val type: String = "",
    val imageBase64: String = "",
    val status: String = "Pending",
    val userId: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
