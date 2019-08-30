package com.example.duanzi.model

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

class RoomHelper {
    companion object{
        private var database:MyRoomDataBase ?= null
        fun getInstance(appContext: Context):MyRoomDataBase{
            if(database == null){
                synchronized(MyRoomDataBase::class.java){
                    if(database==null){
                        database = Room.databaseBuilder(appContext,MyRoomDataBase::class.java,"my_data_base.db")
                            .allowMainThreadQueries()
                            .build()
                    }
                }
            }
            return database!!
        }
    }
}