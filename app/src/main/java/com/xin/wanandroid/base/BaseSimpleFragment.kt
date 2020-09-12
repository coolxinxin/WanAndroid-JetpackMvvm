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

package com.xin.wanandroid.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.snackbar.Snackbar
import com.kongzue.dialog.v3.TipDialog

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
 *@date : 2020/9/9 19:02
 *@since : xinxiniscool@gmail.com
 *@desc :
 */
abstract class BaseSimpleFragment : Fragment() {

    protected lateinit var mActivity: BaseSimpleActivity
    private var isFirstLoad = true

    private var mParentView: View? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as BaseSimpleActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mParentView == null) {
            mParentView = inflater.inflate(initLayoutView(), container, false)
        } else {
            if (mParentView?.parent != null) {
                val viewGroup = mParentView?.parent as ViewGroup
                viewGroup.removeView(mParentView)
            }
        }
        return mParentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFirstLoad = true
        initEvent()
        initData()
        load()
    }

    @LayoutRes
    protected abstract fun initLayoutView(): Int

    protected abstract fun initEvent()

    protected abstract fun initData()

    protected abstract fun lazyLoadData()

    //懒加载
    private fun load() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirstLoad) {
            lazyLoadData()
            isFirstLoad = false
        }
    }

    override fun onResume() {
        super.onResume()
        load()
    }

    protected fun showSnackBar(view: View, msg: String, actionText: String) {
        val snackBar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
        snackBar.setAction(actionText) {
            snackBar.dismiss()
        }.show()
    }

    protected fun showShortToast(msg: String) {
        ToastUtils.showShort(msg)
    }

    protected fun showShortToast(@StringRes msg: Int) {
        ToastUtils.showShort(msg)
    }

    protected fun showTipDialog(msg: String) {
        TipDialog.showWait(mActivity, msg)
    }

    protected fun showTipDialog(@StringRes msg: Int) {
        TipDialog.showWait(mActivity, msg)
    }

    protected fun disMissTipDialog() {
        TipDialog.dismiss()
    }

    protected fun showTypeDialog(msg: String, type: TipDialog.TYPE) {
        TipDialog.show(mActivity, msg, type)
    }

    protected fun showTypeDialog(@StringRes msg: Int, type: TipDialog.TYPE) {
        TipDialog.show(mActivity, msg, type)
    }

    protected fun showErrorDialog(msg: String) {
        showTypeDialog(msg, TipDialog.TYPE.ERROR)
    }

    protected fun showErrorDialog(@StringRes msg: Int) {
        showTypeDialog(msg, TipDialog.TYPE.ERROR)
    }
}