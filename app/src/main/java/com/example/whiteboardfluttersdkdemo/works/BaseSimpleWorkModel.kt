package com.example.whiteboardfluttersdkdemo.works

import com.example.whiteboardfluttersdkdemo.common.UserState
import org.cwk.android.library.work.SimpleWorkModel

/**
 * [SimpleWorkModel]的定制基类，加入基础设置
 *
 * @author 超悟空
 * @version 1.0 2019/4/4
 * @since 1.0 2019/4/4
 */
abstract class BaseSimpleWorkModel<Parameters, Result> : SimpleWorkModel<Parameters, Result>() {

    @SafeVarargs
    override fun onFill(dataMap: MutableMap<String, String?>, vararg parameters: Parameters?) {
        if (UserState.userId != null) {
            dataMap["userId"] = UserState.userId
        }

        onFillParams(dataMap, *parameters)
    }

    /**
     * 填充服务请求所需的参数
     *
     * @param dataMap    将要填充的参数数据集<参数名,参数值>
     * @param params 任务传入的参数
     */
    protected abstract fun onFillParams(dataMap: MutableMap<String, String?>, vararg params: Parameters?)
}
