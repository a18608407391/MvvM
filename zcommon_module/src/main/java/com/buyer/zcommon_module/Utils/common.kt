package com.buyer.zcommon_module.Utils

import androidx.room.Room
import com.buyer.zcommon_module.DataBases.BuyerDatabases
import com.zl.zlibrary.base.BaseApplication
import com.zl.zlibrary.Utils.context




val BaseApplication.database get() =  Room.databaseBuilder(context,BuyerDatabases::class.java,"buyer_red.db").addMigrations().allowMainThreadQueries().build()

