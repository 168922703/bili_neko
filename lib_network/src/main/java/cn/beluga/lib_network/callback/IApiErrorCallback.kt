package cn.beluga.lib_network.callback

/**
 * @Author: beluga
 * @Date: 2024/09/09/下午2:09
 * @Description: 接口请求错误回调
 */
interface IApiErrorCallback {
    /**
     * 错误回调处理
     */
    fun onError(code: Int?, error: String?) {
//        TipsToast.showTips(error)
    }

    /**
     * 登录失效处理
     */
    fun onLoginFail(code: Int?, error: String?) {
//        TipsToast.showTips(error)
    }
}