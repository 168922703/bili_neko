package cn.beluga.lib_network.interceptor

import cn.beluga.lib_framwork.log.LogUtil
import cn.beluga.lib_network.constant.KEY_SAVE_USER_LOGIN
import cn.beluga.lib_network.constant.KEY_SAVE_USER_REGISTER
import cn.beluga.lib_network.constant.KEY_SET_COOKIE
import cn.beluga.lib_network.manager.CookiesManager
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @Author: 作者
 * @Date: 2024/09/09/下午6:11
 * @Description: 致敬
 */
class CookiesInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newBuilder = request.newBuilder()

        val response = chain.proceed(newBuilder.build())
        val url = request.url.toString()
        val host = request.url.host

        // set-cookie maybe has multi, login to save cookie
        if ((url.contains(KEY_SAVE_USER_LOGIN) || url.contains(KEY_SAVE_USER_REGISTER))
            && response.headers(KEY_SET_COOKIE).isNotEmpty()
        ) {
            val cookies = response.headers(KEY_SET_COOKIE)
            val cookiesStr = CookiesManager.encodeCookie(cookies)
            CookiesManager.saveCookies(cookiesStr)
            LogUtil.e("CookiesInterceptor:cookies:$cookies")
        }
        return response
    }
}