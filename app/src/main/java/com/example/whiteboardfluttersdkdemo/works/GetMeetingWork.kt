package com.example.whiteboardfluttersdkdemo.works

import com.example.whiteboardfluttersdkdemo.values.ValueUrl
import org.json.JSONObject

/**
 * 获取会议信息
 *
 * @author 超悟空
 * @version 1.0 2019/4/4
 * @since 1.0 2019/4/4
 **/
class GetMeetingWork : BaseSimpleWorkModel<String, String>() {

    override fun onFillParams(dataMap: MutableMap<String, String?>, vararg params: String?) {
        dataMap["meetingNum"] = params[0]
    }

    override fun onSuccessExtract(jsonResult: JSONObject): String =
        jsonResult.getJSONObject(RESULT).getString("meetingId")

    override fun onTaskUri(): String = ValueUrl.URL_MEETING_GET
}