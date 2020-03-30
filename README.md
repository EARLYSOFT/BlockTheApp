# BlockTheApp
A library for blocking some apps or handling specific actions, by filtering the package name and class name of the current activity.

<img src="https://www.earlysoft.co.kr/wp-content/uploads/2019/04/2-e1556021739988-1.jpg" width="100" height="50" title="Earlysoft BlockTheApp" alt="https://www.earlysoft.co.kr"></img>

## Features
- Filtering the package name and class name of current actitivy
- Support BroadcastReceiver for callback
- Support Handler for callback
- ActivityLifecycleCallbacks
- Check Permisstion for android.permission.PACKAGE_USAGE_STATS
(android.os.Build.VERSION_CODES.KITKAT or higher) 

## Getting Started
To start with this, we need to simply add the library in the libs folder.

## Setting up the AndroidManifest.xml
### Add Permissions
```
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
    <uses-permission
        android:name="android.permission.PACKAGE_USAGE_STATS"
        tools:ignore="ProtectedPermissions" />
```
### Add EarlyBlockTheAppReceiver
Add Callback Receiver in the application tag
```
    <receiver android:name="kr.co.earlysoft.blocktheapp.EarlyBlockTheAppReceiver" >
        <intent-filter>
            <action android:name="kr.co.earlysoft.intent.action.ACTION_BLOCKTHEAPP" />
        </intent-filter>
    </receiver>
```
### Add EarlyLifeCycleService
Add EarlyLifeCycleService in the application tag
```
    <service android:name="kr.co.earlysoft.blocktheapp.EarlyLifeCycleService" />
```

## Build
register ActivityLifecycleCallbacks in **Application**
```
    @Override
    public void onCreate() {
        super.onCreate();
        **registerActivityLifecycleCallbacks(new EarlyActivityLifeCycleCallbacks(getApplicationContext()));**
    }
```
