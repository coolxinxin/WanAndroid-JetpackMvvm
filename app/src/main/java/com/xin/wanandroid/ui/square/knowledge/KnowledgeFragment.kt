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

package com.xin.wanandroid.ui.square.knowledge

import androidx.lifecycle.Observer
import com.xin.wanandroid.R
import com.xin.wanandroid.base.BaseVMFragment
import com.xin.wanandroid.core.Constant
import com.xin.wanandroid.ext.isVisible
import com.xin.wanandroid.ext.setOnNoRepeatClickListener
import com.xin.wanandroid.ext.startActivity
import com.xin.wanandroid.ui.square.knowledge.knowledgeArticle.KnowledgeArticleActivity
import kotlinx.android.synthetic.main.common_reload.*
import kotlinx.android.synthetic.main.fragment_knowledge.*

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
 *@date : 2020/9/10 9:41
 *@since : xinxiniscool@gmail.com
 *@desc :
 */
class KnowledgeFragment : BaseVMFragment<KnowledgeViewModel>() {

    private lateinit var mAdapter: KnowledgeAdapter

    override fun getViewModelClass(): Class<KnowledgeViewModel> = KnowledgeViewModel::class.java

    override fun initLayoutView(): Int = R.layout.fragment_knowledge

    override fun initEvent() {
        mAdapter = KnowledgeAdapter().apply {
            setOnItemClickListener { _, _, position ->
                mActivity.startActivity<KnowledgeArticleActivity> {
                    putExtra(Constant.KNOWLEDGE_DATA,getItem(position))
                }
            }
        }
        rvKnowledge.adapter = mAdapter
        srfKnowledge.setOnRefreshListener {
            mViewModel.getKnowledgeData()
            it.finishRefresh()
        }
        srfKnowledge.setEnableLoadMore(false)
        btReload.setOnNoRepeatClickListener {
            mViewModel.getKnowledgeData()
        }
    }

    override fun initData() {
        mViewModel.apply {
            knowledgeData.observe(this@KnowledgeFragment, Observer {
                mAdapter.setList(it)
            })
            isReload.observe(this@KnowledgeFragment, Observer {
                reloadKnowledge.isVisible = it
            })
        }
    }

    override fun lazyLoadData() {
        mViewModel.getKnowledgeData()
    }
}