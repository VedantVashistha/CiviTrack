package com.gcv.civicissue.data.remote

import com.gcv.civicissue.data.model.AiAnalysis
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class DescriptionRequest(
    val description: String
)

interface AiApiService {

    @POST("analyze")
    fun analyzeIssue(
        @Body request: DescriptionRequest
    ): Call<AiAnalysis>
}
