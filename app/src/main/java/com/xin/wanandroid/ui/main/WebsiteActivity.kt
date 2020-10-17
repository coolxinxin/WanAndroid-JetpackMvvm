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

package com.xin.wanandroid.ui.main

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import com.xin.wanandroid.R
import com.xin.wanandroid.base.BaseVMActivity
import com.xin.wanandroid.core.Constant
import com.xin.wanandroid.core.bean.HotKeyData
import com.xin.wanandroid.ext.isVisible
import com.xin.wanandroid.ext.setOnNoRepeatClickListener
import com.xin.wanandroid.ext.startActivity
import com.xin.wanandroid.ui.webview.WebViewActivity
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.activity_website.*
import kotlinx.android.synthetic.main.common_reload.*


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
 *@date : 2020/9/14 14:19
 *@since : xinxiniscool@gmail.com
 *@desc :
 */
class WebsiteActivity : BaseVMActivity<WebsiteViewModel>() {

    override fun getViewModelClass(): Class<WebsiteViewModel> = WebsiteViewModel::class.java

    override fun initLayoutView(): Int = R.layout.activity_website

    override fun initEvent() {
        btReload.setOnNoRepeatClickListener {
            mViewModel.getWebsiteData()
        }
    }

    override fun initData() {
        mViewModel.apply {
            getWebsiteData()
            hotKeyData.observe(this@WebsiteActivity, Observer {
                val adapter = object : TagAdapter<HotKeyData>(it) {
                    override fun getView(
                        parent: FlowLayout,
                        position: Int,
                        content: HotKeyData
                    ): View {
                        val tvHistory = LayoutInflater.from(this@WebsiteActivity).inflate(
                            R.layout.item_history,
                            parent, false
                        ) as TextView
                        tvHistory.text = content.name
                        return tvHistory
                    }
                }
                flWebsite.adapter = adapter
                flWebsite.setOnTagClickListener { _, position, _ ->
                    startActivity<WebViewActivity> {
                        putExtra(Constant.ARTICLE_URL, it[position].link)
                        putExtra(Constant.ARTICLE_TITLE, it[position].name)
                    }
                    finish()
                    true
                }
            })
            isReload.observe(this@WebsiteActivity, Observer {
                reloadWebsite.isVisible = it
            })
        }
    }
}