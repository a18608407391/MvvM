package com.zl.zlibrary.base

import androidx.lifecycle.*
import com.zl.library.Entity.PostEntity
import com.zl.zlibrary.Utils.context
import com.zl.zlibrary.binding.BindingCommand
import com.zl.zlibrary.binding.BindingConsumer
import com.zl.zlibrary.bus.Even.ViewModelEvent
import com.zl.zlibrary.bus.RxBus
import com.zl.zlibrary.bus.RxSubscriptions
import com.zl.zlibrary.even.RxBusEven
import io.reactivex.disposables.Disposable
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein


open class BaseViewModel : ViewModel(), IBaseViewModel, KodeinAware {

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


    class NotifyLiveData : ViewModelEvent<Any>() {
        enum class PageEnum {
            SHOW_CONTENT,
            SHOW_LOADING,
            SHOW_ERROR,
            SHOW_EMPTY
        }

        private var statusTextColor: ViewModelEvent<Boolean>? = null

        private var pagerStatus: ViewModelEvent<PageEnum>? = null

        fun getStatusTextColorController(): ViewModelEvent<Boolean>? {
            if (statusTextColor == null) {
                statusTextColor = ViewModelEvent()
                statusTextColor!!.value = true
            }
            return statusTextColor
        }

        open fun getPageStatus(): ViewModelEvent<PageEnum>? {
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