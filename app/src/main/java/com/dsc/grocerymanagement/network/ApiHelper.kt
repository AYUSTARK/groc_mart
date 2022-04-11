package com.dsc.grocerymanagement.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.dsc.grocerymanagement.model.RazorLink
import com.dsc.grocerymanagement.util.Resource
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiHelper(private val apiManager: ApiManager) {

//    interface OnStatus {
//        fun onSuccess(razor: RazorLink)
//        fun onFailure()
//    }

    fun getPaymentLink(
        mobile: String,
        amount: Double,
        payment: MutableLiveData<Resource<RazorLink>>,
//        status: OnStatus
    ) {
        payment.postValue(Resource.loading(null))
        apiManager.customApiService.getPaymentLink(mobile, amount)
            .enqueue(object : Callback<RazorLink> {
                override fun onResponse(call: Call<RazorLink>, response: Response<RazorLink>) {
//                    val song = response.body()?.results!!
//                    Log.d("Response", "${response.body()}")
                    val result = if (response.code() == 200 && response.body() != null) {
                        response.body()
                    } else {
                        Gson().fromJson(
                            JSONObject(
                                response.errorBody()!!.charStream().readText()
                            ).toString(2), RazorLink::class.java
                        )
                    }
//                    Log.d("ResponseS", "$result")
                    payment.postValue(Resource.success(result))
//                    status.onSuccess(response.body()!!)
                }

                override fun onFailure(call: Call<RazorLink>, t: Throwable) {
//                    status.onFailure()
                    Log.e("Response", "Error $t", t)
                    payment.postValue(Resource.error("Something Went Wrong", null))
                }
            })
    }
}
