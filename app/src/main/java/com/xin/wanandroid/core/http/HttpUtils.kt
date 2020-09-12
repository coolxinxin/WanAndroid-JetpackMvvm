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

package com.xin.wanandroid.core.http

import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.xin.wanandroid.BuildConfig
import com.xin.wanandroid.app.MyApp
import com.xin.wanandroid.core.Constant
import com.xin.wanandroid.core.http.api.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

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
 *@date : 2020/9/10 10:31
 *@since : xinxiniscool@gmail.com
 *@desc :
 */
object HttpUtils {

    val api: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .client(getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return@lazy retrofit.create(ApiService::class.java)
    }

    private val cookieJar =
        PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(MyApp.getInstance()))
    private val logInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
    //    var httpNetInterceptor: HttpNetInterceptor = HttpNetInterceptor()

    private fun getClient(): OkHttpClient {
        logInterceptor.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        return OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS)
            .writeTimeout(55, TimeUnit.SECONDS)
            .addInterceptor(logInterceptor)
//        .addNetworkInterceptor(httpNetInterceptor)
            .cookieJar(cookieJar)
            .build()
    }

}