package com.example.shoppinglisttesting.data.remote.responses

import com.example.shoppinglisttesting.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface API {

    @GET("/api/")
    suspend fun searchForImage(
        @Query("q") searchQuery: String,
        @Query("key") apiKey: String = BuildConfig.API_KEY,
    ): Response<ImageResponse>
}