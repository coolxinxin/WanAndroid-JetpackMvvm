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

package com.xin.wanandroid.ui.square.navigation

import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xin.wanandroid.R
import com.xin.wanandroid.base.BaseVMFragment
import com.xin.wanandroid.ext.isVisible
import com.xin.wanandroid.ext.setOnNoRepeatClickListener
import kotlinx.android.synthetic.main.common_reload.*
import kotlinx.android.synthetic.main.fragment_navigation.*
import q.rorbin.verticaltablayout.VerticalTabLayout
import q.rorbin.verticaltablayout.adapter.TabAdapter
import q.rorbin.verticaltablayout.widget.ITabView.*
import q.rorbin.verticaltablayout.widget.TabView

/**
 *
 *   █████▒█    ██  ▄████▄   ██ ▄█▀       ██████╗ ██╗   ██╗ ██████╗
 * ▓██   ▒ ██  ▓██▒▒██▀ ▀█   ██▄█▒        ██╔══██╗██║   ██║██╔════╝
 * ▒████ ░▓██  ▒██░▒▓█    ▄ ▓███▄░        ██████╔╝██║   ██║██║  ███╗
 * ░▓█▒  ░▓▓█  ░██░▒▓▓▄ ▄██▒▓██ █▄        ██╔══██╗██║   ██║██║   ██║
 * ░▒█░   ▒▒█████▓ ▒ ▓███▀ ░▒██▒ █▄       ██████╔╝╚██████╔╝╚██████╔╝
 *  ▒ ░   ░▒▓▒ ▒ ▒ ░ ░▒ ▒  ░▒ ▒▒ ▓▒       ╚═════╝  ╚═════╝  ╚═════╝
 *  ░     ░░▒░ ░ ░   ░  ▒   ░ ░▒ ▒░
 *  ░ ░    ░░░ ░ ░ ░        ░ ░░ ░
 *           ░     ░ ░      ░  ░
 *@author : Leo
 *@date : 2020/9/10 9:43
 *@since : xinxiniscool@gmail.com
 *@desc :
 */
class NavigationFragment : BaseVMFragment<NavigationViewModel>() {

    private val mNavigationAdapter by lazy { NavigationAdapter() }
    private lateinit var mManager: LinearLayoutManager
    private var needScroll = false
    private var index = 0
    private var isClickTab = false

    override fun getViewModelClass(): Class<NavigationViewModel> = NavigationViewModel::class.java

    override fun initLayoutView(): Int = R.layout.fragment_navigation

    override fun initEvent() {
        mManager = LinearLayoutManager(mActivity)
        rvNavigation.layoutManager = mManager
        rvNavigation.adapter = mNavigationAdapter
        btReload.setOnNoRepeatClickListener {
            mViewModel.getNavigationData()
        }
    }

    override fun initData() {
        mViewModel.apply {
            isReload.observe(this@NavigationFragment, Observer {
                reloadNavigation.isVisible = it
            })
            navigationData.observe(this@NavigationFragment, Observer {
                leftRightLinkage()
                tabNavigation.setTabAdapter(object :
                    TabAdapter {
                    override fun getCount(): Int {
                        return it.size
                    }

                    override fun getBadge(position: Int): TabBadge? {
                        return null
                    }

                    override fun getIcon(position: Int): TabIcon? {
                        return null
                    }

                    override fun getTitle(position: Int): TabTitle {
                        return TabTitle.Builder()
                            .setContent(it[position].name)
                            .setTextColor(
                                ContextCompat.getColor(mActivity, R.color.green),
                                ContextCompat.getColor(mActivity, R.color.colorText)
                            )
                            .build()
                    }

                    override fun getBackground(position: Int): Int {
                        return -1
                    }
                })
                mNavigationAdapter.setList(it)
            })
        }
    }

    override fun lazyLoadData() {
        mViewModel.getNavigationData()
    }


    /**
     * Left tabLayout and right recyclerView linkage
     */
    private fun leftRightLinkage() {
        rvNavigation.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(
                @NonNull recyclerView: RecyclerView,
                newState: Int
            ) {
                super.onScrollStateChanged(recyclerView, newState)
                if (needScroll && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    scrollRecyclerView()
                }
                rightLinkageLeft(newState)
            }

            override fun onScrolled(
                @NonNull recyclerView: RecyclerView,
                dx: Int,
                dy: Int
            ) {
                super.onScrolled(recyclerView, dx, dy)
                if (needScroll) {
                    scrollRecyclerView()
                }
            }
        })
        tabNavigation.addOnTabSelectedListener(object : VerticalTabLayout.OnTabSelectedListener {
            override fun onTabSelected(
                tabView: TabView,
                i: Int
            ) {
                isClickTab = true
                selectTag(i)
            }

            override fun onTabReselected(
                tabView: TabView,
                i: Int
            ) {
            }
        })
    }

    private fun scrollRecyclerView() {
        needScroll = false
        val indexDistance: Int = index - mManager.findFirstVisibleItemPosition()
        if (indexDistance >= 0 && indexDistance < rvNavigation.childCount) {
            val top: Int = rvNavigation.getChildAt(indexDistance).top
            rvNavigation.smoothScrollBy(0, top)
        }
    }

    /**
     * Right recyclerView linkage left tabLayout
     * SCROLL_STATE_IDLE just call once
     *
     * @param newState RecyclerView new scroll state
     */
    private fun rightLinkageLeft(newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (isClickTab) {
                isClickTab = false
                return
            }
            val firstPosition: Int = mManager.findFirstVisibleItemPosition()
            if (index != firstPosition) {
                index = firstPosition
                setChecked(index)
            }
        }
    }

    private fun selectTag(i: Int) {
        index = i
        rvNavigation.stopScroll()
        smoothScrollToPosition(i)
    }

    /**
     * Smooth right to select the position of the left tab
     *
     * @param position checked position
     */
    private fun setChecked(position: Int) {
        if (isClickTab) {
            isClickTab = false
        } else {
            if (tabNavigation == null) {
                return
            }
            tabNavigation.setTabSelected(index)
        }
        index = position
    }

    private fun smoothScrollToPosition(currentPosition: Int) {
        val firstPosition: Int = mManager.findFirstVisibleItemPosition()
        val lastPosition: Int = mManager.findLastVisibleItemPosition()
        when {
            currentPosition <= firstPosition -> {
                rvNavigation.smoothScrollToPosition(currentPosition)
            }
            currentPosition <= lastPosition -> {
                val top: Int = rvNavigation.getChildAt(currentPosition - firstPosition).top
                rvNavigation.smoothScrollBy(0, top)
            }
            else -> {
                rvNavigation.smoothScrollToPosition(currentPosition)
                needScroll = true
            }
        }
    }
}