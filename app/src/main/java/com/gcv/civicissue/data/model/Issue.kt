package com.gcv.civicissue.data.model

data class Issue(
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var type: String = "",
    var status: String = "Pending",
    var imageBase64: String = "",
    var severity: String = "Low",
    var estimatedHours: Int = 48,
    var timestamp: Long = System.currentTimeMillis()
)
