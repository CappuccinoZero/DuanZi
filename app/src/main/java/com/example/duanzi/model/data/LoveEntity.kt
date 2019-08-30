package com.example.duanzi.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "loves")
class LoveEntity {
    @PrimaryKey
    var id = ""
    var love = false
    var hate = false
}