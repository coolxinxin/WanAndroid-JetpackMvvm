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

import androidx.lifecycle.MutableLiveData
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.xin.wanandroid.base.BaseViewModel
import com.xin.wanandroid.core.Constant
import com.xin.wanandroid.core.bean.BannerData
import com.xin.wanandroid.core.bean.DataX
import com.xin.wanandroid.ext.user
import com.xin.wanandroid.util.LiveBus

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
 *@date : 2020/9/10 9:34
 *@since : xinxiniscool@gmail.com
 *@desc :
 */
class HomeViewModel : BaseViewModel() {

    //首页轮播图数据
    val bannerData: MutableLiveData<MutableList<BannerData>> = MutableLiveData()
    val articleData: MutableLiveData<MutableList<DataX>> = MutableLiveData()

    //只有当请求列表失败的时候并且没有数据在界面上显示才出现
    val isReload: MutableLiveData<Boolean> = MutableLiveData()
    val refreshData: MutableLiveData<RefreshState> = MutableLiveData()


    fun getBannerData() {
        request(
            {
                val banner = mApiRepository.getBannerData()
                bannerData.value = banner
            }
        )
    }

    private var page = 0

    fun getHomeData() {
        isReload.value = false
        request(
            {
                val topArticleDeferred = async { mApiRepository.getTopArticleList() }
                val articleDeferred = async { mApiRepository.getArticleData(0) }
                val topArticle = topArticleDeferred.await().apply { forEach { it.isTop = true } }
                val article = articleDeferred.await()
                page = article.curPage
                articleData.value = mutableListOf<DataX>().apply {
                    addAll(topArticle)
                    addAll(article.datas)
                }
            }, {
                isReload.value = articleData.value.isNullOrEmpty()
            }
        )
    }

    fun getMoreHomeData() {
        isReload.value = false
        request(
            {
                val article = mApiRepository.getArticleData(page)
                page = article.curPage
                val list = articleData.value ?: mutableListOf()
                list.addAll(article.datas)
                articleData.value = list
                refreshData.value = if (article.offset >= article.total) {
                    RefreshState.None
                } else {
                    RefreshState.LoadFinish
                }
            }, {
                isReload.value = articleData.value.isNullOrEmpty()
            }
        )
    }

    fun collect(id: Int) {
        request(
            {
                mApiRepository.collect(id)
                LiveBus.post(Constant.COLLECT_STATUS, id to true)
            }
        )
    }

    fun unCollect(id: Int) {
        request(
            {
                mApiRepository.unCollect(id)
                LiveBus.post(Constant.COLLECT_STATUS, id to false)
            }
        )
    }

    fun updateCollectState(pair: Pair<Int, Boolean>) {
        val list = articleData.value
        val data = list?.find { it.id == pair.first } ?: return
        data.collect = pair.second
        articleData.value = list
    }

    fun updateCollectListState(isLogin: Boolean) {
        val list = articleData.value
        if (isLogin) {
            val collectIds = user?.collectIds ?: return
            list?.forEach { it.collect = collectIds.contains(it.id) }
        } else {
            list?.forEach { it.collect = false }
        }
        articleData.value = list
    }
}