package com.zl.zlibrary.base.activity

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.zl.zlibrary.Utils.StatusbarUtils
import com.zl.zlibrary.ext.dismissProgressDialogExt
import com.zl.zlibrary.ext.showProgressDialogExt


open class BaseActivityImpl : AppCompatActivity,IBaseActivity{

    constructor()
    constructor(@LayoutRes res:Int):super(res)

    override fun showProgressDialog(value: String) {
       showProgressDialogExt(value)
    }

    override fun dismissProgressDialog() {
        dismissProgressDialogExt()
    }

    override fun setStatusBarTextColor(it: Boolean) {
        StatusbarUtils.setStatusTextColor(it,this)
    }
}