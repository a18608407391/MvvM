package com.zl.zlibrary.cache


class Value {



    //存储的对象
     var obj: Any? = null

    //存储的数量
     var count = 0

    //存储的键
     var key: String? = null

     var callBack: ValueCallBack? = null


    fun nonUseAction() {
        if (count-- <= 0 && callBack != null) {
            callBack!!.valueNonUseListener(key!!, this)
        }
    }

    fun UseAction() {
        if (obj == null) {
            return
        }
        count++
    }


}