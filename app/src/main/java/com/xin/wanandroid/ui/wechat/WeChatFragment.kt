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

package com.xin.wanandroid.ui.wechat

import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.xin.wanandroid.R
import com.xin.wanandroid.base.BaseVMFragment
import com.xin.wanandroid.ext.isVisible
import com.xin.wanandroid.ext.setOnNoRepeatClickListener
import com.xin.wanandroid.ui.common.ViewPagerCommonAdapter
import com.xin.wanandroid.ui.wechat.article.WeChatListFragment
import kotlinx.android.synthetic.main.common_reload.*
import kotlinx.android.synthetic.main.fragment_wechat.*

/**
 *   █████▒█    ██  ▄████▄   ██ ▄█▀       ██████╗ ██╗   ██╗ ██████╗
 * ▓██   ▒ ██  ▓██▒▒██▀ ▀█   ██▄█▒        ██╔══██╗██║   ██║██╔════╝
 * ▒████ ░▓██  ▒██░▒▓█    ▄ ▓███▄░        ██████╔╝██║   ██║██║  ███╗
 * ░▓█▒  ░▓▓█  ░██░▒▓▓▄ ▄██▒▓██ █▄        ██╔══██╗██║   ██║██║   ██║
 * ░▒█░   ▒▒█████▓ ▒ ▓███▀ ░▒██▒ █▄       ██████╔╝╚██████╔╝╚██████╔╝
 *  ▒ ░   ░▒▓▒ ▒ ▒ ░ ░▒ ▒  ░▒ ▒▒ ▓▒       ╚═════╝  ╚═════╝  ╚═════╝
 *  ░     ░░▒░ ░ ░   ░  ▒   ░ ░▒ ▒░
 *  ░ ░    ░░░ ░ ░ ░        ░ ░░ ░
 *           ░     ░ ░      ░  ░
 * @author : Leo
 * @date : 2020/9/13 18:43
 * @desc :
 * @since : xinxiniscool@gmail.com
 */
class WeChatFragment : BaseVMFragment<WeChatViewModel>(), ViewPager.OnPageChangeListener {

    private val adapter by lazy {
        ViewPagerCommonAdapter(
            childFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
    }
    private var page = 0

    override fun getViewModelClass(): Class<WeChatViewModel> = WeChatViewModel::class.java

    override fun initLayoutView(): Int = R.layout.fragment_wechat

    override fun initEvent() {
        vpWeChat.adapter = adapter
        vpWeChat.currentItem = page
        vpWeChat.addOnPageChangeListener(this)
        btReload.setOnNoRepeatClickListener {
            mViewModel.getWeChatData()
        }
    }

    override fun initData() {
        mViewModel.apply {
            isReload.observe(this@WeChatFragment, Observer {
                reloadWeChat.isVisible = it
            })
            weChatData.observe(this@WeChatFragment, Observer {
                it.forEach { it1 ->
                    tabWeChat.addTab(tabWeChat.newTab().setText(it1.name))
                    adapter.apply {
                        addFragment(WeChatListFragment().addId(it1.id))
                        addTitle(it1.name)
                    }
                }
                tabWeChat.setupWithViewPager(vpWeChat)
            })
        }
    }

    override fun lazyLoadData() {
        mViewModel.getWeChatData()
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        page = position
    }
}