package cn.beluga.lib_network.interceptor

import cn.beluga.lib_framwork.log.LogUtil
import cn.beluga.lib_network.constant.ARTICLE_WEBSITE
import cn.beluga.lib_network.constant.COIN_WEBSITE
import cn.beluga.lib_network.constant.COLLECTION_WEBSITE
import cn.beluga.lib_network.constant.KEY_COOKIE
import cn.beluga.lib_network.constant.NOT_COLLECTION_WEBSITE
import cn.beluga.lib_network.manager.CookiesManager
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @Author: beluga
 * @Date: 2024/09/10/上午2:59
 * @Description: 头信息拦截器
 * 添加头信息
 */
class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newBuilder = request.newBuilder()
        newBuilder.addHeader("Content-type", "application/json; charset=utf-8")

        val host = request.url.host
        val url = request.url.toString()

        //给有需要的接口添加Cookies
        if (host.isNotEmpty() && (url.contains(COLLECTION_WEBSITE)
                    || url.contains(NOT_COLLECTION_WEBSITE)
                    || url.contains(ARTICLE_WEBSITE)
                    || url.contains(COIN_WEBSITE))) {
            val cookies = CookiesManager.getCookies()
            LogUtil.e("HeaderInterceptor:cookies:$cookies")
            if (!cookies.isNullOrEmpty()) {
                newBuilder.addHeader(KEY_COOKIE, cookies)
            }
        }
        return chain.proceed(newBuilder.build())
    }
}