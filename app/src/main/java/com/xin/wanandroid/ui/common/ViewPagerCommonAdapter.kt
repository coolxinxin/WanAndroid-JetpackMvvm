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

import androidx.annotation.Nullable
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.xin.wanandroid.base.BaseSimpleFragment

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
 * @date : 2020/9/13 3:07
 * @desc :
 * @since : xinxiniscool@gmail.com
 */
class ViewPagerCommonAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {

    private val mFragmentList: ArrayList<BaseSimpleFragment> = ArrayList()
    private val mTitleList: ArrayList<String> = ArrayList()

    fun addFragment(fragment: BaseSimpleFragment) {
        mFragmentList.add(fragment)
        notifyDataSetChanged()
    }

    fun addTitle(title: String) {
        mTitleList.add(title)
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): BaseSimpleFragment = mFragmentList[position]

    override fun getCount(): Int = mFragmentList.size

    /**
     * 返回值有三种，
     * POSITION_UNCHANGED  默认值，位置没有改变
     * POSITION_NONE       item已经不存在
     * position            item新的位置
     * 当position发生改变时这个方法应该返回改变后的位置，以便页面刷新。
     */
    override fun getItemPosition(`object`: Any): Int {
        if (`object` is BaseSimpleFragment) {
            val index = mFragmentList.indexOf(`object`)
            return if (index != -1) {
                index
            } else {
                PagerAdapter.POSITION_NONE
            }
        }
        return super.getItemPosition(`object`)
    }

    override fun getItemId(position: Int): Long {
        return mFragmentList[position].hashCode().toLong()
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mTitleList[position]
    }
}