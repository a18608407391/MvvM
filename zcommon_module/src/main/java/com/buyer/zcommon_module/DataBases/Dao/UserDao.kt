package com.buyer.zcommon_module.DataBases.Dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.buyer.zcommon_module.Entity.UserInfo


interface UserDao{



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(vararg user:UserInfo)


}