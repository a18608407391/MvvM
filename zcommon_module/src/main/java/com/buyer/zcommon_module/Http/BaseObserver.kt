package com.buyer.zcommon_module.Http

import io.reactivex.Observer
import io.reactivex.disposables.Disposable


abstract class BaseObserver<T> : Observer<T> {

    override fun onComplete() {
    }

    override fun onNext(t: T) {
    }

    override fun onError(e: Throwable) {
    }
    override fun onSubscribe(d: Disposable) {
    }
}