package com.example.whiteboardfluttersdkdemo.common

import android.app.Application
import io.flutter.facade.Flutter

/**
 * 自定义应用入口
 *
 * @author 超悟空
 * @version 1.0 2019/4/4
 * @since 1.0 2019/4/4
 **/
class MyApplication :Application(){
    override fun onCreate() {
        super.onCreate()

        Flutter.startInitialization(this)
    }
}