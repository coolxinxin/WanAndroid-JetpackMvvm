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

package com.xin.wanandroid.ui.square.knowledge

import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xin.wanandroid.R
import com.xin.wanandroid.core.bean.KnowledgeData
import kotlinx.android.synthetic.main.item_knowledge.view.*
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
 * @date : 2020/9/12 22:04
 * @desc :
 * @since : xinxiniscool@gmail.com
 */
class KnowledgeAdapter : BaseQuickAdapter<KnowledgeData, BaseViewHolder>(R.layout.item_knowledge) {

    override fun convert(holder: BaseViewHolder, item: KnowledgeData) {
        holder.itemView.apply {
            val content = StringBuilder()
            for (data in item.children) {
                content.append(data.name).append("   ")
            }
            val random = Random()
            tvKnowledgeName.apply {
                text = item.name
                setTextColor(
                    Color.rgb(
                        random.nextInt(150),
                        random.nextInt(150),
                        random.nextInt(150)
                    )
                )
            }
            tvKnowledgeContent.apply {
                text = content.toString()
                setTextColor(
                    Color.rgb(
                        random.nextInt(150),
                        random.nextInt(150),
                        random.nextInt(150)
                    )
                )
            }
        }
    }
}