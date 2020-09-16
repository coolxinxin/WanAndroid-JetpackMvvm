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

import android.annotation.SuppressLint
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.xin.wanandroid.R
import com.xin.wanandroid.base.BaseVMActivity
import kotlinx.android.synthetic.main.activity_person_rank.*
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.header_person_rank.*

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
 *@date : 2020/9/14 14:53
 *@since : xinxiniscool@gmail.com
 *@desc :
 */
class PersonRankActivity : BaseVMActivity<PersonRankViewModel>() {

    private val adapter by lazy { PersonRankAdapter() }

    override fun getViewModelClass(): Class<PersonRankViewModel> = PersonRankViewModel::class.java

    override fun initLayoutView(): Int = R.layout.activity_person_rank

    @SuppressLint("InflateParams")
    override fun initEvent() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.apply {
            tvToolbarTitle.text  = "我的积分"
            setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
            setNavigationOnClickListener {
                onBackPressed()
            }
        }
        val mHeaderGroup =
            LayoutInflater.from(this).inflate(R.layout.header_person_rank, null) as ConstraintLayout
        adapter.addHeaderView(mHeaderGroup)
        rvPersonRank.adapter = adapter
        srfPersonRank.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mViewModel.getMoreUserRankInfo()
                refreshLayout.finishLoadMore()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mViewModel.getUserRankInfo()
                refreshLayout.finishRefresh()
            }

        })
    }

    override fun initData() {
        mViewModel.apply {
            getUserRankInfo()
            userRank.observe(this@PersonRankActivity, Observer {
                val score = "积分:${it.coinCount}"
                val level = "等级:${it.level}"
                val rank = "排名:${it.rank}"
                tvScores.text = score
                tvLevel.text = level
                tvRank.text = rank
            })
            userRankInfo.observe(this@PersonRankActivity, Observer {
                adapter.setList(it)
            })

            refreshData.observe(this@PersonRankActivity, Observer {
                when (it) {
                    RefreshState.None -> srfPersonRank.finishLoadMoreWithNoMoreData()
                    RefreshState.LoadFinish -> srfPersonRank.finishLoadMore()
                    else -> return@Observer
                }
            })
        }
    }
}