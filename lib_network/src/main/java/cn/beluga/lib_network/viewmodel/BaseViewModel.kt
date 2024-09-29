package cn.beluga.lib_network.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewModelScope
import cn.beluga.lib_framwork.log.LogUtil
import cn.beluga.lib_network.callback.IApiErrorCallback
import cn.beluga.lib_network.error.ERROR
import cn.beluga.lib_network.error.ExceptionHandler
import cn.beluga.lib_network.flow.requestFlow
import cn.beluga.lib_network.response.BaseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

/**
 * @Author: belega
 * @Date: 2024/09/09/下午10:18
 * @Description: viewModel基类
 */
open class BaseViewModel:ViewModel() {

    /**
     *运行在主线程中,可直接调用
     * @author beluga
     * @create 2024/9/10
     * @param errorBlock 错误回调
     * @param responseBlock 请求函数
     */
    fun launchUI(errorBlock:(Int?,String?) -> Unit,responseBlock:suspend() -> Unit){
        viewModelScope.launch (Dispatchers.Main){
            safeApiCall(errorBlock = errorBlock,responseBlock)
        }

    }

    /**
     * 需要运行在协程作用域中
     * @author beluga
     * @create 2024/9/10
     * @param errorBlock 错误回调
     * @param responseBlock 请求函数
     * @return
     */
    suspend fun <T> safeApiCall(
        errorBlock: suspend  (Int?, String?) -> Unit,
        responseBlock: suspend () -> T?
    ):T? {
        try{
            return responseBlock()
        }catch (e:Exception){
            e.printStackTrace()
            LogUtil.e(e)
            val exception =ExceptionHandler.handleException(e)
            errorBlock(exception.errCode,exception.errMsg)
        }
        return null
    }

    /**
     * 不依赖BaseRepository，运行在主线程中，可直接调用
     * @author beluga
     * @create 2024/9/10
     * @param responseBlock 错误回调
     * @param errorCall 请求函数
     * @param successBlock 请求回调
     */
    fun <T> launchUiWithResult(
        responseBlock: suspend () -> BaseResponse<T>?,
        errorCall: IApiErrorCallback?,
        successBlock:(T?) -> Unit
    ){
        viewModelScope.launch (Dispatchers.Main){
            val result = safeApiCallWithResult(errorCall = errorCall,responseBlock)
            successBlock(result)
        }

    }


    /**
     * 不依赖BaseRepository，需要在作用域中运行
     * @author beluga
     * @create 2024/9/10
     * @param errorCall 错误回调
     * @param responseBlock 请求函数
     * @return
     */
    suspend fun <T> safeApiCallWithResult(
        errorCall:IApiErrorCallback?,
        responseBlock: suspend () -> BaseResponse<T>?
    ):T?{
        try {
            val response = withContext(Dispatchers.IO){
                withTimeout(10*1000){
                    responseBlock()
                }
            } ?: return null
        }catch (e:Exception){
            e.printStackTrace()
            LogUtil.e(e)
            val  exception = ExceptionHandler.handleException(e)
            if (ERROR.UNLOGIN.code == exception.errCode){
                errorCall?.onLoginFail(exception.errCode,exception.errMsg)
            }else{
                errorCall?.onError(exception.errCode,exception.errMsg)
            }
        }
        return null
    }

    fun <T> launchFlow(
        errorCall: IApiErrorCallback? = null,
        requestCall:suspend() -> BaseResponse<T>?,
        showLoading:((Boolean) -> Unit)? = null,
        successBlock:(T?) -> Unit
    ){
        viewModelScope.launch(Dispatchers.Main){
            val data = requestFlow(errorBlock = { code,error ->
                if (ERROR.UNLOGIN.code == code){
                    errorCall?.onLoginFail(code,error)
                }else{
                    errorCall?.onError(code,error)
                }
            },requestCall,showLoading)
            successBlock(data)
        }
    }




}