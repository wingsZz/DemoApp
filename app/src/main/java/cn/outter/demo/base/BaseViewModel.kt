package cn.outter.demo.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel() : ViewModel() {
    private val disposables = CompositeDisposable()

    fun register(disposable: Disposable?) {
        disposable?.let {
            disposables.add(it)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
        disposables.clear()
    }
}

fun Disposable?.bindLifecycle(model: BaseViewModel?) {
    this?.let {
        model?.register(it)
    }
}