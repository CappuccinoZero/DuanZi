package com.example.duanzi.view_model

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.duanzi.model.Repository
import com.example.duanzi.model.data.Data
import com.example.duanzi.model.data.LoveEntity
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class MyViewModel(app:Application):AndroidViewModel(app),Observer<ArrayList<Data>> {
    override fun onError(e: Throwable) {
        newDatas.value?.clear()
    }

    override fun onSubscribe(d: Disposable) {

    }

    override fun onComplete() {

    }

    private val model:Repository by lazy {
        Repository.getInstance(app)
    }
    val newDatas by lazy {
        MutableLiveData<ArrayList<Data>>()
    }
    private var page = 0
    fun onloadTextData(){
        model.onLoadTextData(page,this)
    }

    fun onLoadPictureData(){
        model.onLoadPictureData(page,this)
    }

    override fun onNext(t: ArrayList<Data>) {
        newDatas.value = t
    }

    fun onLoveByTest(id:String){
        model.insert(LoveEntity().also {
            it.hate = false
            it.love = true
            it.id = id
        })
    }
}