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

package com.xin.wanandroid.ui.login

import androidx.lifecycle.Observer
import com.kongzue.dialog.v3.TipDialog
import com.xin.wanandroid.R
import com.xin.wanandroid.base.BaseVMActivity
import com.xin.wanandroid.ext.setOnNoRepeatClickListener
import com.xin.wanandroid.ext.startActivity
import com.xin.wanandroid.ui.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*
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
 *@date : 2020/9/11 16:28
 *@since : xinxiniscool@gmail.com
 *@desc :
 */
class LoginActivity : BaseVMActivity<LoginViewModel>() {

    override fun getViewModelClass(): Class<LoginViewModel> = LoginViewModel::class.java

    override fun initLayoutView(): Int = R.layout.activity_login

    override fun initEvent() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        tvRegister.setOnNoRepeatClickListener {
            startActivity<RegisterActivity>()
        }
        btLogin.setOnNoRepeatClickListener {
            val loginAccount = etLoginAccount.text.toString()
            val loginPassword = etLoginPassword.text.toString()
            when {
                loginAccount.isEmpty() -> showErrorDialog("账号不能为空")
                loginPassword.isEmpty() -> showErrorDialog("密码不能为空")
                else -> mViewModel.login(loginAccount, loginPassword)
            }
        }
    }

    override fun initData() {
        mViewModel.apply {
            loginStatus.observe(this@LoginActivity, Observer {
                if (it) {
                    showTypeDialog("登录成功", TipDialog.TYPE.SUCCESS)
                    finish()
                }
            })
        }
    }
}