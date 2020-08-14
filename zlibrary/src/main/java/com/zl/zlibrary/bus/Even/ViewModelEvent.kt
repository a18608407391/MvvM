package com.zl.zlibrary.bus.Even

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean


open class ViewModelEvent<T> : MutableLiveData<T>() {


    companion object {
        var TAG: String = javaClass.name
        var mPending: AtomicBoolean = AtomicBoolean(false)
    }

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {

        if (hasActiveObservers()) {
            Log.e(TAG, "Multiple observers registered but only one will be notified of changes.")
        }
        super.observe(owner, Observer<T> {
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        })
    }

    @MainThread
    override fun setValue(value: T?) {
        mPending.set(true)
        super.setValue(value)
    }

    @MainThread
    fun call() {
        this.value = null
    }

    @WorkerThread
    fun callNomal() {

    }

}