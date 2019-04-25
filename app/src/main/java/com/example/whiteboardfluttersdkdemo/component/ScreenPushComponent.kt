package com.example.whiteboardfluttersdkdemo.component

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import com.alivc.live.pusher.*


/**
 * 阿里云屏幕直播工具
 *
 * @author 超悟空
 * @version 1.0 2019/4/25
 * @since 1.0 2019/4/25
 **/
class ScreenPushComponent : LifecycleObserver, AlivcLivePushInfoListener,
    AlivcLivePushErrorListener {

    companion object {
        private const val TAG = "ScreenPushComponent"
    }

    /**
     * 阿里云推流SDK工具
     */
    private val alivcLivePusher = AlivcLivePusher()

    /**
     * 启动录屏推流
     *
     * @param activity 挂载的Activity
     * @param data 申请到的录屏权限
     */
    fun startScreenPush(activity: AppCompatActivity, data: Intent) {
        activity.lifecycle.addObserver(this)

        val alivcLivePushConfig = AlivcLivePushConfig()

        // 该方法必须在[AlivcLivePushConfig]初始化后调用，否则会被清空
        AlivcLivePushConfig.setMediaProjectionPermissionResultData(data)

        // 手动设置屏幕分辨率，SDK有BUG [AlivcResolutionEnum.RESOLUTION_PASS_THROUGH]完全未被使用
        val metrics = DisplayMetrics()
        activity.window.windowManager.defaultDisplay.getMetrics(metrics)
        val alivcResolutionEnum = AlivcResolutionEnum.RESOLUTION_SELFDEFINE
        alivcResolutionEnum.setSelfDefineResolution(metrics.widthPixels, metrics.heightPixels)
        alivcLivePushConfig.setResolution(alivcResolutionEnum)

        // 设置一下码率
        alivcLivePushConfig.setTargetVideoBitrate(1200)
        alivcLivePushConfig.setInitialVideoBitrate(1200)

        // 简单使用，没有处理其他情况如屏幕旋转等
        alivcLivePusher.init(activity.applicationContext, alivcLivePushConfig)
        alivcLivePusher.setLivePushInfoListener(this)
        alivcLivePusher.setLivePushErrorListener(this)

        alivcLivePusher.startPush("rtmp://livechanneltest.oss-cn-beijing.aliyuncs.com/live/testChannelName2")
    }

    /**
     * 释放直播资源
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun release() {
        if (alivcLivePusher.isPushing) {
            alivcLivePusher.stopPush()
        }
        alivcLivePusher.destroy()
        alivcLivePusher.setLivePushInfoListener(null)
    }

    override fun onPushResumed(p0: AlivcLivePusher?) {
        Log.v(TAG, "onPushResumed")
    }

    override fun onPreviewStarted(p0: AlivcLivePusher?) {
        Log.v(TAG, "onPreviewStarted")
    }

    override fun onAdjustFps(p0: AlivcLivePusher?, p1: Int, p2: Int) {
        Log.v(TAG, "onAdjustFps $p1 | $p2")
    }

    override fun onFirstFramePreviewed(p0: AlivcLivePusher?) {
        Log.v(TAG, "onFirstFramePreviewed")
    }

    override fun onPushStoped(p0: AlivcLivePusher?) {
        Log.v(TAG, "onPushStoped")
    }

    override fun onDropFrame(p0: AlivcLivePusher?, p1: Int, p2: Int) {
        Log.v(TAG, "onDropFrame $p1 * $p2")
    }

    override fun onFirstAVFramePushed(p0: AlivcLivePusher?) {
        Log.v(TAG, "onFirstAVFramePushed")
    }

    override fun onPreviewStoped(p0: AlivcLivePusher?) {
        Log.v(TAG, "onPreviewStoped")
    }

    override fun onAdjustBitRate(p0: AlivcLivePusher?, p1: Int, p2: Int) {
        Log.v(TAG, "onAdjustBitRate $p1 | $p2")
    }

    override fun onPushStarted(p0: AlivcLivePusher?) {
        Log.v(TAG, "onPushStarted")
    }

    override fun onPushPauesed(p0: AlivcLivePusher?) {
        Log.v(TAG, "onPushPauesed")
    }

    override fun onPushRestarted(p0: AlivcLivePusher?) {
        Log.v(TAG, "onPushRestarted")
    }

    override fun onSystemError(p0: AlivcLivePusher?, p1: AlivcLivePushError?) {
        Log.e(TAG, "onSystemError:$p1")
    }

    override fun onSDKError(p0: AlivcLivePusher?, p1: AlivcLivePushError?) {
        Log.d(TAG, "onSDKError:$p1")
        alivcLivePusher.restartPush()
    }
}