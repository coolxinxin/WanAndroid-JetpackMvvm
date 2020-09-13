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
import androidx.lifecycle.Observer
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.just.agentweb.WebChromeClient
import com.kongzue.dialog.util.DialogSettings
import com.kongzue.dialog.v3.ShareDialog
import com.xin.wanandroid.R
import com.xin.wanandroid.base.BaseVMActivity
import com.xin.wanandroid.core.Constant
import com.xin.wanandroid.ext.html
import com.xin.wanandroid.ext.user
import com.xin.wanandroid.util.LiveBus
import kotlinx.android.synthetic.main.activity_webview.*
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
 *@date : 2020/9/12 15:43
 *@since : xinxiniscool@gmail.com
 *@desc :
 */
class WebViewActivity : BaseVMActivity<WebViewModel>() {

    private lateinit var mAgentWeb: AgentWeb
    private var url: String? = ""
    private var title: String? = ""
    private var id: Int = -1
    private var isCollect: Boolean = false
    private var mItemCollect: MenuItem? = null

    override fun getViewModelClass(): Class<WebViewModel> = WebViewModel::class.java

    override fun initLayoutView(): Int = R.layout.activity_webview

    override fun initEvent() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
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
        LiveBus.observe<Pair<Int, Boolean>>(Constant.COLLECT_STATUS, this, Observer {
            collectStatus(it.second)
        })
        LiveBus.observe<Boolean>(Constant.LOGIN_STATUS, this, Observer {
            if (it) {
                user?.collectIds?.let { it1 ->
                    collectStatus(it1.contains(id))
                }
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_article_common, menu)
        mItemCollect = menu?.findItem(R.id.item_collect)
        collectStatus(isCollect)
        if (id == -1) {
            mItemCollect?.isVisible = false
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun collectStatus(isCollect: Boolean) {
        if (isCollect) {
            mItemCollect?.title = "取消收藏"
            mItemCollect?.setIcon(R.drawable.icon_like)
        } else {
            mItemCollect?.title = "收藏"
            mItemCollect?.setIcon(R.drawable.icon_unlike)
        }
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
                try {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.putExtra(
                        Intent.EXTRA_TEXT, getString(
                            R.string.share_type_url,
                            getString(R.string.app_name), title, url
                        )
                    )
                    intent.type = "text/plain"
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                    showErrorDialog("未安装手机${item.text}或安装的版本不支持")
                }
                false
            }.show()
    }

    private fun collectEvent() {
        if (isCollect) {
            mViewModel.unCollect(id)
        } else {
            mViewModel.collect(id)
        }
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