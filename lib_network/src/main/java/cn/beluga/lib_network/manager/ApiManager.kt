package cn.beluga.lib_network.manager

import cn.beluga.lib_network.api.ApiInterface

/**
 * @Author: beluga
 * @Date: 2024/09/09/下午10:12
 * @Description: API管理类
 */
object ApiManager {
    val api by lazy { HttpManager.create(ApiInterface::class.java) }
}