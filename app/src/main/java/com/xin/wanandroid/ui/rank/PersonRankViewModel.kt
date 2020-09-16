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
import com.xin.wanandroid.core.bean.DataXX
import com.xin.wanandroid.core.bean.UserRank

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
class PersonRankViewModel : BaseViewModel() {

    val userRank: MutableLiveData<UserRank> = MutableLiveData()
    val userRankInfo: MutableLiveData<MutableList<DataXX>> = MutableLiveData()
    val refreshData: MutableLiveData<RefreshState> = MutableLiveData()
    private var page = 1

    fun getUserRankInfo() {
        request(
            {
                page = 1
                val userRankData = async { mApiRepository.getUserRank() }
                val userRankInfoData = async { mApiRepository.getUserRankInfo(page) }
                userRank.value = userRankData.await()
                val data = userRankInfoData.await()
                page = data.curPage + 1
                userRankInfo.value = mutableListOf<DataXX>().apply {
                    addAll(data.datas)
                }
            },
            isShowDialog = userRankInfo.value.isNullOrEmpty()
        )
    }

    fun getMoreUserRankInfo() {
        request(
            {
                val data = mApiRepository.getUserRankInfo(page)
                page = data.curPage + 1
                val list = userRankInfo.value ?: mutableListOf()
                list.addAll(data.datas)
                userRankInfo.value = list
                refreshData.value = if (data.offset >= data.total) {
                    RefreshState.None
                } else {
                    RefreshState.LoadFinish
                }
            }
        )
    }
}