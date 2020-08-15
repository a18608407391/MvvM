package com.zl.zlibrary.base

import androidx.databinding.ObservableArrayList
import com.zl.zlibrary.base.adapter.recycler.BindingRecyclerViewAdapter
import com.zl.zlibrary.base.listener.BaseItemBean
import com.zl.zlibrary.binding.BindingCommand
import com.zl.zlibrary.binding.BindingConsumer


class BaseListViewModel : BaseViewModel() {


    //通用适配器
    var adapter = BindingRecyclerViewAdapter<Any>()


    // 通用列表数据模型
    var items = ObservableArrayList<Any>()




    //当前显示页码
    var page = 0

    //当前每页显示数量
    var limit = 0


    open fun initPage(page: Int, limit: Int) {
        this.page = page
        this.limit = limit
    }


    //刷新事件
    open fun onRefresh() {
        initPage(0, limit)
    }


    //列表点击事件绑定
    var onItemClickBinding = BindingCommand(object : BindingConsumer<BaseItemBean<*>> {
        override fun call(t: BaseItemBean<*>) {
            onItemClickListener(t)
        }
    })


    //列表长按点击事件绑定
    var onItemLongClickBinding = BindingCommand(object : BindingConsumer<BaseItemBean<*>> {
        override fun call(t: BaseItemBean<*>) {
            onItemLongClickListener(t)
        }
    })


    //滑动事件绑定
    var onScrollerBinding = BindingCommand(object : BindingConsumer<Int> {
        override fun call(t: Int) {
            //处理加载更多
            if (t < page * limit && t != items.size) {
                return
            } else {
                page++
                LoadMore()
            }
        }
    })


    //加载更多
    open fun LoadMore() {

    }

    open fun onItemClickListener(t: BaseItemBean<*>) {

    }

    open fun onItemLongClickListener(t: BaseItemBean<*>) {

    }


}