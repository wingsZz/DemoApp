package cn.outter.demo

import androidx.lifecycle.MutableLiveData
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

class ChatViewModel:BaseViewModel() {
    val data = MutableLiveData<String>()
}