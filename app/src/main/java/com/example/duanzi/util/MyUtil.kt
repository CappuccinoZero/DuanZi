package com.example.duanzi.util

import android.content.Context
import android.net.ConnectivityManager

class MyUtil {
    companion object{
        fun isNetworkConnected(appContext: Context) : Boolean{
            val connectManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkinfo = connectManager.activeNetworkInfo
            if(networkinfo==null)
                return false
            return networkinfo.isAvailable
        }
    }
}