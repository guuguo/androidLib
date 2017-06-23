工具类库
=======
[![](https://jitpack.io/v/GUUGUO/androidLib.svg)](https://jitpack.io/#GUUGUO/androidLib)

简化自己的 安卓开发工作
# 使用
```groovy
compile 'com.github.GUUGUO:androidLib:0.11'
```


# 依赖的库
```groovy
compile "com.android.support:appcompat-v7:$support_vesion"
compile "com.android.support:design:$support_vesion"
compile 'com.android.support.constraint:constraint-layout:1.0.2'
//function 
compile 'com.orhanobut:logger:1.15'
compile 'com.squareup.okhttp3:okhttp:3.8.0'
compile 'com.tbruyelle.rxpermissions2:rxpermissions:0.9.3@aar'
compile 'com.google.code.gson:gson:2.8.0'
//rxjava
compile 'io.reactivex.rxjava2:rxandroid:2.0.1'
compile 'io.reactivex.rxjava2:rxjava:2.1.0'
compile 'com.artemzin.rxjava:proguard-rules:1.2.7.0'
//view
compile 'com.flyco.roundview:FlycoRoundView_Lib:1.1.4@aar'
compile 'com.flyco.systembar:FlycoSystemBar_Lib:1.0.0@aar'
compile 'com.flyco.dialog:FlycoDialog_Lib:1.3.2@aar'

compile 'am.drawable:drawable:1.4.0'
compile "org.jetbrains.kotlin:kotlin-stdlib-jre7:$kotlin_version"
compile 'me.yokeyword:fragmentation:0.10.6'
```
# Base
- Activity 继承 LNBaseActivity
- Fragment 继承 LNBaseFragment

> 调用 dialog

```java
activity.dialogLoadingShow("加载中")
activity.dialogCompleteShow("成功", null)
activity.dialogErrorShow("失败", null)
activity.dialogWarningShow("确定要这么做吗?", "取消", "确定", new OnBtnClickL() {
    @Override
    public void onBtnClick() {
        Toast.makeText(activity, "你竟然真的这么做了", Toast.LENGTH_LONG).show();
    }
});
```
> 简便地设置 layout和menu等
```java
@Override
protected int getLayoutResId() {
    return R.layout.activity_table;
}
@Override
protected int getMenuResId() {
    return R.menu.menu_manage;
}
```
以及其他通用的utils
===
- NetworkUtil
- AppUtil
- DisplayUtil
- DeviceUtil
- FileUtil
- InputMethodUtils
- ......