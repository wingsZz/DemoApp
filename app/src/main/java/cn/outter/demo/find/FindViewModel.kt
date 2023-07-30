package cn.outter.demo.find

import androidx.lifecycle.MutableLiveData
import cn.outter.demo.base.BaseViewModel
import cn.outter.demo.bean.FindUser

class FindViewModel:BaseViewModel() {
    val findUserLiveData = MutableLiveData<List<FindUser>?>()

    fun getFindUsers() {

    }

}