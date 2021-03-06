package com.example.whiteboardfluttersdkdemo.activity

import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.whiteboardfluttersdkdemo.R
import com.example.whiteboardfluttersdkdemo.common.UserState
import com.example.whiteboardfluttersdkdemo.component.ScreenPushComponent
import com.example.whiteboardfluttersdkdemo.works.CreateMeetingWork
import com.example.whiteboardfluttersdkdemo.works.GetMeetingWork
import com.example.whiteboardfluttersdkdemo.works.LoginWork
import com.example.whiteboardfluttersdkdemo.works.start
import io.flutter.facade.Flutter
import io.flutter.facade.RoomInfo
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

/**
 * 首页
 *
 * @author 超悟空
 * @version 1.0 2019/4/4
 * @since 1.0 2019/4/4
 **/
class MainActivity : AppCompatActivity() {

    companion object {
        /**
         * 录屏权限请求码
         */
        private const val RECORD_REQUEST_CODE = 12345
    }

    /**
     * 推流工具
     */
    private var screenPush: ScreenPushComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LoginWork().start {
            if (it.isSuccess) {

                user_name.setText(it.result.nickName)

                toast("生成用户信息成功")
            } else {
                toast("生成用户信息失败")
            }
        }
    }

    override fun onContentChanged() {
        super.onContentChanged()

        create_meeting.setOnClickListener {
            if (meeting_name.text.isNotBlank()) {

                CreateMeetingWork().start(meeting_name.text.toString()) {
                    if (it.isSuccess) {

                        meeting_number.setText("${it.result.meetingNum}")

                        toast("创建会议成功")
                    } else {
                        toast("创建会议失败")
                    }
                }
            } else {
                meeting_name.error = "会议名称不能为空"
            }
        }

        join_meeting.setOnClickListener {
            if (meeting_number.text.isNotBlank()) {

                GetMeetingWork().start(meeting_number.text.toString()) {
                    if (it.isSuccess) {
                        onJoinMeeting(it.result)
                    } else {
                        toast("获取会议信息失败")
                    }
                }
            } else {
                meeting_number.error = "会议号不能为空"
            }
        }

        screen_push.setOnClickListener {
            if (screenPush != null) {
                screenPush?.release()
                screenPush = null
                screen_push.text = "录屏直播"
            } else {
                val projectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager

                // 省略请求录音和相机权限操作

                startActivityForResult(projectionManager.createScreenCaptureIntent(), RECORD_REQUEST_CODE)
            }
        }
    }

    /**
     * 执行加入会议
     *
     * @param meetingId 目标会议id
     */
    private fun onJoinMeeting(meetingId: String) {
        if (UserState.userId == null) {
            toast("用户未登录")
            return
        }

        if (user_name.text.isBlank()) {
            user_name.error = "昵称不能空"
            return
        }

        if (user_role.text.isBlank()) {
            user_role.error = "角色不能空"
            return
        }

        val roomInfo = RoomInfo(
            "b289e574dbae4ed9b63702877ff2b51f",
            meetingId,
            UserState.userId ?: "",
            user_role.text.toString().toInt(),
            user_name.text.toString(),
            UserState.avatar,
            meeting_password.text.toString()
        )

        Flutter.startRoomActivity(this, roomInfo)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RECORD_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                screenPush = ScreenPushComponent().apply {
                    startScreenPush(this@MainActivity, data)
                }

                screen_push.text = "停止直播"
            }
        }
    }
}
