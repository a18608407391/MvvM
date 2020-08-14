package com.zl.zlibrary.even




class RxBusEven {


    companion object {
        val POST_VALUE =  0x9999
    }


    constructor(type:Int){
         this.type = type
    }

    var type = 0

    var value: Any? = null

    var secondValue: Any? = null

    var parameter: HashMap<Any, Any>? = null


}