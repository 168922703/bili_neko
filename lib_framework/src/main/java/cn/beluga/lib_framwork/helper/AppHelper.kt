package cn.beluga.lib_framwork.helper

import android.app.Application

/**
 * @Author: beluga
 * @Date: 2024/09/10/上午2:47
 * @Description: 提供应用环境
 */
object AppHelper {
    private lateinit var app:Application
    private var isDebug = false

    fun init(application: Application,isDebug:Boolean){
        this.app = application
        this.isDebug = isDebug
    }

    /**
     * 获取全局应用
     */
    fun getApplication() = app


    /**
     * 是否为debug环境
     */
    fun isDebug() = isDebug

}