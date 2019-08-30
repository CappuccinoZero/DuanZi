package com.example.duanzi.model

import com.example.duanzi.model.data.ContentBean
import io.reactivex.Observable
import io.reactivex.Observer
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("satinApi")
    fun getDataItem(@Query("type") type:Int,@Query("page") page:Int): Observable<ContentBean>
}