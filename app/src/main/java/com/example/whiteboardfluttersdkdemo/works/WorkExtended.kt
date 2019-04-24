package com.example.whiteboardfluttersdkdemo.works

import org.cwk.android.library.data.IDataModel
import org.cwk.android.library.data.SimpleDataModel
import org.cwk.android.library.data.WorkDataModel
import org.cwk.android.library.work.StandardWorkModel

/**
 * 任务装配器
 *
 * @author 超悟空
 * @version 1.0 2019/4/4
 * @since 1.0 2019/4/4
 **/

/**
 * 异步执行一个网络请求
 *
 * @param parameters 参数集合
 * @param isUiThread 是否将回调发送到主线程，true表示发到主线程
 * @param retry 重试次数，默认0表示不重试
 * @param finish 完成回调
 *
 * @return 任务实例
 */
inline fun <C, D : WorkDataModel<*, *, *, C, *>, T : StandardWorkModel<C, D>> T.start(vararg parameters: C?,
                                                                                      isUiThread: Boolean = true, retry: Int = 0, crossinline finish: (D) -> Unit): T = apply {
    setOnWorkFinishListener(isUiThread) { finish(it) }.setRetryTimes(retry).beginExecute(*parameters)
}

/**
 * 异步执行一个网络请求
 *
 * @param parameters 参数集合
 * @param init 初始化函数
 *
 * @return 任务实例
 */
inline fun <C, D : WorkDataModel<*, *, *, C, *>, T : StandardWorkModel<C, D>> T.initStart(vararg parameters: C?, crossinline init: WorkBuilder<C, D, T>.() -> Unit): T = apply {
    WorkBuilder(this).apply { init() }
    beginExecute(*parameters)
}

/**
 * 同步执行一个网络请求
 *
 * @param parameters 参数集合
 * @param retry 重试次数，默认0表示不重试
 * @param finish 完成回调
 *
 * @return 任务实例
 */
inline fun <C, D : WorkDataModel<*, *, *, C, *>, T : StandardWorkModel<C, D>> T.syncStart(vararg parameters: C?, retry: Int = 0, crossinline finish: (D) -> Unit): T = apply {
    setRetryTimes(retry).execute(*parameters)?.let(finish)
}

/**
 * 同步执行一个网络请求
 *
 * @param parameters 参数集合
 * @param init 初始化函数
 *
 * @return 任务实例
 */
inline fun <C, D : WorkDataModel<*, *, *, C, *>, T : StandardWorkModel<C, D>> T.syncInitStart(vararg parameters: C?, crossinline init: WorkBuilder<C, D, T>.() -> Unit): D? = let {
    WorkBuilder(this).apply { init() }
    execute(*parameters)
}

/**
 * 回调函数构建器
 */
class WorkBuilder<C, D : WorkDataModel<*, *, *, C, *>, out T : StandardWorkModel<C, D>>(val work: T) {

    /**
     * 设置重试次数
     *
     * @param times 重试次数
     */
    fun retry(times: Int) {
        work.setRetryTimes(times)
    }

    /**
     * 设置完成回调
     *
     * @param isUiThread 是否将回调发送到主线程，true表示发到主线程
     * @param holder 回调函数
     */
    inline fun finish(isUiThread: Boolean = true, crossinline holder: (D) -> Unit) {
        work.setOnWorkFinishListener(isUiThread) { holder(it) }
    }

    /**
     * 设置进度更新回调
     *
     * @param isUiThread 是否将回调发送到主线程，true表示发到主线程
     * @param holder 回调函数
     */
    inline fun update(isUiThread: Boolean = true, crossinline holder: (current: Long, total: Long, done: Boolean) -> Unit) {
        work.setOnNetworkProgressListener(isUiThread) { current, total, done -> holder(current, total, done) }
    }

    /**
     * 设置取消回调
     *
     * @param isUiThread 是否将回调发送到主线程，true表示发到主线程
     * @param holder 回调函数
     */
    inline fun cancel(isUiThread: Boolean = true, crossinline holder: (parameters: Array<C>) -> Unit) {
        work.setOnWorkCanceledListener(isUiThread) { holder(it) }
    }
}

// [IDataModel]解构扩展
inline operator fun <reified T : IDataModel<*, *>> T.component1(): Boolean = isSuccess

inline operator fun <RESULT, reified T : IDataModel<*, RESULT>> T.component2(): RESULT? = result

inline operator fun <reified T : IDataModel<*, *>> T.component3(): String? = message

inline operator fun <reified T : IDataModel<*, *>> T.component4(): Int = code

// [SimpleDataModel]解构扩展
inline operator fun <reified T : SimpleDataModel<*, *>> T.component5(): Int = errorCode
