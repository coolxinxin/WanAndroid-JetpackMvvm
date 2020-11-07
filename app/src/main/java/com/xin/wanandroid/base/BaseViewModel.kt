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

package com.xin.wanandroid.base

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.JsonParseException
import com.xin.wanandroid.core.Constant
import com.xin.wanandroid.core.http.AppException
import com.xin.wanandroid.core.repository.ApiRepository
import com.xin.wanandroid.ext.user
import com.xin.wanandroid.util.LiveBus
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

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
 *@date : 2020/9/9 19:11
 *@since : xinxiniscool@gmail.com
 *@desc :
 */
open class BaseViewModel : ViewModel() {

    companion object {
        const val TAG = "BaseViewModel"
    }

    val mApiRepository by lazy { ApiRepository() }

    //不喜欢使用Bus可以将数据放到Base中共享
    val collect: MutableLiveData<Pair<Int, Boolean>> = MutableLiveData()

    private lateinit var mOnDialogListener: OnDialogListener

    protected fun request(
        block: suspend () -> Unit,
        error: (suspend (Exception) -> Unit)? = null,
        isShowDialog: Boolean = false,
        dialogMsg: String = "小鑫正在为你努力加载中"
    ): Job {
        return viewModelScope.launch {
            try {
                if (isShowDialog) {
                    mOnDialogListener.onShowDialog?.invoke(dialogMsg)
                }
                block()
                if (isShowDialog) {
                    mOnDialogListener.onCloseDialog?.invoke()
                }
            } catch (e: Exception) {
                Log.e(TAG, "request:$e")
//                mOnDialogListener.onShowErrorDialog?.invoke("请求异常${e.message}...")
                error?.invoke(e)
                showErrorToast(e)
            }
        }
    }

    protected fun <T> async(block: suspend () -> T): Deferred<T> {
        return viewModelScope.async { block() }
    }

    fun registerListener(onDialogListener: OnDialogListener.() -> Unit) {
        mOnDialogListener = OnDialogListener().also(onDialogListener)
    }

    private fun showErrorToast(e: Exception) {
        when (e) {
            is AppException -> {
                mOnDialogListener.onShowErrorDialog?.invoke(e.message)
                when (e.code) {
                    -1001 -> {
                        //登录失效,将登录状态标记为fail
                        user = null
                        LiveBus.post(Constant.LOGIN_STATUS, false)
                        //因为弹出框的时候立马就进入了登录界面,不想延时进入,就先弹个Toast提示一下(非常不情愿的加上)
                        ToastUtils.showShort(e.message)
                    }
                }
            }
            is ConnectException, is SocketTimeoutException, is UnknownHostException, is HttpException ->
                mOnDialogListener.onShowErrorDialog?.invoke("网络请求失败")
            is JsonParseException ->
                mOnDialogListener.onShowErrorDialog?.invoke("数据解析失败")
            else -> e.message?.let { mOnDialogListener.onShowErrorDialog?.invoke(it) }
        }
    }


    inner class OnDialogListener {
        internal var onShowDialog: ((String) -> Unit)? = null

        internal var onCloseDialog: (() -> Unit)? = null

        internal var onShowErrorDialog: ((String) -> Unit)? = null

        fun showTipDialog(block: (String) -> Unit) {
            onShowDialog = block
        }

        fun closeTipDialog(block: () -> Unit) {
            onCloseDialog = block
        }

        fun showErrorDialog(block: (String) -> Unit) {
            onShowErrorDialog = block
        }

    }

//    interface OnDialogListener {
//        fun showTipDialog(msg: String)
//
//        fun closeTipDialog()
//
//        fun showErrorDialog(errorMsg: String)
//    }

}