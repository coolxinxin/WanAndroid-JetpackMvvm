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

package com.xin.wanandroid.ui.project.projectList

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
import com.xin.wanandroid.ui.webview.WebViewActivity
import kotlinx.android.synthetic.main.common_reload.*
import kotlinx.android.synthetic.main.fragment_project_list.*

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
 * @date : 2020/9/13 10:57
 * @desc :
 * @since : xinxiniscool@gmail.com
 */
class ProjectListFragment : BaseVMFragment<ProjectListViewModel>() {

    private var cid = -1
    private val adapter by lazy { ProjectListAdapter() }

    override fun getViewModelClass(): Class<ProjectListViewModel> = ProjectListViewModel::class.java

    override fun initLayoutView(): Int = R.layout.fragment_project_list

    fun addCid(cid: Int): ProjectListFragment {
        this.cid = cid
        return this
    }

    override fun initEvent() {
        adapter.apply {
            rvProjectList.adapter = this
            adapter.setOnItemClickListener { _, _, position ->
                mActivity.startActivity<WebViewActivity> {
                    putExtra(Constant.ARTICLE_URL, getItem(position).link)
                    putExtra(Constant.ARTICLE_TITLE, getItem(position).title)
                }
            }
        }
        srfProjectList.setOnRefreshLoadMoreListener(object :OnRefreshLoadMoreListener{
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mViewModel.getMoreProjectListData(cid)
                refreshLayout.finishLoadMore()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mViewModel.getProjectListData(cid)
                refreshLayout.finishRefresh()
            }
        })
        btReload.setOnNoRepeatClickListener {
            mViewModel.getProjectListData(cid)
        }
    }

    override fun initData() {
        mViewModel.apply {
            articleData.observe(this@ProjectListFragment, Observer {
                adapter.setList(it)
            })
            refreshData.observe(this@ProjectListFragment, Observer {
                when (it) {
                    RefreshState.None -> srfProjectList.finishLoadMoreWithNoMoreData()
                    RefreshState.LoadFinish -> srfProjectList.finishLoadMore()
                    else -> return@Observer
                }
            })
            isReload.observe(this@ProjectListFragment, Observer {
                reloadProjectList.isVisible = it
            })
        }
    }

    override fun lazyLoadData() {
        mViewModel.getProjectListData(cid)
    }

}