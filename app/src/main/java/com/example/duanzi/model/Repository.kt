package com.example.duanzi.model

import android.content.Context
import android.util.Log
import com.example.duanzi.model.data.ContentBean
import com.example.duanzi.model.data.Data
import com.example.duanzi.model.data.LoveEntity
import com.example.duanzi.util.MyUtil
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import org.reactivestreams.Subscriber
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class Repository(appContext:Context) {
    companion object{
        private var instance:Repository ?= null
        fun getInstance(appContext:Context):Repository{
            if(instance == null){
                synchronized(Repository::class.java){
                    if (instance==null)
                        instance = Repository(appContext)
                }
            }
            return instance!!
        }
    }
    private val cache_size = 1024*1024*10L
    private var max_age = 60*60*24*3
    private val url = "https://www.apiopen.top/"
    private val room by lazy {
        RoomHelper.getInstance(appContext.applicationContext)
    }
    private val retrofit:Retrofit by lazy {
        Retrofit.Builder().apply {
            addConverterFactory(GsonConverterFactory.create())
            addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            client(client)
            baseUrl(url)
        }.build()
    }
    private val client:OkHttpClient by lazy {
        OkHttpClient.Builder().apply {
            readTimeout(10,TimeUnit.SECONDS)
            connectTimeout(10,TimeUnit.SECONDS)
            writeTimeout(10,TimeUnit.SECONDS)
            addNetworkInterceptor(cacheInterceptor)
            cache(cache)
        }.build()
    }
    private val cacheFile by lazy {
        File(appContext.cacheDir,"httpCache")
    }
    private val cache by lazy {
        Cache(cacheFile,cache_size)
    }

    private val cacheInterceptor = object :Interceptor{
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val response = chain.proceed(request)
            if(MyUtil.isNetworkConnected(appContext)){
                response.newBuilder().removeHeader("Pragma")
                    .addHeader("Cache-Control","public, max-age=$max_age")
                    .build()
            }
            return response
        }
    }

    fun onLoadPictureData(page:Int,subscriber: Observer<ArrayList<Data>>){
        retrofit.create(Api::class.java).getDataItem(3,page)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { t -> t.data!! }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber)
    }

    fun onLoadTextData(page:Int,subscriber: Observer<ArrayList<Data>>){
        retrofit.create(Api::class.java).getDataItem(2,page)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { t -> t.data!! }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber)
    }

    fun onQuery(id:String,subscriber: Subscriber<List<LoveEntity>>){
        room.getDao().queryLoveData(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber)
    }

    fun insert(loveEntity: LoveEntity){
        room.getDao().insertData(loveEntity)
    }
}