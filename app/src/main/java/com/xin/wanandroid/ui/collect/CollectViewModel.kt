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

import androidx.lifecycle.MutableLiveData
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.xin.wanandroid.base.BaseViewModel
import com.xin.wanandroid.core.Constant
import com.xin.wanandroid.core.bean.DataX
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
 *@date : 2020/9/14 17:07
 *@since : xinxiniscool@gmail.com
 *@desc :
 */
class CollectViewModel : BaseViewModel() {

    val articleData: MutableLiveData<MutableList<DataX>> = MutableLiveData()

    val refreshData: MutableLiveData<RefreshState> = MutableLiveData()

    val isReload: MutableLiveData<Boolean> = MutableLiveData()

    private var page = 0

    fun getCollectList() {
        isReload.value = false
        request(
            {
                page = 0
                val article = mApiRepository.getCollectList(page)
                page = article.curPage
                articleData.value = mutableListOf<DataX>().apply {
                    addAll(article.datas)
                }
            }, {
                isReload.value = articleData.value.isNullOrEmpty()
            },
            isShowDialog = articleData.value.isNullOrEmpty()
        )
    }

    fun getMoreCollectList() {
        isReload.value = false
        request(
            {
                val article = mApiRepository.getCollectList(page)
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

    fun unCollect(id: Int) {
        request(
            {
                mApiRepository.unCollect(id)
                LiveBus.post(Constant.COLLECT_STATUS, id to false)
            }
        )
    }

}