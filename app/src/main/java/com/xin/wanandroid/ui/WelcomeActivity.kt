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

package com.xin.wanandroid.ui

import android.os.CountDownTimer
import com.xin.wanandroid.R
import com.xin.wanandroid.app.MyApp
import com.xin.wanandroid.base.BaseSimpleActivity
import com.xin.wanandroid.ext.setOnNoRepeatClickListener
import com.xin.wanandroid.ext.startActivity
import com.xin.wanandroid.widget.ModelSVG
import kotlinx.android.synthetic.main.activity_welcome.*

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
 *@date : 2020/9/9 19:33
 *@since : xinxiniscool@gmail.com
 *@desc :
 */
class WelcomeActivity : BaseSimpleActivity() {

    private lateinit var countDownTimer: CountDownTimer

    override fun initLayoutView(): Int = R.layout.activity_welcome

    override fun initEvent() {

    }

    override fun initData() {
        if (MyApp.getInstance().isFirstRun) {
            setSvg(ModelSVG.values()[4])
        } else {
            jumpToMainActivity()
        }
        countDownTimer = object : CountDownTimer(3000, 1000) {
            override fun onFinish() {
                jumpToMainActivity()
            }

            override fun onTick(millisUntilFinished: Long) {
                val text = "点击跳过\r\r" + millisUntilFinished / 1000
                tvJump.text = text
            }
        }
        //启动倒计时
        countDownTimer.start()

        tvJump.setOnNoRepeatClickListener {
            jumpToMainActivity()
        }
    }

    private fun setSvg(modelSvg: ModelSVG) {
        animatedSvgView.setGlyphStrings(*modelSvg.glyphs)
        animatedSvgView.setFillColors(modelSvg.colors)
        animatedSvgView.setViewportSize(modelSvg.width, modelSvg.height)
        animatedSvgView.setTraceResidueColor(0x32000000)
        animatedSvgView.setTraceColors(modelSvg.colors)
        animatedSvgView.rebuildGlyphData()
        animatedSvgView.start()
    }

    private fun jumpToMainActivity() {
        startActivity<MainActivity>()
        MyApp.getInstance().isFirstRun = false
        finish()
    }
}