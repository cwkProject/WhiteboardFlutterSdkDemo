# Android版白板SDK

* 由flutter编写白板上层UI和控制单元，java/kotlin/C++完成底层数据处理，OpenGL实现白板绘制。

编译后的SDK为aar库和gradle远程依赖组合形式，
其中flutter.aar库已经集成flutter sdk，原生项目无需额外配置flutter环境。

## 安装

* 为项目[引入最新版kotlin支持](https://developer.android.google.cn/kotlin/get-started)

解压`white_board_sdk.zip`

将libs文件夹下的全部aar库导入项目的libs文件夹下，配置gradle依赖项

```
    implementation fileTree(dir: 'libs', include: ['*.jar','*.aar'])
    implementation 'com.google.code.gson:gson:2.8.5'
    implementation 'io.agora.rtc:full-sdk:2.4.0'            // 声网音视频库
    implementation 'com.latitech.sdk:whiteboard:0.9.141'    // 白板底层库
```

增加abiFilters

```
    defaultConfig {
        ...

        ndk {
            abiFilters 'armeabi-v7a'
        }
    }
```

目前flutter版sdk仅支持打包单一arm架构，`armeabi-v7a`或 `arm64-v8a`不可得兼，为了保证兼容性这个仅提供`armeabi-v7a`版本

使用java 1.8编译

```
android {
    ...
    
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = '1.8'
    }
}
```

为了保证flutter engine在加载完成并绘制好第一帧之前的UI表现更加平滑，需要增加闪屏启动页，
在非应用启动场景时仅提供纯白色背景即可很好衔接Flutter UI。

在`style.xml`中加入主题设置

```
    <style name="FlutterTheme" parent="@android:style/Theme.Light.NoTitleBar">
        <!-- Show a splash screen on the activity. Automatically removed when
             Flutter draws its first frame -->
        <item name="android:windowBackground">@drawable/launch_background</item>
    </style>
```

在`drawable`文件夹下新建`launch_background.xml`文件并写入以下内容

```
<?xml version="1.0" encoding="utf-8"?>
<!-- Modify this file to customize your launch splash screen -->
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:drawable="@android:color/white" />

    <!-- You can insert your own image assets here -->
    <!-- <item>
        <bitmap
            android:gravity="center"
            android:src="@mipmap/launch_image" />
    </item> -->
</layer-list>
```

在`AndroidManifest.xml`文件中注册白板activity

```
        <activity android:name="io.flutter.facade.RoomActivity"
                  android:theme="@style/FlutterTheme"
                  android:hardwareAccelerated="true"
                  android:windowSoftInputMode="adjustResize"
                  android:configChanges="orientation|keyboardHidden|keyboard|screenSize|locale|layoutDirection|fontScale|screenLayout|density|uiMode"
                  android:launchMode="singleTop">

            <!-- This keeps the window background of the activity showing
                 until Flutter renders its first frame. It can be removed if
                 there is no splash screen (such as the default splash screen
                 defined in @style/FlutterTheme). -->
            <meta-data
                    android:name="io.flutter.facade.SplashScreenUntilFirstFrame"
                    android:value="true"/>
        </activity>
```

## 使用

1、在自定义的`Application`类的`onCreate`方法中调用
`Flutter.startInitialization(this)` 实现对flutter engine和dart vm的初始化

2、在启动白板房间前，需要准备好加入房间的全部参数，并构建出`RoomInfo`数据类，
然后调用`Flutter.startRoomActivity`启动房间

``` kotlin
        val roomInfo = RoomInfo(
            meetingId, // 房间id，String
            userId,    // 客户自己的用户系统中的用户id，String
            roleId,    // 该用户在本房间中的角色id(可选角色由客户定义)，Int
            userName,  // 用户在房间中显示的名称，可空
            avatar,    // 用户在房间中显示的头像，url，可空
            password   // 房间的加入密码，String，可空，RoomActivity不提供密码输入界面，如果需要，密码输入界面应由客户端自行设计实现
        )

        // 启动房间
        Flutter.startRoomActivity(this, roomInfo)
        
        // 或者直接使用下面的方法
        
        // val intent = Intent(context, RoomActivity::class.java).apply {
        //     putExtra(RoomActivity.ROOM_INFO_TAG, roomInfo)
        // }
        
        // context.startActivity(intent)
```

以上代码均为[kotlin](https://www.kotlincn.net/)编写

## 关于混淆

flutter.aar中已经自带了混淆配置，项目无需额外配置混淆参数

## 其他

以上aar库在Android Studio 3.4中基于Android sdk 28，gradle 5.1.1，android.support，编译生成。
建议客户端使用相同环境构建项目。

如果需要AndroidX 版本请联系我们。