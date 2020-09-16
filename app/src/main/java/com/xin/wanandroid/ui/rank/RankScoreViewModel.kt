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

import androidx.lifecycle.MutableLiveData
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.xin.wanandroid.base.BaseViewModel
import com.xin.wanandroid.core.bean.RankData

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
class RankScoreViewModel : BaseViewModel() {

    val rankData: MutableLiveData<MutableList<RankData>> = MutableLiveData()
    val refreshData: MutableLiveData<RefreshState> = MutableLiveData()
    private var page = 0

    fun getRankData() {
        request(
            {
                page = 1
                val data = mApiRepository.getRankList(page)
                page = data.curPage + 1
                rankData.value = mutableListOf<RankData>().apply {
                    addAll(data.datas)
                }
            },
            isShowDialog = rankData.value.isNullOrEmpty()
        )
    }

    fun getMoreRankData() {
        request(
            {
                val data = mApiRepository.getRankList(page)
                page = data.curPage + 1
                val list = rankData.value ?: mutableListOf()
                list.addAll(data.datas)
                rankData.value = list
                refreshData.value = if (data.offset >= data.total) {
                    RefreshState.None
                } else {
                    RefreshState.LoadFinish
                }
            }
        )
    }

}