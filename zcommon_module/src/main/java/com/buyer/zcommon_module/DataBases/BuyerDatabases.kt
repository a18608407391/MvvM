package com.buyer.zcommon_module.DataBases

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.buyer.zcommon_module.Entity.UserInfo


@Database(entities = [UserInfo::class], version = 1)
@TypeConverters(value = [DateConverter::class])
abstract class BuyerDatabases  : RoomDatabase(){


}