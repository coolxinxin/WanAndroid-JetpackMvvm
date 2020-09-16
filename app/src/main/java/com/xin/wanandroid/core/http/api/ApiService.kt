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

package com.xin.wanandroid.core.http.api

import com.xin.wanandroid.core.bean.*
import retrofit2.http.*

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
 *@date : 2020/9/10 10:25
 *@since : xinxiniscool@gmail.com
 *@desc :
 */
interface ApiService {

    @GET("/banner/json")
    suspend fun getBannerData(): ApiResults<MutableList<BannerData>>

    @GET("/article/list/{page}/json")
    suspend fun getArticleData(@Path("page") page: Int): ApiResults<ArticleData>

    @GET("/article/top/json")
    suspend fun getTopArticleList(): ApiResults<List<DataX>>

    @FormUrlEncoded
    @POST("/user/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") rePassword: String
    ): ApiResults<User>

    @FormUrlEncoded
    @POST("/user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): ApiResults<User>

    @GET("/user/logout/json")
    suspend fun logout(): ApiResults<*>

    @POST("lg/collect/{id}/json")
    suspend fun collect(@Path("id") id: Int): ApiResults<*>

    @FormUrlEncoded
    @POST("lg/collect/add/json")
    suspend fun collect(
        @Field("title") title: String,
        @Field("author") author: String,
        @Field("link") link: String
    ): ApiResults<ArticleData>

    @POST("lg/uncollect_originId/{id}/json")
    suspend fun unCollect(@Path("id") id: Int): ApiResults<*>

    @GET("lg/collect/list/{page}/json")
    suspend fun getCollectList(@Path("page") page: Int): ApiResults<ArticleData>

    @POST("lg/uncollect/{id}/json")
    suspend fun cancelCollect(@Path("id") id: Int): ApiResults<*>

    @GET("/tree/json")
    suspend fun getKnowledgeData(): ApiResults<MutableList<KnowledgeData>>

    @GET("/article/list/{page}/json")
    suspend fun getKnowledgeArticleData(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): ApiResults<ArticleData>

    @GET("/navi/json")
    suspend fun getNavigationData(): ApiResults<MutableList<NavigationData>>

    @GET("project/tree/json")
    suspend fun getProjectData(): ApiResults<MutableList<ProjectData>>

    @GET("project/list/{page}/json")
    suspend fun getProjectListData(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): ApiResults<ArticleData>

    @GET("/user_article/list/{page}/json")
    suspend fun getSquareData(@Path("page") page: Int): ApiResults<ArticleData>

    @GET("/wenda/list/{page}/json ")
    suspend fun getAnswerData(@Path("page") page: Int): ApiResults<ArticleData>

    @GET("wxarticle/chapters/json")
    suspend fun getWeChatData(): ApiResults<MutableList<WeChatData>>

    @GET("wxarticle/list/{id}/{page}/json")
    suspend fun getWeChatListData(
        @Path("id") id: Int,
        @Path("page") page: Int
    ): ApiResults<ArticleData>

    @GET("wxarticle/list/{id}/{page}/json")
    suspend fun getWeChatSearchData(
        @Path("id") id: Int,
        @Path("page") page: Int,
        @Query("k") k: String
    ): ApiResults<ArticleData>

    @GET("/hotkey/json")
    suspend fun getHotKeyData(): ApiResults<MutableList<HotKeyData>>

    @FormUrlEncoded
    @POST("/article/query/{page}/json")
    suspend fun queryData(@Path("page") page: Int, @Field("k") k: String): ApiResults<ArticleData>

    @GET("/friend/json")
    suspend fun getWebsiteData(): ApiResults<MutableList<HotKeyData>>

    @GET("lg/coin/userinfo/json")
    suspend fun getUserRank(): ApiResults<UserRank>

    @GET("/lg/coin/list/{page}/json")
    suspend fun getUserRankInfo(@Path("page") page: Int): ApiResults<UserRankInfo>

    @GET("/coin/rank/{page}/json")
    suspend fun getRankList(@Path("page") page: Int): ApiResults<RankListData>
}