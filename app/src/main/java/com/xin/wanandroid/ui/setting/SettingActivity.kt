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

package com.xin.wanandroid.ui.setting

import android.content.Intent
import android.net.Uri
import android.view.View
import com.blankj.utilcode.util.AppUtils
import com.kongzue.dialog.util.BaseDialog
import com.kongzue.dialog.util.DialogSettings
import com.kongzue.dialog.v3.MessageDialog
import com.kongzue.dialog.v3.TipDialog
import com.xin.wanandroid.R
import com.xin.wanandroid.base.BaseSimpleActivity
import com.xin.wanandroid.core.Constant
import com.xin.wanandroid.ext.setOnNoRepeatClickListener
import com.xin.wanandroid.ext.startActivity
import com.xin.wanandroid.ui.webview.WebViewActivity
import com.xin.wanandroid.util.FileUtils
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.common_toolbar.*
import java.io.File


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
 *@date : 2020/9/15 11:00
 *@since : xinxiniscool@gmail.com
 *@desc :
 */
class SettingActivity : BaseSimpleActivity() {

    private lateinit var mFile: File

    override fun initLayoutView(): Int = R.layout.activity_setting

    override fun initEvent() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.apply {
            tvToolbarTitle.text = "设置"
            setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
            setNavigationOnClickListener {
                onBackPressed()
            }
        }
        rlCleanCache.setOnNoRepeatClickListener {
            MessageDialog.build(this@SettingActivity)
                .setStyle(DialogSettings.STYLE.STYLE_IOS)
                .setTitle("提示")
                .setMessage("确定清空缓存吗")
                .setOkButton("确定") { baseDialog: BaseDialog, _: View? ->
                    val isDelete = FileUtils.delete(mFile)
                    if (isDelete) {
                        TipDialog.show(this@SettingActivity, "清除成功", TipDialog.TYPE.SUCCESS)
                    } else {
                        TipDialog.show(this@SettingActivity, "清除失败", TipDialog.TYPE.SUCCESS)
                    }
                    tvCacheSize.text = FileUtils.formatFileSize(FileUtils.getFileSize(mFile))
                    baseDialog.doDismiss()
                    false
                }
                .setCancelButton("取消") { baseDialog: BaseDialog, _: View? ->
                    baseDialog.doDismiss()
                    false
                }.show()
        }
        rlWebsite.setOnNoRepeatClickListener {
            startActivity<WebViewActivity> {
                putExtra(
                    Constant.ARTICLE_URL,
                    "https://github.com/XinHaoLeo/WanAndroid-JetpackMvvm"
                )
                putExtra(Constant.ARTICLE_TITLE, "酷酷的鑫")
            }
        }
        rlAuthor.setOnNoRepeatClickListener {
            try {
                val qqUrl =
                    "mqqwpa://im/chat?chat_type=wpa&version=1&uin=2438565661"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl))
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                showErrorDialog("你还未安装QQ或QQ版本不支持")
            }
        }
        rlAddGroupChat.setOnNoRepeatClickListener {
            val intent = Intent()
//            591683946,生成不出key来
            intent.data =
                Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D591683946")
            try {
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                showErrorDialog("未安装手机QQ或安装的版本不支持")
            }
        }
        rlCopyright.setOnNoRepeatClickListener {
            MessageDialog.build(this@SettingActivity)
                .setStyle(DialogSettings.STYLE.STYLE_IOS)
                .setTitle("鑫提示")
                .setMessage("本App使用的API均为WanAndroid网站提供,仅供学习交流")
                .setOkButton("确定").setOnOkButtonClickListener { baseDialog, _ ->
                    baseDialog.doDismiss()
                    false
                }
                .show()
        }
    }

    override fun initData() {

    }

    override fun onResume() {
        super.onResume()
        mFile = File(cacheDir.absolutePath)
        val fileSize = FileUtils.getFileSize(mFile)
        tvCacheSize.text = FileUtils.formatFileSize(fileSize)
        tvVersion.text = AppUtils.getAppVersionName()
    }

}