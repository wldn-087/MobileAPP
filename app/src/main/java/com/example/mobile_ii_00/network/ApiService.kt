package com.example.mobile_ii_00.network

import com.example.mobile_ii_00.model.AuthDTO
import com.example.mobile_ii_00.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/auth")
    fun login (@Body authDTO: AuthDTO): Call<LoginResponse>
}