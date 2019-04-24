package com.example.whiteboardfluttersdkdemo.models

/**
 * 数据类
 *
 * @author 超悟空
 * @version 1.0 2019/4/4
 * @since 1.0 2019/4/4
 **/

/**
 * Demo服务器返回的模拟用户数据
 *
 * @property userId 用户id
 * @property nickName 昵称
 * @property headPic 头像地址
 */
data class User(
    val userId: String,
    val nickName: String,
    val headPic: String
)

/**
 * Demo服务器返回的会议数据
 *
 * @property meetingId 会议id
 * @property name 会议名称
 * @property meetingNum 会议数字码（邀请码）
 */
data class Meeting(
    val meetingId: String,
    val name: String,
    val meetingNum: Int
)