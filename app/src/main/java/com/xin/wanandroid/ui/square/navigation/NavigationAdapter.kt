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

package com.xin.wanandroid.ui.square.navigation

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xin.wanandroid.R
import com.xin.wanandroid.core.Constant
import com.xin.wanandroid.core.bean.DataX
import com.xin.wanandroid.core.bean.NavigationData
import com.xin.wanandroid.ext.startActivity
import com.xin.wanandroid.ui.webview.WebViewActivity
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.item_navigation.view.*
import java.util.*

/**
 *   █████▒█    ██  ▄████▄   ██ ▄█▀       ██████╗ ██╗   ██╗ ██████╗
 * ▓██   ▒ ██  ▓██▒▒██▀ ▀█   ██▄█▒        ██╔══██╗██║   ██║██╔════╝
 * ▒████ ░▓██  ▒██░▒▓█    ▄ ▓███▄░        ██████╔╝██║   ██║██║  ███╗
 * ░▓█▒  ░▓▓█  ░██░▒▓▓▄ ▄██▒▓██ █▄        ██╔══██╗██║   ██║██║   ██║
 * ░▒█░   ▒▒█████▓ ▒ ▓███▀ ░▒██▒ █▄       ██████╔╝╚██████╔╝╚██████╔╝
 *  ▒ ░   ░▒▓▒ ▒ ▒ ░ ░▒ ▒  ░▒ ▒▒ ▓▒       ╚═════╝  ╚═════╝  ╚═════╝
 *  ░     ░░▒░ ░ ░   ░  ▒   ░ ░▒ ▒░
 *  ░ ░    ░░░ ░ ░ ░        ░ ░░ ░
 *           ░     ░ ░      ░  ░
 * @author : Leo
 * @date : 2020/9/13 2:15
 * @desc :
 * @since : xinxiniscool@gmail.com
 */
class NavigationAdapter :
    BaseQuickAdapter<NavigationData, BaseViewHolder>(R.layout.item_navigation) {

    override fun convert(holder: BaseViewHolder, item: NavigationData) {
        holder.itemView.apply {
            tvNavigationName.text = item.name
            tabNavigationList.adapter =
                object : TagAdapter<DataX>(item.articles) {
                    override fun getView(
                        parent: FlowLayout,
                        position: Int,
                        articlesBean: DataX
                    ): View {
                        val view = LayoutInflater.from(parent.context).inflate(
                            R.layout.item_flow,
                            tabNavigationList,
                            false
                        ) as TextView
                        val title: String = articlesBean.title
                        val random = Random()
                        val red = random.nextInt(150)
                        val green = random.nextInt(150)
                        val blue = random.nextInt(150)
                        view.text = title
                        view.setTextColor(Color.rgb(red, green, blue))
                        tabNavigationList.setOnTagClickListener { _: View?, position1: Int, _: FlowLayout? ->
                            context.startActivity<WebViewActivity> {
                                putExtra(Constant.ARTICLE_URL, getItem(position1).link)
                                putExtra(Constant.ARTICLE_TITLE, getItem(position1).title)
//                                putExtra(Constant.ARTICLE_ID, getItem(position1).id)
//                                putExtra(Constant.ARTICLE_COLLECT, getItem(position1).collect)
                            }
                            true
                        }
                        return view
                    }
                }
        }
    }

}