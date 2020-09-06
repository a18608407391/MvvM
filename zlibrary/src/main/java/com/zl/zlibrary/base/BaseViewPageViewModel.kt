package com.zl.zlibrary.base

import androidx.databinding.ObservableArrayList
import com.zl.zlibrary.base.adapter.base.BindingViewPagerAdapter


class BaseViewPageViewModel   :BaseViewModel(){



    var adapter = BindingViewPagerAdapter<Any>()

    var items =  ObservableArrayList<Any>()


}