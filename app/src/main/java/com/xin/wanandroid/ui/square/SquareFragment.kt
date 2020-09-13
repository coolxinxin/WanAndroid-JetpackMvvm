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

package com.xin.wanandroid.ui.square

import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.xin.wanandroid.R
import com.xin.wanandroid.base.BaseSimpleFragment
import com.xin.wanandroid.ui.common.ViewPagerCommonAdapter
import com.xin.wanandroid.ui.square.answer.AnswerFragment
import com.xin.wanandroid.ui.square.knowledge.KnowledgeFragment
import com.xin.wanandroid.ui.square.navigation.NavigationFragment
import com.xin.wanandroid.ui.square.squares.SquareArticleFragment
import kotlinx.android.synthetic.main.fragment_square.*

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
 * @date : 2020/9/13 17:34
 * @desc :
 * @since : xinxiniscool@gmail.com
 */
class SquareFragment : BaseSimpleFragment(), ViewPager.OnPageChangeListener {

    private val adapter by lazy {
        ViewPagerCommonAdapter(
            childFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
    }
    private var page = 0
    private var list: ArrayList<String> = ArrayList()

    override fun initLayoutView(): Int = R.layout.fragment_square

    override fun initEvent() {
        vpSquare.adapter = adapter
        vpSquare.currentItem = page
        vpSquare.addOnPageChangeListener(this)
    }

    override fun initData() {
    }

    override fun lazyLoadData() {
        list.add("广场")
        list.add("导航")
        list.add("知识体系")
        list.add("问答")
        adapter.addFragment(SquareArticleFragment())
        adapter.addFragment(NavigationFragment())
        adapter.addFragment(KnowledgeFragment())
        adapter.addFragment(AnswerFragment())
        adapter.apply {
            list.forEach {
                tabSquare.addTab(tabSquare.newTab().setText(it))
                addTitle(it)
            }
        }
        tabSquare.setupWithViewPager(vpSquare)
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        page = position
    }
}