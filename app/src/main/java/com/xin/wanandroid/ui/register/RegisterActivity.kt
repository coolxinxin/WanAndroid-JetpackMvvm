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

package com.xin.wanandroid.ui.register

import androidx.lifecycle.Observer
import com.xin.wanandroid.R
import com.xin.wanandroid.base.BaseVMActivity
import com.xin.wanandroid.ext.setOnNoRepeatClickListener
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.common_toolbar.*

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
 *@date : 2020/9/11 16:40
 *@since : xinxiniscool@gmail.com
 *@desc :
 */
class RegisterActivity : BaseVMActivity<RegisterViewModel>() {

    override fun getViewModelClass(): Class<RegisterViewModel> = RegisterViewModel::class.java

    override fun initLayoutView(): Int = R.layout.activity_register

    override fun initEvent() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        btRegister.setOnNoRepeatClickListener {
            val registerAccount = etRegisterAccount.text.toString()
            val registerPassword = etRegisterPassword.text.toString()
            val registerRePassword = etRegisterRepassword.text.toString()
            when {
                registerAccount.isEmpty() -> showErrorDialog("账号不能为空")
                registerPassword.isEmpty() || registerRePassword.isEmpty() -> showErrorDialog("密码不能为空")
                registerPassword != registerRePassword -> {
                    etRegisterPassword.text?.clear()
                    etRegisterRepassword.text?.clear()
                    showErrorDialog("两次密码不一致,请确定密码")
                }
                else -> mViewModel.register(registerAccount, registerPassword, registerRePassword)
            }
        }
    }

    override fun initData() {
        mViewModel.apply {
            registerState.observe(this@RegisterActivity, Observer {
                if (it) finish()
            })
        }
    }
}