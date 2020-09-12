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

package com.xin.wanandroid.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.xin.wanandroid.R
import com.xin.wanandroid.base.BaseVMFragment
import com.xin.wanandroid.core.Constant
import com.xin.wanandroid.core.bean.BannerData
import com.xin.wanandroid.ext.isVisible
import com.xin.wanandroid.ext.setOnNoRepeatClickListener
import com.xin.wanandroid.ext.startActivity
import com.xin.wanandroid.ui.webview.WebViewActivity
import com.xin.wanandroid.util.ImageLoader
import com.xin.wanandroid.util.LiveBus
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.common_reload.*
import java.util.*

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
 *@date : 2020/9/10 9:33
 *@since : xinxiniscool@gmail.com
 *@desc :
 */
class HomeFragment : BaseVMFragment<HomeViewModel>() {

    private lateinit var mAdapter: HomeAdapter
    private lateinit var mBanner: Banner

    override fun getViewModelClass(): Class<HomeViewModel> = HomeViewModel::class.java

    override fun initLayoutView(): Int = R.layout.fragment_home

    @SuppressLint("InflateParams")
    override fun initEvent() {
        mAdapter = HomeAdapter(null).apply {
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
        val mHeaderGroup =
            LayoutInflater.from(mActivity).inflate(R.layout.head_banner, null) as LinearLayout
        mBanner = mHeaderGroup.findViewById(R.id.banner)
        mHeaderGroup.removeView(mBanner)
        mAdapter.addHeaderView(mBanner)
        rvHomeArticle.apply {
            layoutManager = LinearLayoutManager(mActivity)
            setHasFixedSize(true)
        }.adapter = mAdapter
        srfHome.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mViewModel.getMoreHomeData()
                refreshLayout.finishLoadMore()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mViewModel.getBannerData()
                mViewModel.getHomeData()
                refreshLayout.finishRefresh()
            }

        })
        btnReload.setOnNoRepeatClickListener {
            mViewModel.getHomeData()
        }
    }

    override fun initData() {
        mViewModel.apply {
            articleData.observe(this@HomeFragment, Observer {
                mAdapter.setList(it)
            })

            bannerData.observe(this@HomeFragment, Observer {
                setBanner(it)
            })

            refreshData.observe(this@HomeFragment, Observer {
                when (it) {
                    RefreshState.None -> srfHome.finishLoadMoreWithNoMoreData()
                    RefreshState.LoadFinish -> srfHome.finishLoadMore()
                    else -> return@Observer
                }
            })

            isReload.observe(this@HomeFragment, Observer {
                reload.isVisible = it
            })

            LiveBus.observe<Boolean>(Constant.LOGIN_STATUS, this@HomeFragment, Observer {
                updateCollectListState(it)
            })

            LiveBus.observe<Pair<Int, Boolean>>(
                Constant.COLLECT_STATUS,
                this@HomeFragment,
                Observer {
                    updateCollectState(it)
                })
        }
    }

    override fun lazyLoadData() {
        mViewModel.getBannerData()
        mViewModel.getHomeData()
    }

    private fun setBanner(bannerDataList: MutableList<BannerData>) {
        val bannerImages: MutableList<String> = ArrayList()
        val bannerTitles: MutableList<String> = ArrayList()
        val bannerUrl: MutableList<String> = ArrayList()
        for (homeBanner in bannerDataList) {
            bannerImages.add(homeBanner.imagePath)
            bannerTitles.add(homeBanner.title)
            bannerUrl.add(homeBanner.url)
        }
        mBanner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
        mBanner.setImageLoader(ImageLoader())
        mBanner.setImages(bannerImages)
        mBanner.setBannerAnimation(Transformer.DepthPage)
        mBanner.setBannerTitles(bannerTitles)
        mBanner.isAutoPlay(true)
        mBanner.setDelayTime(bannerDataList.size * 400)
        mBanner.setIndicatorGravity(BannerConfig.CENTER)
        mBanner.setOnBannerListener { position: Int ->
            mActivity.startActivity<WebViewActivity> {
                putExtra(Constant.ARTICLE_URL, bannerUrl[position])
                putExtra(Constant.ARTICLE_TITLE, bannerTitles[position])
            }
        }
        mBanner.start()
    }

    override fun onStart() {
        super.onStart()
        mBanner.startAutoPlay()
    }

    override fun onStop() {
        mBanner.stopAutoPlay()
        super.onStop()
    }
}