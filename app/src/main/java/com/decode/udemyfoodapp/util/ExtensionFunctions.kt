package com.decode.udemyfoodapp.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.initRecyclerView(mLayoutManager: RecyclerView.LayoutManager, mAdapter: RecyclerView.Adapter<*>) {
    this.apply {
        adapter = mAdapter
        layoutManager = mLayoutManager
    }
}

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>){
    observe(lifecycleOwner,object : Observer<T> {
        override fun onChanged(value: T) {
            removeObserver(this)
            observer.onChanged(value)
        }
    })
}

/** 'OnChanged' methodu, 'LiveData' nesnesinden 'Observer' nesnesini kaldırır ve ardından 'observeOnce' işlevine aktarılan orijinal 'Observer' nesnesinin 'onChanged' methodunu çağırır.
 * Bu fonksiyon, bir "LiveData" nesnesini yalnızca bir kez gözlemlemek ve ardından "Observer" nesnesini "LiveData" nesnesinden kaldırmak için kullanışlıdır.
 * Bu, bellek sızıntılarını önlemek veya 'Observer' nesnesinin yalnızca bir kez çağrılmasını sağlamak için yararlı olabilir. **/