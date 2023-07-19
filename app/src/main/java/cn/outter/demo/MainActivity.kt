package cn.outter.demo

import android.os.Bundle
import cn.outter.demo.databinding.ActivityMainBinding
import me.hgj.jetpackmvvm.base.activity.BaseVmVbActivity

class MainActivity : BaseVmVbActivity<MainViewModel,ActivityMainBinding>() {
    private var fragment:ChatFragment? = null

    override fun createObserver() {

    }

    override fun dismissLoading() {

    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun showLoading(message: String) {

    }
}