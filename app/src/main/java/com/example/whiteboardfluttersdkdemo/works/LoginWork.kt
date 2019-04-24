package com.example.whiteboardfluttersdkdemo.works

import com.example.whiteboardfluttersdkdemo.common.UserState
import com.example.whiteboardfluttersdkdemo.common.jsonToObject
import com.example.whiteboardfluttersdkdemo.models.User
import com.example.whiteboardfluttersdkdemo.values.ValueUrl
import org.cwk.android.library.annotation.Post
import org.json.JSONObject

/**
 * 登录接口
 *
 * @author 超悟空
 * @version 1.0 2019/4/4
 * @since 1.0 2019/4/4
 **/
class LoginWork : BaseSimpleWorkModel<Unit, User>() {

    override fun onFillParams(dataMap: MutableMap<String, String?>, vararg params: Unit?) {
    }

    override fun onSuccessExtract(jsonResult: JSONObject): User =
        jsonResult.getString(RESULT).jsonToObject()

    override fun onSuccess() {
        mData.result.apply {
            UserState.userId = userId
            UserState.avatar = headPic
        }
    }

    @Post
    override fun onTaskUri(): String = ValueUrl.URL_USER_LOGIN
}