package com.zl.zlibrary.base

import android.util.Log
import androidx.databinding.ObservableArrayMap
import androidx.databinding.ObservableMap
import androidx.lifecycle.*
import com.zl.library.Entity.PostEntity
import com.zl.zlibrary.Utils.NetException
import com.zl.zlibrary.Utils.context
import com.zl.zlibrary.binding.BindingCommand
import com.zl.zlibrary.binding.BindingConsumer
import com.zl.zlibrary.bus.Even.ViewModelEvent
import com.zl.zlibrary.bus.RxBus
import com.zl.zlibrary.bus.RxSubscriptions
import com.zl.zlibrary.even.RxBusEven
import com.zl.zlibrary.ext.IS_DARK
import com.zl.zlibrary.ext.NOTIFY_VIEW_STATE
import com.zl.zlibrary.tools.NetState
import io.reactivex.disposables.Disposable
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein


open class BaseViewModel : ViewModel(), IBaseViewModel, KodeinAware {
    override fun onSingle(value: String) {

    }


    var curLenth = 2

    var state = 0

    var netState = MutableLiveData<NetState<String>>()





    var evenHandler = ObservableArrayMap<String, ViewModelEvent<Any>>().apply {
        this.put(NOTIFY_VIEW_STATE, getNotifyLiveData()!!.getPageStatus()!!)
        this.put(IS_DARK, getNotifyLiveData()!!.getStatusTextColorController())
    }.also {
        it.addOnMapChangedCallback(object :
            ObservableMap.OnMapChangedCallback<ObservableArrayMap<String, ViewModelEvent<Any>>, String, ViewModelEvent<Any>>() {
            override fun onMapChanged(
                sender: ObservableArrayMap<String, ViewModelEvent<Any>>?,
                key: String?
            ) {
                //监听集合，一旦新增了
                if (sender!!.size > curLenth) {
                    //添加
                    state = 1
                    getNotifyLiveData()!!.value = key

                } else if (sender!!.size < curLenth) {
                    //删除
                    state = 2
                    if (sender!!.size == 0) {
                        //remove
                        state = 3
                    }
                }
                curLenth = sender.size
            }
        })
    }

    var requestParameter: PostEntity? = null


    override fun initData(postEntity: PostEntity?) {
        //加载数据  为null是无参，不为null是有参数


    }


    override val kodein by closestKodein(context)

    var nomalDispose: Disposable? = null

    var notify: NotifyLiveData? = null

    open fun getNotifyLiveData(): NotifyLiveData? {
        if (notify == null) {
            notify = NotifyLiveData()
        }
        return notify
    }


    //统一单点事件处理
    var singleClick = BindingCommand(object : BindingConsumer<Int> {
        override fun call(t: Int) {
            onSingleClick(t)
        }
    })


    fun setPageState(state: NotifyLiveData.PageEnum) {
        getNotifyLiveData()!!.getPageStatus()!!.value = state
    }


    class NotifyLiveData : ViewModelEvent<String>() {
        enum class PageEnum {
            SHOW_CONTENT,
            SHOW_LOADING,
            SHOW_ERROR,
            SHOW_EMPTY
        }

        private var statusTextColor: ViewModelEvent<Any>? = null

        private var pagerStatus: ViewModelEvent<Any>? = null

        fun getStatusTextColorController(): ViewModelEvent<Any>? {
            if (statusTextColor == null) {
                statusTextColor = ViewModelEvent()
                statusTextColor!!.value = true
            }
            return statusTextColor
        }

        open fun getPageStatus(): ViewModelEvent<Any>? {
            if (pagerStatus == null) {
                pagerStatus = ViewModelEvent()
                pagerStatus!!.value = PageEnum.SHOW_LOADING
            }
            return pagerStatus
        }
    }


    open fun onSingleClick(id: Int) {

    }

    override fun onAny(owner: LifecycleOwner, event: Lifecycle.Event) {
    }

    override fun onDestroy() {
        removeRxBus()
    }

    override fun onPause() {
    }

    override fun onResume() {
    }

    override fun onStart() {
    }

    override fun onStop() {
    }

    override fun registerRxBus() {
        nomalDispose = RxBus.default!!.toObservable(RxBusEven::class.java).subscribe {
            doRxBus(it)
        }
        RxSubscriptions.add(nomalDispose)
    }

    open fun doRxBus(event: RxBusEven) {
    }

    override fun removeRxBus() {
        RxSubscriptions.remove(nomalDispose)
    }
}