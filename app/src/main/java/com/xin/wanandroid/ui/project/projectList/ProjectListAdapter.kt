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

package com.xin.wanandroid.ui.project.projectList

import android.graphics.Color
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xin.wanandroid.R
import com.xin.wanandroid.core.bean.DataX
import com.xin.wanandroid.ext.html
import com.xin.wanandroid.ext.isVisible
import kotlinx.android.synthetic.main.item_project_list.view.*
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
 * @date : 2020/9/13 11:11
 * @desc :
 * @since : xinxiniscool@gmail.com
 */
class ProjectListAdapter : BaseQuickAdapter<DataX, BaseViewHolder>(R.layout.item_project_list) {


    override fun convert(holder: BaseViewHolder, item: DataX) {
        holder.itemView.apply {
            Glide.with(context).load(item.envelopePic).into(ivProjectListIcon)
            tvProjectListTitle.text = item.title
            tvProjectListContent.text = item.desc.html()
            tvProjectListContent.isVisible = item.desc.isNotEmpty()
            tvProjectListTime.text = item.niceDate
            tvProjectListAuthor.text = item.author
            val random = Random()
            tvProjectListAuthor.setTextColor(
                Color.rgb(
                    random.nextInt(150),
                    random.nextInt(150),
                    random.nextInt(150)
                )
            )
        }
    }
}