package com.buyer.zcommon_module.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable



@Entity(tableName = "user_table")
class UserInfo :Serializable{


    @PrimaryKey(autoGenerate = true)
    var id :Int = 0

}