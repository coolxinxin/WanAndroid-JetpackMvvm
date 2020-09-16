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

package com.xin.wanandroid.ui.common

import android.graphics.Color
import android.text.TextUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xin.wanandroid.R
import com.xin.wanandroid.core.bean.DataX
import com.xin.wanandroid.ext.html
import com.xin.wanandroid.ext.isVisible
import kotlinx.android.synthetic.main.item_article_common.view.*
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
 *@date : 2020/9/11 11:32
 *@since : xinxiniscool@gmail.com
 *@desc :
 */
class ArticleCommonAdapter : BaseQuickAdapter<DataX, BaseViewHolder>(R.layout.item_article_common) {

    private var type: Type = Type.UNCOLLECT

    //我的收藏列表中没有collect字段,为了能让我的收藏列表重用,增加一个type判断
    fun setType(type: Type) {
        this.type = type
    }

    override fun convert(holder: BaseViewHolder, item: DataX) {
        holder.itemView.apply {
            val random = Random()
            val red = random.nextInt(150)
            val green = random.nextInt(150)
            val blue = random.nextInt(150)
            tvTitle.text = item.title
            tvChapterName.text = item.chapterName
            tvChapterName.setTextColor(Color.rgb(red, green, blue))
            tvAuthor.setTextColor(Color.rgb(red, green, blue))
            tvAuthor.text = when {
                !TextUtils.isEmpty(item.author) -> {
                    item.author
                }
                !TextUtils.isEmpty(item.shareUser) -> {
                    item.shareUser
                }
                else -> "匿名作者"
            }
            tvItemTop.isVisible = item.isTop
            tvDesc.text = item.desc.html()
            tvDesc.isVisible = item.desc.isNotEmpty()
            tvNew.isVisible = item.fresh
            tvProject.text = item.superChapterName
            tvProject.isVisible = !TextUtils.isEmpty(item.superChapterName)
            tvTime.text = item.niceDate
            ivLike.isSelected = item.collect
            if (type == Type.COLLECT) {
                ivLike.isSelected = true
            }
        }
        addChildClickViewIds(R.id.ivLike)
    }

    enum class Type {
        COLLECT, UNCOLLECT
    }
}