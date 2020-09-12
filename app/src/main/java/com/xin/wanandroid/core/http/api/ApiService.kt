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

    @POST("lg/uncollect_originId/{id}/json")
    suspend fun unCollect(@Path("id") id: Int): ApiResults<*>

}