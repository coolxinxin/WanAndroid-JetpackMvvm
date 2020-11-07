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

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.xin.wanandroid.core.Constant
import com.xin.wanandroid.ext.startActivity
import com.xin.wanandroid.ui.login.LoginActivity
import com.xin.wanandroid.util.LiveBus

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
 *@date : 2020/9/9 19:05
 *@since : xinxiniscool@gmail.com
 *@desc :
 */
abstract class BaseVMActivity<VM : BaseViewModel> : BaseSimpleActivity() {

    protected lateinit var mViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        createViewModel()
        registerListener()
        observerLoginStatus()
        super.onCreate(savedInstanceState)
    }

    private fun createViewModel() {
        mViewModel = ViewModelProvider(this).get(getViewModelClass())
    }

    private fun observerLoginStatus(){
        LiveBus.observe<Boolean>(Constant.LOGIN_STATUS,this, Observer {
            if (!it){
                startActivity<LoginActivity>()
            }
        })
    }

    private fun registerListener() {
        mViewModel.registerListener {
            showTipDialog {
                this@BaseVMActivity.showTipDialog(it)
            }
            closeTipDialog {
                this@BaseVMActivity.disMissTipDialog()
            }
            showErrorDialog {
                this@BaseVMActivity.showErrorDialog(it)
            }
        }
    }

    protected abstract fun getViewModelClass(): Class<VM>

}