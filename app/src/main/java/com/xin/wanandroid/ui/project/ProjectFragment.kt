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

package com.xin.wanandroid.ui.project

import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.xin.wanandroid.R
import com.xin.wanandroid.base.BaseVMFragment
import com.xin.wanandroid.ext.isVisible
import com.xin.wanandroid.ext.setOnNoRepeatClickListener
import com.xin.wanandroid.ui.common.ViewPagerCommonAdapter
import com.xin.wanandroid.ui.project.projectList.ProjectListFragment
import kotlinx.android.synthetic.main.common_reload.*
import kotlinx.android.synthetic.main.fragment_project.*

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
 *@date : 2020/9/10 9:46
 *@since : xinxiniscool@gmail.com
 *@desc :
 */
class ProjectFragment : BaseVMFragment<ProjectViewModel>(), ViewPager.OnPageChangeListener {

    private val adapter by lazy {
        ViewPagerCommonAdapter(
            childFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
    }
    private var page = 0

    override fun getViewModelClass(): Class<ProjectViewModel> = ProjectViewModel::class.java

    override fun initLayoutView(): Int = R.layout.fragment_project

    override fun initEvent() {
        vpProject.adapter = adapter
        vpProject.currentItem = page
        vpProject.addOnPageChangeListener(this)
        btReload.setOnNoRepeatClickListener {
            mViewModel.getProjectData()
        }
    }

    override fun initData() {
        mViewModel.apply {
            isReload.observe(this@ProjectFragment, Observer {
                reloadProject.isVisible = it
            })
            projectData.observe(this@ProjectFragment, Observer {
                it.forEach { it1 ->
                    tabProject.addTab(tabProject.newTab().setText(it1.name))
                    adapter.apply {
                        addFragment(ProjectListFragment().addCid(it1.id))
                        addTitle(it1.name)
                    }
                }
                tabProject.setupWithViewPager(vpProject)
            })
        }
    }

    override fun lazyLoadData() {
        mViewModel.getProjectData()
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        page = position
    }
}