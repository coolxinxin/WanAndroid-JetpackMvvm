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

package com.xin.wanandroid.ui.collect

import androidx.lifecycle.Observer
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.xin.wanandroid.R
import com.xin.wanandroid.base.BaseVMActivity
import com.xin.wanandroid.core.Constant
import com.xin.wanandroid.ext.startActivity
import com.xin.wanandroid.ui.common.ArticleCommonAdapter
import com.xin.wanandroid.ui.webview.WebViewActivity
import com.xin.wanandroid.util.LiveBus
import kotlinx.android.synthetic.main.activity_collect.*
import kotlinx.android.synthetic.main.common_toolbar.*

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
 *@date : 2020/9/14 17:07
 *@since : xinxiniscool@gmail.com
 *@desc :
 */
class CollectActivity : BaseVMActivity<CollectViewModel>() {

    private val adapter by lazy { ArticleCommonAdapter() }

    override fun getViewModelClass(): Class<CollectViewModel> = CollectViewModel::class.java

    override fun initLayoutView(): Int = R.layout.activity_collect

    override fun initEvent() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.apply {
            tvToolbarTitle.text = "我的收藏"
            setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
            setNavigationOnClickListener {
                onBackPressed()
            }
        }
        srfCollect.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mViewModel.getMoreCollectList()
                refreshLayout.finishLoadMore()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mViewModel.getCollectList()
                refreshLayout.finishRefresh()
            }

        })
        adapter.apply {
            setType(ArticleCommonAdapter.Type.COLLECT)
            setOnItemChildClickListener { _, view, position ->
                val data = getItem(position)
                if (view.id == R.id.ivLike) {
                    mViewModel.unCollect(data.originId)
                    removeAt(position)
                }
            }
            setOnItemClickListener { _, _, position ->
                startActivity<WebViewActivity> {
                    putExtra(Constant.ARTICLE_URL, getItem(position).link)
                    putExtra(Constant.ARTICLE_TITLE, getItem(position).title)
                    putExtra(Constant.ARTICLE_ID, getItem(position).originId)
                    putExtra(Constant.ARTICLE_COLLECT, true)
                }
            }
            rvCollect.adapter = adapter
        }
    }

    override fun initData() {
        mViewModel.apply {
            getCollectList()
            articleData.observe(this@CollectActivity, Observer {
                adapter.setList(it)
            })

            refreshData.observe(this@CollectActivity, Observer {
                when (it) {
                    RefreshState.None -> srfCollect.finishLoadMoreWithNoMoreData()
                    RefreshState.LoadFinish -> srfCollect.finishLoadMore()
                    else -> return@Observer
                }
            })

            LiveBus.observe<Pair<Int, Boolean>>(
                Constant.COLLECT_STATUS,
                this@CollectActivity,
                Observer { it1 ->
                    val position = adapter.data.indexOfFirst { it.originId == it1.first }
                    if (position != -1) adapter.removeAt(position)
                })
        }
    }
}