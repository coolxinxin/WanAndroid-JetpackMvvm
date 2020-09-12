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

package com.xin.wanandroid.ui.home

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xin.wanandroid.R
import com.xin.wanandroid.core.bean.DataX
import com.xin.wanandroid.ext.html
import com.xin.wanandroid.ext.isVisible
import kotlinx.android.synthetic.main.item_home.view.*

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
 *@date : 2020/9/11 11:32
 *@since : xinxiniscool@gmail.com
 *@desc :
 */
class HomeAdapter(data: MutableList<DataX>?) :
    BaseQuickAdapter<DataX, BaseViewHolder>(R.layout.item_home, data) {

    override fun convert(holder: BaseViewHolder, item: DataX) {
        holder.itemView.apply {
            tvTitle.text = item.title
            tvChapterName.text = item.chapterName
            tvAuthor.text = when {
                item.author.isNotEmpty() -> {
                    item.author
                }
                item.shareUser.isNotEmpty() -> {
                    item.shareUser
                }
                else -> "匿名作者"
            }
            tvItemTop.isVisible = item.isTop
            tvDesc.text = item.desc.html()
            tvDesc.isVisible = item.desc.isNotEmpty()
            tvNew.isVisible = item.fresh
            tvProject.text = item.superChapterName
            tvTime.text = item.niceDate
            ivLike.isSelected = item.collect
        }
        addChildClickViewIds(R.id.ivLike)
    }
}