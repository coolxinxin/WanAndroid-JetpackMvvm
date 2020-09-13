/*
 * Copyright 2020 Leo
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xin.wanandroid.ui.main

import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.xin.wanandroid.R
import com.xin.wanandroid.base.BaseSimpleActivity
import com.xin.wanandroid.ui.home.HomeFragment
import com.xin.wanandroid.ui.mine.MineFragment
import com.xin.wanandroid.ui.project.ProjectFragment
import com.xin.wanandroid.ui.square.SquareFragment
import com.xin.wanandroid.ui.wechat.WeChatFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseSimpleActivity() {

    private lateinit var mFragments: ArrayList<Fragment>
    private var mLastFgIndex: Int = 0
    private var mLastTime = 0L

    override fun initLayoutView(): Int = R.layout.activity_main

    override fun initEvent() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun initData() {
        initFragment()
        initBottomNavigationView()
        showFragment("首页", 0)
    }

    private fun initFragment() {
        mFragments = ArrayList()
        mFragments.add(HomeFragment())
        mFragments.add(SquareFragment())
        mFragments.add(WeChatFragment())
        mFragments.add(ProjectFragment())
        mFragments.add(MineFragment())
    }

    private fun initBottomNavigationView() {
        bottom_navigation_view.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.tabHome -> {
                    showFragment("首页", 0)
                }
                R.id.tabSquare -> {
                    showFragment("广场中心", 1)
                }
                R.id.tabWeChatPublic -> {
                    showFragment("微信公众号", 2)
                }
                R.id.tabProject -> {
                    showFragment("项目列表", 3)
                }
                R.id.tabMine -> {
                    showFragment("我的", 4)
                }
            }
            true
        }
    }

    /**
     * 切换fragment
     *
     * @param title toolbar显示的标题
     * @param position 要显示的fragment的下标
     */
    private fun showFragment(title: String, position: Int) {
        if (position >= mFragments.size) {
            return
        }
        tvToolbarTitle.text = title
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        val targetFg = mFragments[position]
        val lastFg = mFragments[mLastFgIndex]
        mLastFgIndex = position
        ft.hide(lastFg)
        if (!targetFg.isAdded) {
            supportFragmentManager.beginTransaction().remove(targetFg)
                .commitAllowingStateLoss()
            ft.add(R.id.fragment_group, targetFg)
        }
        ft.show(targetFg)
        ft.commitAllowingStateLoss()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
//        val itemSearch = menu?.findItem(R.id.item_search)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_search ->{}
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - mLastTime > 2000) {
            showSnackBar(bottom_navigation_view)
            mLastTime = System.currentTimeMillis()
        } else {
            super.onBackPressed()
        }
    }

    private fun showSnackBar(view: View) {
        //因为bottom_navigation_view会遮挡住Snackbar,所以先隐藏,在显示
        bottom_navigation_view.visibility = View.GONE
        val snackBar = Snackbar.make(view, "再按一次返回键退出", Snackbar.LENGTH_SHORT)
        snackBar.setAction("知道了") {
            snackBar.dismiss()
            bottom_navigation_view.visibility = View.VISIBLE
        }.show()
        snackBar.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar?>() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                bottom_navigation_view.visibility = View.VISIBLE
            }
        })
    }
}