package com.dsc.grocerymanagement.network

import com.dsc.grocerymanagement.model.RazorLink
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("api/pay")
    fun getPaymentLink(
        @Query("mobile") mobile: String,
        @Query("amount") amount: Double
    ): Call<RazorLink>

}