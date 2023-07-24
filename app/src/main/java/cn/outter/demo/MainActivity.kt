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
import cn.outter.demo.conversation.ConversationActivity
import cn.outter.demo.conversation.ConversionFragment
import cn.outter.demo.databinding.ActivityMainBinding
import cn.outter.demo.session.SessionFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import me.hgj.jetpackmvvm.base.activity.BaseVmVbActivity

class MainActivity : BaseVmVbActivity<MainViewModel, ActivityMainBinding>() {
    private var adapter: FragmentPagerAdapter? = null
    private var tabLayoutMediator: TabLayoutMediator? = null
    private val tabs = arrayOf("发现", "聊天", "主页")

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
                        if (tab.position == position) {
                            tabLabel.setTypeface(Typeface.DEFAULT_BOLD);
                        } else {
                            tabLabel.setTypeface(Typeface.DEFAULT);
                        }
                    }
                }

            }
        }
    }

    override fun createObserver() {

    }

    override fun dismissLoading() {

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
//                int[][] states = new int[2][];
//                states[0] = new int[]{android.R.attr.state_selected};
//                states[1] = new int[]{};
//
//                int[] colors = new int[]{activeColor, normalColor};
//                ColorStateList colorStateList = new ColorStateList(states, colors);
//                customTabView.setText(tabs[position]);
//                customTabView.setTextSize(normalSize);
//                customTabView.setTextColor(colorStateList);

                tab.customView = customTabView
            }
        })
        tabLayoutMediator?.attach()

        Handler().postDelayed({
//            DatabaseMockUtil.mockMessage()
        },1000)

        mViewBind.toConversation.setOnClickListener {
            startActivity(Intent(this,ConversationActivity::class.java))
        }
    }

    override fun showLoading(message: String) {

    }

    private class FragmentPagerAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        private val fragments = Array<Fragment>(3) {
            when (it) {
                0 -> SessionFragment()
                1 -> SessionFragment()
                2 -> SessionFragment()
                else -> SessionFragment()
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