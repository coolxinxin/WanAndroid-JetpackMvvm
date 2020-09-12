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

package com.xin.wanandroid.ui.webview

import android.content.Intent
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import android.widget.LinearLayout
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.just.agentweb.WebChromeClient
import com.kongzue.dialog.util.DialogSettings
import com.kongzue.dialog.v3.ShareDialog
import com.xin.wanandroid.R
import com.xin.wanandroid.base.BaseSimpleActivity
import com.xin.wanandroid.core.Constant
import com.xin.wanandroid.ext.html
import kotlinx.android.synthetic.main.activity_webview.*
import kotlinx.android.synthetic.main.common_toolbar.*
import java.util.*

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
 *@date : 2020/9/12 15:43
 *@since : xinxiniscool@gmail.com
 *@desc :
 */
class WebViewActivity : BaseSimpleActivity() {

    private lateinit var mAgentWeb: AgentWeb
    private var url: String? = ""
    private var title: String? = ""
    private var id: Int = -1
    private var isCollect: Boolean = false
    private var mItemCollect: MenuItem? = null

    override fun initLayoutView(): Int = R.layout.activity_webview

    override fun initEvent() {
        setSupportActionBar(toolbar)
        toolbar.apply {
            setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
            setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    override fun initData() {
        url = intent.getStringExtra(Constant.ARTICLE_URL)
        title = intent.getStringExtra(Constant.ARTICLE_TITLE)
        id = intent.getIntExtra(Constant.ARTICLE_ID, -1)
        isCollect = intent.getBooleanExtra(Constant.ARTICLE_COLLECT, false)
        tvToolbarTitle.text = title.html()
        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(webContainer, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)
            .setWebChromeClient(object : WebChromeClient() {
                override fun onReceivedTitle(view: WebView?, title: String?) {
                    tvToolbarTitle.text = title.html()
                    super.onReceivedTitle(view, title)
                }
            })
            .interceptUnkownUrl()
            .createAgentWeb()
            .ready()
            .go(url)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (id != -1) {
            menuInflater.inflate(R.menu.menu_article_common, menu)
            mItemCollect = menu?.findItem(R.id.item_collect)
            if (isCollect) {
                mItemCollect?.title = "取消收藏"
                mItemCollect?.setIcon(R.drawable.icon_like)
            } else {
                mItemCollect?.title = "收藏"
                mItemCollect?.setIcon(R.drawable.icon_unlike)
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * 让菜单同时显示图标和文字
     *
     * @param featureId Feature id
     * @param menu      Menu
     * @return menu if opened
     */
    override fun onMenuOpened(featureId: Int, menu: Menu): Boolean {
        if ("MenuBuilder".equals(menu.javaClass.simpleName, ignoreCase = true)) {
            try {
                val method = menu.javaClass.getDeclaredMethod(
                    "setOptionalIconsVisible",
                    java.lang.Boolean.TYPE
                )
                method.isAccessible = true
                method.invoke(menu, true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return super.onMenuOpened(featureId, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_share -> shareArticle()
            R.id.item_collect -> collectEvent()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareArticle() {
        val itemList: MutableList<ShareDialog.Item> =
            ArrayList()
        itemList.add(ShareDialog.Item(this, R.mipmap.img_qq_ios, "QQ"))
        itemList.add(ShareDialog.Item(this, R.mipmap.img_wechat_ios, "微信"))
        itemList.add(ShareDialog.Item(this, R.mipmap.img_weibo_ios, "微博"))
        ShareDialog.build(this)
            .setStyle(DialogSettings.STYLE.STYLE_IOS)
            .setItems(itemList)
            .setOnItemClickListener { _, _, item ->
                val intent = Intent(Intent.ACTION_SEND)
                //待集成分享通道
                when (item.text) {
                    "QQ" -> {
                        intent.putExtra(
                            Intent.EXTRA_TEXT, getString(
                                R.string.share_type_url,
                                getString(R.string.app_name), title, url
                            )
                        )
                    }
                    "微信" -> {
                        intent.putExtra(
                            Intent.EXTRA_TEXT, getString(
                                R.string.share_type_url,
                                getString(R.string.app_name), title, url
                            )
                        )
                    }
                    else -> {
                        intent.putExtra(
                            Intent.EXTRA_TEXT, getString(
                                R.string.share_type_url,
                                getString(R.string.app_name), title, url
                            )
                        )
                    }
                }
                false
            }.show()
    }

    private fun collectEvent() {

    }

    override fun onResume() {
        super.onResume()
        mAgentWeb.webLifeCycle.onResume()
    }

    override fun onPause() {
        mAgentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            true
        } else super.onKeyDown(keyCode, event)
    }

//    override fun onBackPressed() {
//        if (mAgentWeb.webCreator.webView.canGoBack()) {
//            mAgentWeb.back()
//        } else {
//            super.onBackPressed()
//        }
//    }

    override fun onDestroy() {
        mAgentWeb.webLifeCycle.onDestroy()
        super.onDestroy()
    }

}