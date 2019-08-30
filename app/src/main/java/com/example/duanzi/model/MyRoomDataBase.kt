package com.example.duanzi.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.duanzi.model.data.LoveEntity

@Database(entities = [LoveEntity::class],version = 1)
abstract class MyRoomDataBase:RoomDatabase() {
    abstract fun getDao():Dao
}