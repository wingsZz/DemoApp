package cn.outter.demo

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import cn.outter.demo.base.BaseVmVbActivity
import cn.outter.demo.conversation.ConversationActivity
import cn.outter.demo.databinding.ActivityMainBinding
import cn.outter.demo.find.FindFragment
import cn.outter.demo.session.SessionFragment
import cn.outter.demo.user.UserFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : BaseVmVbActivity<MainViewModel, ActivityMainBinding>() {
    private var adapter: FragmentPagerAdapter? = null
    private var tabLayoutMediator: TabLayoutMediator? = null
    private val tabs = arrayOf("发现", "聊天", "主页")
    private val selectedIcons = arrayOf(R.drawable.main_icon_love_select,R.drawable.main_icon_session_unselect,R.drawable.main_icon_setting_unselect)
    private val unselectedIcons = arrayOf(R.drawable.main_icon_love_select,R.drawable.main_icon_session_unselect,R.drawable.main_icon_setting_unselect)

    private val pageChangeCallback = object : OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            val tabCount = mViewBind.mainTab.tabCount
            for (i in 0 until tabCount) {
                val tab = mViewBind.mainTab.getTabAt(i)
                if (tab != null) {
                    val tabView = tab.customView
                    if (tabView != null) {
                        val tabLabel = tabView.findViewById<TextView>(R.id.tabLabel)
                        val tabIcon = tabView.findViewById<ImageView>(R.id.tabIcon)
                        if (i == position) {
                            tabLabel.setTypeface(Typeface.DEFAULT_BOLD)
                            tabIcon.setImageResource(selectedIcons[i])
                        } else {
                            tabLabel.setTypeface(Typeface.DEFAULT)
                            tabIcon.setImageResource(unselectedIcons[i])
                        }
                    }
                }

            }
        }
    }

    override fun createObserver() {

    }

    override fun initView(savedInstanceState: Bundle?) {
        adapter = FragmentPagerAdapter(this)
        mViewBind.viewPager.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
        mViewBind.viewPager.registerOnPageChangeCallback(pageChangeCallback)
        mViewBind.viewPager.adapter = adapter

        tabLayoutMediator = TabLayoutMediator(mViewBind.mainTab, mViewBind.viewPager, object :
            TabLayoutMediator.TabConfigurationStrategy {
            override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                val customTabView = layoutInflater.inflate(R.layout.item_main_tab, null)
                val tabLabel = customTabView.findViewById<TextView>(R.id.tabLabel)
                tabLabel.text = tabs[position]
                tab.customView = customTabView
            }
        })
        tabLayoutMediator?.attach()

        Handler().postDelayed({
//            DatabaseMockUtil.mockMessage()
        },1000)
    }

    private class FragmentPagerAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        private val fragments = Array<Fragment>(3) {
            when (it) {
                0 -> FindFragment()
                1 -> SessionFragment()
                2 -> UserFragment()
                else -> FindFragment()
            }
        }

        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mViewBind.viewPager.unregisterOnPageChangeCallback(pageChangeCallback)
        tabLayoutMediator?.detach()
    }
}