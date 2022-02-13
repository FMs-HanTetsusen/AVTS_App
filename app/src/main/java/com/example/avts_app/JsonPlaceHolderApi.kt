package com.example.avts_app

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface JsonPlaceHolderApi {
    @POST("api/posts")
    fun createPost(@Body userInfo: UserInfo): Call<UserInfo>
}