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

import androidx.lifecycle.MutableLiveData
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.xin.wanandroid.base.BaseViewModel
import com.xin.wanandroid.core.bean.DataX

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
class ProjectListViewModel : BaseViewModel() {

    val articleData: MutableLiveData<MutableList<DataX>> = MutableLiveData()

    //只有当请求列表失败的时候并且没有数据在界面上显示才出现
    val isReload: MutableLiveData<Boolean> = MutableLiveData()
    val refreshData: MutableLiveData<RefreshState> = MutableLiveData()

    //观察数据得知curPage需要+1
    private var page = 1

    fun getProjectListData(cid: Int) {
        isReload.value = false
        request(
            {
                page = 1
                val article = mApiRepository.getProjectListData(page, cid)
                page = article.curPage + 1
                articleData.value = mutableListOf<DataX>().apply {
                    addAll(article.datas)
                }
            }, {
                isReload.value = articleData.value.isNullOrEmpty()
            },
            isShowDialog = articleData.value.isNullOrEmpty()
        )
    }

    fun getMoreProjectListData(cid: Int) {
        isReload.value = false
        request(
            {
                val article = mApiRepository.getProjectListData(page, cid)
                page = article.curPage + 1
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
}