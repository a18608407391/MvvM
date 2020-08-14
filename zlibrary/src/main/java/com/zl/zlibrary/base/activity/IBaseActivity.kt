package com.zl.zlibrary.base.activity



interface IBaseActivity  {

    //显示弹出窗
    fun showProgressDialog(value:String)

    //隐藏弹出窗
    fun dismissProgressDialog()

    fun onNetWorkStateChanged(netState: Boolean){}

    fun setStatusBarTextColor(it:Boolean)


}