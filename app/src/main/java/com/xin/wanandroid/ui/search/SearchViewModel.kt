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

import androidx.lifecycle.MutableLiveData
import com.xin.wanandroid.base.BaseViewModel
import com.xin.wanandroid.core.bean.HotKeyData
import com.xin.wanandroid.util.DaoUtils

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
 *@date : 2020/9/14 9:42
 *@since : xinxiniscool@gmail.com
 *@desc :
 */
class SearchViewModel : BaseViewModel() {

    val hotKeyData: MutableLiveData<MutableList<HotKeyData>> = MutableLiveData()
    val historyData: MutableLiveData<MutableList<String>> = MutableLiveData()

    fun getHotKeyData() {
        request(
            {
                hotKeyData.value = mApiRepository.getHotKeyData()
            },
            isShowDialog = hotKeyData.value.isNullOrEmpty()
        )
    }

    fun getHistoryData() {
        request(
            {
                historyData.value = DaoUtils.getRecordsList()
            }
        )
    }

    fun addHistoryData(searchResponseStr: String) {
        request({
            DaoUtils.addRecords(searchResponseStr)
        })

    }

    fun clearHistoryData() {
        request({
            DaoUtils.deleteAllRecords()
        })
    }
}