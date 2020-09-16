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

package com.xin.wanandroid.core.repository

import com.xin.wanandroid.core.http.HttpUtils

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
 *@date : 2020/9/10 11:43
 *@since : xinxiniscool@gmail.com
 *@desc :
 */
class ApiRepository {

    suspend fun getBannerData() = HttpUtils.api.getBannerData().apiData()

    suspend fun getArticleData(page: Int) = HttpUtils.api.getArticleData(page).apiData()

    suspend fun getTopArticleList() = HttpUtils.api.getTopArticleList().apiData()

    suspend fun register(username: String, password: String, rePassword: String) =
        HttpUtils.api.register(username, password, rePassword).apiData()

    suspend fun login(username: String, password: String) =
        HttpUtils.api.login(username, password).apiData()

    suspend fun logout() = HttpUtils.api.logout().apiData()

    suspend fun collect(id: Int) = HttpUtils.api.collect(id).apiData()

    suspend fun collect(title: String, auth: String, link: String) =
        HttpUtils.api.collect(title, auth, link).apiData()

    suspend fun unCollect(id: Int) = HttpUtils.api.unCollect(id).apiData()

    suspend fun getCollectList(page: Int) = HttpUtils.api.getCollectList(page).apiData()

    suspend fun cancelCollect(id: Int) = HttpUtils.api.cancelCollect(id).apiData()

    suspend fun getKnowledgeData() = HttpUtils.api.getKnowledgeData().apiData()

    suspend fun getKnowledgeArticleData(page: Int, cid: Int) =
        HttpUtils.api.getKnowledgeArticleData(page, cid).apiData()

    suspend fun getNavigationData() = HttpUtils.api.getNavigationData().apiData()

    suspend fun getProjectData() = HttpUtils.api.getProjectData().apiData()

    suspend fun getProjectListData(page: Int, cid: Int) =
        HttpUtils.api.getProjectListData(page, cid).apiData()

    suspend fun getSquareData(page: Int) = HttpUtils.api.getSquareData(page).apiData()

    suspend fun getAnswerData(page: Int) = HttpUtils.api.getAnswerData(page).apiData()

    suspend fun getWeChatData() = HttpUtils.api.getWeChatData().apiData()

    suspend fun getWeChatListData(id: Int, page: Int) =
        HttpUtils.api.getWeChatListData(id, page).apiData()

    suspend fun getWeChatSearchData(id: Int, page: Int, k: String) =
        HttpUtils.api.getWeChatSearchData(id, page, k).apiData()

    suspend fun getHotKeyData() = HttpUtils.api.getHotKeyData().apiData()

    suspend fun queryData(page: Int, k: String) = HttpUtils.api.queryData(page, k).apiData()

    suspend fun getWebsiteData() = HttpUtils.api.getWebsiteData().apiData()

    suspend fun getUserRank() = HttpUtils.api.getUserRank().apiData()

    suspend fun getUserRankInfo(page: Int) = HttpUtils.api.getUserRankInfo(page).apiData()

    suspend fun getRankList(page: Int) = HttpUtils.api.getRankList(page).apiData()

}