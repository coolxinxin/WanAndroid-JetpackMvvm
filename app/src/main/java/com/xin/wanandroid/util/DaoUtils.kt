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

package com.xin.wanandroid.util

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.xin.wanandroid.ext.user
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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
 *@date : 2020/7/6 18:16
 *@since : lightingxin@qq.com
 *@desc :
 */
object DaoUtils {

    private const val TABLE_NAME = "searchRecord"
    private lateinit var mSearchRecordSQLiteOpenHelper: SearchRecordSQLiteOpenHelper
    private lateinit var mSearchRecordDb: SQLiteDatabase

    //不登录的时候默认Leo
    private var mUserName = "Leo"
    private var mOnNotifyDataChangedListener: (() -> Unit)? = null

    /**
     * 使用此工具类必须先调用init函数
     *
     * @param context 上下文环境
     */
    fun init(context: Context, userName: String? = user?.username) {
        userName?.let { mUserName = it }
        mSearchRecordSQLiteOpenHelper = SearchRecordSQLiteOpenHelper(context)
    }

//    interface OnNotifyDataChangedListener {
//        fun notifyDataChanged()
//    }

    fun setOnNotifyDataChanged(listener: (() -> Unit)) {
        mOnNotifyDataChangedListener = listener
    }

    fun removeNotifyDataChanged() {
        if (mOnNotifyDataChangedListener != null) {
            mOnNotifyDataChangedListener = null
        }
    }


//    /**
//     * 设置数据变化监听
//     */
//    fun setOnNotifyDataChanged(onNotifyDataChangedListener: OnNotifyDataChangedListener) {
//        mOnNotifyDataChangedListener = onNotifyDataChangedListener
//    }

//    /**
//     * 移除数据变化监听
//     */
//    fun removeNotifyDataChanged() {
//        if (mOnNotifyDataChangedListener != null) {
//            mOnNotifyDataChangedListener = null
//        }
//    }

    @Synchronized
    private fun getWritableDatabase(): SQLiteDatabase {
        return mSearchRecordSQLiteOpenHelper.writableDatabase
    }

    @Synchronized
    private fun getReadableDatabase(): SQLiteDatabase {
        return mSearchRecordSQLiteOpenHelper.readableDatabase
    }

    /**
     * 如果考虑操作频繁可以到最后不用数据库时关闭
     *
     * 关闭数据库
     */
    fun closeDatabase() {
        mSearchRecordDb.close()
    }

    /**
     * 添加搜索记录
     *
     * @param record 记录
     */
    fun addRecords(record: String) {
        //如果这条记录没有则添加，有则更新时间
        val recordId = getRecordId(record)
        try {
            mSearchRecordDb = getReadableDatabase()
            if (-1 == recordId) {
                val values = ContentValues()
                values.put("username", mUserName)
                values.put("keyword", record)
                //添加搜索记录
                mSearchRecordDb.insert(TABLE_NAME, null, values)
            } else {
                val d = Date()
                @SuppressLint("SimpleDateFormat") val sdf =
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                //更新搜索历史数据时间
                val values = ContentValues()
                values.put("time", sdf.format(d))
                mSearchRecordDb.update(
                    TABLE_NAME,
                    values,
                    "_id = ?",
                    arrayOf(recordId.toString())
                )
            }
            mOnNotifyDataChangedListener?.invoke()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 判断是否含有该搜索记录
     *
     * @param record 记录
     * @return true | false
     */
    fun isHasRecord(record: String): Boolean {
        var isHasRecord = false
        var cursor: Cursor? = null
        try {
            mSearchRecordDb = getReadableDatabase()
            cursor = mSearchRecordDb.query(
                TABLE_NAME,
                null,
                "username = ?",
                arrayOf(mUserName),
                null,
                null,
                null
            )
            while (cursor.moveToNext()) {
                if (record == cursor.getString(cursor.getColumnIndexOrThrow("keyword"))) {
                    isHasRecord = true
                }
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return isHasRecord
    }

    /**
     * 判断是否含有该搜索记录
     *
     * @param record 记录
     * @return id
     */
    fun getRecordId(record: String): Int {
        var isHasRecord = -1
        var cursor: Cursor? = null
        try {
            mSearchRecordDb = getReadableDatabase()
            cursor = mSearchRecordDb.query(
                TABLE_NAME,
                null,
                "username = ?",
                arrayOf(mUserName),
                null,
                null,
                null
            )
            while (cursor.moveToNext()) {
                if (record == cursor.getString(cursor.getColumnIndexOrThrow("keyword"))) {
                    isHasRecord = cursor.getInt(cursor.getColumnIndexOrThrow("_id"))
                }
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return isHasRecord
    }

    /**
     * 获取当前用户全部搜索记录
     *
     * @return 记录集合
     */
    fun getRecordsList(): ArrayList<String> {
        val recordsList: ArrayList<String> = ArrayList()
        var cursor: Cursor? = null
        try {
            mSearchRecordDb = getReadableDatabase()
            cursor = mSearchRecordDb.query(
                TABLE_NAME,
                null,
                "username = ?",
                arrayOf(mUserName),
                null,
                null,
                "time desc"
            )
            while (cursor.moveToNext()) {
                val name = cursor.getString(cursor.getColumnIndexOrThrow("keyword"))
                recordsList.add(name)
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return recordsList
    }

    /**
     * 获取指定数量搜索记录
     * 当业务有需要只展示部分搜索记录,其余需要点击展开才显示的时候 可以使用此函数
     *
     * @return 记录集合
     */
    fun getRecordsByNumber(recordNumber: Int): List<String>? {
        val recordsList: MutableList<String> =
            ArrayList()
        require(recordNumber >= 0)
        if (0 == recordNumber) {
            return recordsList
        } else {
            var cursor: Cursor? = null
            try {
                mSearchRecordDb = getReadableDatabase()
                cursor = mSearchRecordDb.query(
                    TABLE_NAME,
                    null,
                    "username = ?",
                    arrayOf(mUserName),
                    null,
                    null,
                    "time desc limit $recordNumber"
                )
                while (cursor.moveToNext()) {
                    val name =
                        cursor.getString(cursor.getColumnIndexOrThrow("keyword"))
                    recordsList.add(name)
                }
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } finally {
                cursor?.close()
            }
        }
        return recordsList
    }

    /**
     * 模糊查询
     *
     * @param record 记录
     * @return 返回类似记录
     */
    fun querySimlarRecord(record: String): List<String>? {
        val similarRecords: MutableList<String> =
            ArrayList()
        var cursor: Cursor? = null
        try {
            mSearchRecordDb = getReadableDatabase()
            cursor = mSearchRecordDb.query(
                TABLE_NAME,
                null,
                "username = ? and keyword like '%?%'",
                arrayOf(mUserName, record),
                null,
                null,
                "order by time desc"
            )
            while (cursor.moveToNext()) {
                val name = cursor.getString(cursor.getColumnIndexOrThrow("keyword"))
                similarRecords.add(name)
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return similarRecords
    }

    /**
     * 清除指定用户的搜索记录
     */
    fun deleteUsernameAllRecords(userName: String) {
        try {
            mSearchRecordDb = getWritableDatabase()
            mSearchRecordDb.delete(TABLE_NAME, "username = ?", arrayOf(userName))
            mOnNotifyDataChangedListener?.invoke()
        } catch (e: SQLException) {
            e.printStackTrace()
            Log.e(TABLE_NAME, "清除所有历史记录失败")
        } finally {
        }
    }

    /**
     * 清空数据库所有的历史记录
     */
    fun deleteAllRecords() {
        try {
            mSearchRecordDb = getWritableDatabase()
            mSearchRecordDb.execSQL("delete from $TABLE_NAME")
            mOnNotifyDataChangedListener?.invoke()
        } catch (e: SQLException) {
            e.printStackTrace()
            Log.e(TABLE_NAME, "清除所有历史记录失败")
        } finally {
        }
    }

    /**
     * 通过id删除记录
     *
     * @param id 记录id
     * @return 返回删除id
     */
    fun deleteRecord(id: Int): Int {
        var d = -1
        try {
            mSearchRecordDb = getWritableDatabase()
            d = mSearchRecordDb.delete(
                TABLE_NAME,
                "_id = ?",
                arrayOf(id.toString())
            )
            mOnNotifyDataChangedListener?.invoke()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TABLE_NAME, "删除_id：" + id + "历史记录失败")
        }
        return d
    }

    /**
     * 通过记录删除记录
     *
     * @param record 记录
     */
    fun deleteRecord(record: String): Int {
        var recordId = -1
        try {
            mSearchRecordDb = getWritableDatabase()
            recordId = mSearchRecordDb.delete(
                TABLE_NAME,
                "username = ? and keyword = ?",
                arrayOf(mUserName, record)
            )
            mOnNotifyDataChangedListener?.invoke()
        } catch (e: SQLException) {
            e.printStackTrace()
            Log.e(TABLE_NAME, "清除所有历史记录失败")
        }
        return recordId
    }
}