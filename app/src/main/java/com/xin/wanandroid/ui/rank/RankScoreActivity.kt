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

package com.xin.wanandroid.ui.rank

import androidx.lifecycle.Observer
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.xin.wanandroid.R
import com.xin.wanandroid.base.BaseVMActivity
import kotlinx.android.synthetic.main.activity_rank_score.*
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
 *@date : 2020/9/14 16:23
 *@since : xinxiniscool@gmail.com
 *@desc :
 */
class RankScoreActivity : BaseVMActivity<RankScoreViewModel>() {

    private val adapter by lazy { RankScoreAdapter() }

    override fun getViewModelClass(): Class<RankScoreViewModel> = RankScoreViewModel::class.java

    override fun initLayoutView(): Int = R.layout.activity_rank_score

    override fun initEvent() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.apply {
            tvToolbarTitle.text  = "积分排行"
            setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
            setNavigationOnClickListener {
                onBackPressed()
            }
        }
        srfRankScore.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mViewModel.getMoreRankData()
                refreshLayout.finishLoadMore()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mViewModel.getRankData()
                refreshLayout.finishRefresh()
            }

        })
        rvRankScore.adapter = adapter
    }

    override fun initData() {
        mViewModel.apply {
            getRankData()
            refreshData.observe(this@RankScoreActivity, Observer {
                when (it) {
                    RefreshState.None -> srfRankScore.finishLoadMoreWithNoMoreData()
                    RefreshState.LoadFinish -> srfRankScore.finishLoadMore()
                    else -> return@Observer
                }
            })
            rankData.observe(this@RankScoreActivity, Observer {
                adapter.setList(it)
            })
        }
    }
}