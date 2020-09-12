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

package com.xin.wanandroid.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.jeremyliao.liveeventbus.LiveEventBus

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
 *@date : 2020/9/12 10:34
 *@since : xinxiniscool@gmail.com
 *@desc :
 */
object LiveBus {

    /**
     * 消息发布
     */
    inline fun <reified T> post(channel: String, value: T) {
        LiveEventBus.get(channel, T::class.java).post(value)
    }

    /**
     * 消息延时发布
     */
    inline fun <reified T> postDelay(channel: String, delay: Long, value: T) {
        LiveEventBus.get(channel, T::class.java).postDelay(value, delay)
    }


    /**
     * 订阅LiveDataEventBus消息
     */
    inline fun <reified T> observe(channel: String, owner: LifecycleOwner, observer: Observer<T>) {
        LiveEventBus.get(channel, T::class.java).observe(owner, observer)
    }

    /**
     * 应用进程生命周期内订阅LiveDataEventBus消息
     */
    inline fun <reified T> observeForever(channel: String, observer: Observer<T>) {
        LiveEventBus.get(channel, T::class.java).observeForever(observer)
    }

    /**
     * 订阅粘性LiveDataEventBus消息
     */
    inline fun <reified T> observeSticky(
        channel: String,
        owner: LifecycleOwner,
        observer: Observer<T>
    ) {
        LiveEventBus.get(channel, T::class.java).observeSticky(owner, observer)
    }

    /**
     * 应用进程生命周期内订阅粘性LiveDataEventBus消息
     */
    inline fun <reified T> observeStickyForever(
        channel: String,
        observer: Observer<T>
    ) {
        LiveEventBus.get(channel, T::class.java).observeStickyForever(observer)
    }
}