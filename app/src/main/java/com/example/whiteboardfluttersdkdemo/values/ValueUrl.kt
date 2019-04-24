package com.example.whiteboardfluttersdkdemo.values

/**
 * 接口地址
 *
 * @author 超悟空
 * @version 1.0 2019/4/4
 * @since 1.0 2019/4/4
 **/
interface ValueUrl {

    companion object {
        /**
         * 根路径
         */
        private const val BASE_URL = "http://192.168.0.31:8989/Demo"

        /**
         * 根路径+/user
         */
        private const val BASE_USER_URL = "$BASE_URL/user"

        /**
         * 根路径+/meeting
         */
        private const val BASE_MEETING_URL = "$BASE_URL/meeting"

        /**
         * 登录
         */
        const val URL_USER_LOGIN = "$BASE_USER_URL/login"

        /**
         * 创建会议
         */
        const val URL_MEETING_CREATE = "$BASE_MEETING_URL/create"

        /**
         * 获取会议详情
         */
        const val URL_MEETING_GET = "$BASE_MEETING_URL/get"
    }
}