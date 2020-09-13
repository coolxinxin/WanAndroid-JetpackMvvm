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

package com.xin.wanandroid.ui.square.answer

import androidx.lifecycle.Observer
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.xin.wanandroid.R
import com.xin.wanandroid.base.BaseVMFragment
import com.xin.wanandroid.core.Constant
import com.xin.wanandroid.ext.isVisible
import com.xin.wanandroid.ext.setOnNoRepeatClickListener
import com.xin.wanandroid.ext.startActivity
import com.xin.wanandroid.ui.common.ArticleCommonAdapter
import com.xin.wanandroid.ui.webview.WebViewActivity
import com.xin.wanandroid.util.LiveBus
import kotlinx.android.synthetic.main.common_reload.*
import kotlinx.android.synthetic.main.framgne_answer.*

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
 * @date : 2020/9/13 18:10
 * @desc :
 * @since : xinxiniscool@gmail.com
 */
class AnswerFragment : BaseVMFragment<AnswerViewModel>() {

    private lateinit var mAdapter: ArticleCommonAdapter
    
    override fun getViewModelClass(): Class<AnswerViewModel> = AnswerViewModel::class.java

    override fun initLayoutView(): Int = R.layout.framgne_answer

    override fun initEvent() {
        mAdapter = ArticleCommonAdapter().apply {
            setOnItemChildClickListener { _, view, position ->
                val data = getItem(position)
                if (view.id == R.id.ivLike) {
                    if (data.collect) {
                        mViewModel.unCollect(data.id)
                    } else {
                        mViewModel.collect(data.id)
                    }
                }
            }
            setOnItemClickListener { _, _, position ->
                mActivity.startActivity<WebViewActivity> {
                    putExtra(Constant.ARTICLE_URL, getItem(position).link)
                    putExtra(Constant.ARTICLE_TITLE, getItem(position).title)
                    putExtra(Constant.ARTICLE_ID, getItem(position).id)
                    putExtra(Constant.ARTICLE_COLLECT, getItem(position).collect)
                }
            }
        }
        rvAnswer.adapter = mAdapter
        srfAnswer.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mViewModel.getMoreAnswerData()
                refreshLayout.finishLoadMore()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mViewModel.getAnswerData()
                refreshLayout.finishRefresh()
            }

        })
        btReload.setOnNoRepeatClickListener {
            mViewModel.getAnswerData()
        }
    }

    override fun initData() {
        mViewModel.apply {
            articleData.observe(this@AnswerFragment, Observer {
                mAdapter.setList(it)
            })
            isReload.observe(this@AnswerFragment, Observer {
                reloadAnswer.isVisible = it
            })
            refreshData.observe(this@AnswerFragment, Observer {
                when (it) {
                    RefreshState.None -> srfAnswer.finishLoadMoreWithNoMoreData()
                    RefreshState.LoadFinish -> srfAnswer.finishLoadMore()
                    else -> return@Observer
                }
            })
            LiveBus.observe<Boolean>(
                Constant.LOGIN_STATUS,
                this@AnswerFragment,
                Observer {
                    updateCollectListState(it)
                })

            LiveBus.observe<Pair<Int, Boolean>>(
                Constant.COLLECT_STATUS,
                this@AnswerFragment,
                Observer {
                    updateCollectState(it)
                })
        }
    }

    override fun lazyLoadData() {
        mViewModel.getAnswerData()
    }
}