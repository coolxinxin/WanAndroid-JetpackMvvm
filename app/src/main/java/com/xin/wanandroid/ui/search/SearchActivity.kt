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

package com.xin.wanandroid.ui.search

import android.text.TextUtils
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import com.kongzue.dialog.util.BaseDialog
import com.kongzue.dialog.util.DialogSettings
import com.kongzue.dialog.v3.MessageDialog
import com.xin.wanandroid.R
import com.xin.wanandroid.base.BaseVMActivity
import com.xin.wanandroid.core.Constant
import com.xin.wanandroid.ext.startActivity
import com.xin.wanandroid.util.DaoUtils
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.activity_search.*

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
 *@date : 2020/9/14 9:42
 *@since : xinxiniscool@gmail.com
 *@desc :
 */
class SearchActivity : BaseVMActivity<SearchViewModel>() {


    override fun getViewModelClass(): Class<SearchViewModel> = SearchViewModel::class.java

    override fun initLayoutView(): Int = R.layout.activity_search

    override fun initEvent() {
        DaoUtils.init(this)
        DaoUtils.setOnNotifyDataChanged {
            mViewModel.getHistoryData()
        }
        initOnClick()
    }

    override fun initData() {
        mViewModel.getHotKeyData()
        mViewModel.getHistoryData()
        mViewModel.apply {
            hotKeyData.observe(this@SearchActivity, Observer {
                val list = ArrayList<String>()
                it.forEach { it1 ->
                    list.add(it1.name)
                }
                initAdapter(0, list)
            })
            historyData.observe(this@SearchActivity, Observer {
                initAdapter(1, it)
            })
        }
    }

    private fun initOnClick() {
        ivClearAllHistory.setOnClickListener {
            MessageDialog.build(this@SearchActivity)
                .setStyle(DialogSettings.STYLE.STYLE_IOS)
                .setTitle("提示")
                .setMessage("确定删除所有历史搜索记录吗?")
                .setOkButton("确定") { baseDialog: BaseDialog, _: View? ->
                    mViewModel.clearHistoryData()
                    baseDialog.doDismiss()
                    false
                }
                .setCancelButton("取消") { baseDialog: BaseDialog, _: View? ->
                    baseDialog.doDismiss()
                    false
                }.show()

        }
        tvSearch.setOnClickListener {
            startSearch()
        }
        ivClear.setOnClickListener {
            val record = etQuery.text.toString()
            if (record.isNotEmpty()) {
                val selectionStart = etQuery.selectionStart
                if (selectionStart > 0) {
                    etQuery.text?.delete(selectionStart - 1, selectionStart)
                    etQuery.setSelection(selectionStart - 1)
                }
            }
        }
        ivClear.setOnLongClickListener {
            etQuery.text?.clear()
            true
        }
        ivBack.setOnClickListener { onBackPressed() }
    }

    private fun startSearch(data: String = etQuery.text.toString()) {
        if (TextUtils.isEmpty(data)) {
            showShortToast("哎呀，什么都还没输入呢~")
        } else {
            mViewModel.addHistoryData(data)
            startActivity<SearchListActivity> {
                putExtra(Constant.SEARCH_DATA, data)
            }
            finish()
        }
    }

    private fun initAdapter(id: Int, data: List<String>) {
        val adapter = object : TagAdapter<String>(data) {
            override fun getView(parent: FlowLayout, position: Int, content: String): View {
                val tvHistory = LayoutInflater.from(this@SearchActivity).inflate(
                    R.layout.item_history,
                    parent, false
                ) as TextView
                tvHistory.text = content
                return tvHistory
            }

        }
        if (id == 0) {
            flHotSearch.adapter = adapter
            flHotSearch.setOnTagClickListener { _, position, _ ->
                startSearch(data[position])
                true
            }
        } else {
            flHistorySearch.adapter = adapter
            flHistorySearch.setOnTagClickListener { _, position, _ ->
                startSearch(data[position])
                true
            }
        }
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_ENTER -> startSearch()
        }
        return super.onKeyUp(keyCode, event)
    }

    override fun onDestroy() {
        DaoUtils.closeDatabase()
        DaoUtils.removeNotifyDataChanged()
        super.onDestroy()
    }
}