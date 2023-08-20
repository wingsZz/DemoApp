package cn.outter.demo.base

import android.R
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.outter.demo.base.manager.NetState
import cn.outter.demo.base.manager.NetworkStateManager
import cn.outter.demo.utils.notNull
import com.gyf.immersionbar.ImmersionBar


abstract class BaseVmActivity<VM : BaseViewModel> : AppCompatActivity() {

    private var loadingDialog = LoadingDialogFragment()

    lateinit var mViewModel: VM

    abstract fun layoutId(): Int

    abstract fun initView(savedInstanceState: Bundle?)

    fun showLoading(message: String = "请求网络中...") {
        loadingDialog.show(supportFragmentManager,"")
    }

    fun dismissLoading() {
        loadingDialog.dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initImmersionBar()
        super.onCreate(savedInstanceState)
        initDataBind().notNull({
            setContentView(it)
        }, {
            setContentView(layoutId())
        })
        init(savedInstanceState)
    }

    private fun init(savedInstanceState: Bundle?) {
        mViewModel = createViewModel()
        registerUiChange()
        initView(savedInstanceState)
        createObserver()
        NetworkStateManager.instance.mNetworkStateCallback.observeInActivity(this, Observer {
            onNetworkStateChanged(it)
        })
    }

    /**
     * 初始化沉浸式
     * Init immersion bar.
     */
    protected open fun initImmersionBar() {
        //设置共同沉浸式样式
        ImmersionBar.with(this).statusBarColor(android.R.color.white)
            .navigationBarColor(R.color.white).fitsSystemWindows(true).init()
    }

    /**
     * 网络变化监听 子类重写
     */
    open fun onNetworkStateChanged(netState: NetState) {}

    /**
     * 创建viewModel
     */
    private fun createViewModel(): VM {
        return ViewModelProvider(this).get(getVmClazz(this))
    }

    /**
     * 创建LiveData数据观察者
     */
    abstract fun createObserver()

    /**
     * 注册UI 事件
     */
    private fun registerUiChange() {
//        //显示弹窗
//        mViewModel.loadingChange.showDialog.observeInActivity(this, Observer {
//            showLoading(it)
//        })
//        //关闭弹窗
//        mViewModel.loadingChange.dismissDialog.observeInActivity(this, Observer {
//            dismissLoading()
//        })
    }

    /**
     * 将非该Activity绑定的ViewModel添加 loading回调 防止出现请求时不显示 loading 弹窗bug
     * @param viewModels Array<out BaseViewModel>
     */
    protected fun addLoadingObserve(vararg viewModels: BaseViewModel) {
//        viewModels.forEach { viewModel ->
//            //显示弹窗
//            viewModel.loadingChange.showDialog.observeInActivity(this, Observer {
//                showLoading(it)
//            })
//            //关闭弹窗
//            viewModel.loadingChange.dismissDialog.observeInActivity(this, Observer {
//                dismissLoading()
//            })
//        }
    }

    /**
     * 供子类BaseVmDbActivity 初始化Databinding操作
     */
    open fun initDataBind(): View? {
        return null
    }
}