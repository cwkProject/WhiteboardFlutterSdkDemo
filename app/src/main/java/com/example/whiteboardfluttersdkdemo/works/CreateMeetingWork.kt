package com.example.whiteboardfluttersdkdemo.works

import com.example.whiteboardfluttersdkdemo.common.jsonToObject
import com.example.whiteboardfluttersdkdemo.models.Meeting
import com.example.whiteboardfluttersdkdemo.values.ValueUrl
import org.cwk.android.library.annotation.Post
import org.json.JSONObject

/**
 * 创建会议接口
 *
 * @author 超悟空
 * @version 1.0 2019/4/4
 * @since 1.0 2019/4/4
 **/
class CreateMeetingWork : BaseSimpleWorkModel<String, Meeting>() {

    override fun onFillParams(dataMap: MutableMap<String, String?>, vararg params: String?) {
        dataMap["name"] = params[0]
    }

    override fun onSuccessExtract(jsonResult: JSONObject): Meeting =
        jsonResult.getString(RESULT).jsonToObject()

    @Post
    override fun onTaskUri(): String = ValueUrl.URL_MEETING_CREATE
}