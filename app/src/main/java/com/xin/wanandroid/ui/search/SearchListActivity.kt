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

package com.xin.wanandroid.ui.search

import androidx.lifecycle.Observer
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.xin.wanandroid.R
import com.xin.wanandroid.base.BaseVMActivity
import com.xin.wanandroid.core.Constant
import com.xin.wanandroid.ext.isVisible
import com.xin.wanandroid.ext.setOnNoRepeatClickListener
import com.xin.wanandroid.ext.startActivity
import com.xin.wanandroid.ui.common.ArticleCommonAdapter
import com.xin.wanandroid.ui.webview.WebViewActivity
import com.xin.wanandroid.util.LiveBus
import kotlinx.android.synthetic.main.activity_search_list.*
import kotlinx.android.synthetic.main.common_reload.*

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
 *@date : 2020/9/14 10:58
 *@since : xinxiniscool@gmail.com
 *@desc :
 */
class SearchListActivity : BaseVMActivity<SearchListViewModel>() {

    private var data: String? = ""
    private lateinit var mAdapter: ArticleCommonAdapter

    override fun getViewModelClass(): Class<SearchListViewModel> = SearchListViewModel::class.java

    override fun initLayoutView(): Int = R.layout.activity_search_list

    override fun initEvent() {
        data = intent.getStringExtra(Constant.SEARCH_DATA)
        tvSearchListTitle.text = data
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
                startActivity<WebViewActivity> {
                    putExtra(Constant.ARTICLE_URL, getItem(position).link)
                    putExtra(Constant.ARTICLE_TITLE, getItem(position).title)
                    putExtra(Constant.ARTICLE_ID, getItem(position).id)
                    putExtra(Constant.ARTICLE_COLLECT, getItem(position).collect)
                }
            }
        }
        rvSearchList.adapter = mAdapter
        srfSearchList.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                data?.let { mViewModel.getMoreSearchListData(it) }
                refreshLayout.finishLoadMore()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                data?.let { mViewModel.getSearchListData(it) }
                refreshLayout.finishRefresh()
            }

        })
        btReload.setOnNoRepeatClickListener {
            data?.let { mViewModel.getSearchListData(it) }
        }
    }

    override fun initData() {
        data?.let { mViewModel.getSearchListData(it) }
        mViewModel.apply {
            articleData.observe(this@SearchListActivity, Observer {
                mAdapter.setList(it)
            })
            isReload.observe(this@SearchListActivity, Observer {
                reloadSearchList.isVisible = it
            })
            refreshData.observe(this@SearchListActivity, Observer {
                when (it) {
                    RefreshState.None -> srfSearchList.finishLoadMoreWithNoMoreData()
                    RefreshState.LoadFinish -> srfSearchList.finishLoadMore()
                    else -> return@Observer
                }
            })
            LiveBus.observe<Boolean>(
                Constant.LOGIN_STATUS,
                this@SearchListActivity,
                Observer {
                    updateCollectListState(it)
                })

            LiveBus.observe<Pair<Int, Boolean>>(
                Constant.COLLECT_STATUS,
                this@SearchListActivity,
                Observer {
                    updateCollectState(it)
                })
        }
    }

}