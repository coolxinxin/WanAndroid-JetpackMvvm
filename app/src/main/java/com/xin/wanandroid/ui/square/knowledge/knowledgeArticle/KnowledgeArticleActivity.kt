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

package com.xin.wanandroid.ui.square.knowledge.knowledgeArticle

import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.xin.wanandroid.R
import com.xin.wanandroid.base.BaseSimpleActivity
import com.xin.wanandroid.core.Constant
import com.xin.wanandroid.core.bean.KnowledgeData
import com.xin.wanandroid.ui.common.ViewPagerCommonAdapter
import kotlinx.android.synthetic.main.activity_knowledge_article.*
import kotlinx.android.synthetic.main.common_toolbar.*

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
 * @date : 2020/9/12 22:46
 * @desc :
 * @since : xinxiniscool@gmail.com
 */
class KnowledgeArticleActivity : BaseSimpleActivity(), ViewPager.OnPageChangeListener {

    private var knowledgeData: KnowledgeData? = null
    private lateinit var adapter: ViewPagerCommonAdapter
    private var page = 0

    override fun initLayoutView(): Int = R.layout.activity_knowledge_article

    override fun initEvent() {
        knowledgeData = intent.getParcelableExtra(Constant.KNOWLEDGE_DATA)
        tvToolbarTitle.text = knowledgeData?.name
        adapter = ViewPagerCommonAdapter(
            supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        vpKnowledge.adapter = adapter
        vpKnowledge.currentItem = page
        vpKnowledge.addOnPageChangeListener(this)
    }

    override fun initData() {
        knowledgeData?.apply {
            children.forEach {
                tabKnowledge.addTab(tabKnowledge.newTab().setText(it.name))
                adapter.apply {
                    addFragment(KnowledgeArticleFragment().setKnowledgeChildrenData(it.id))
                    it.name?.apply { addTitle(this) }
                }
            }
            tabKnowledge.setupWithViewPager(vpKnowledge)
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        page = position
    }

}