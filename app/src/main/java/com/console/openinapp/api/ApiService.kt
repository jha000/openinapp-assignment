package com.console.openinapp.api

import com.console.openinapp.model.Response
import com.console.openinapp.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.Call

interface ApiService {
    @GET(Constants.API_ENDPOINT)
    fun getDashboardData(
        @Header("Authorization") authToken: String
    ): Call<Response>
}

