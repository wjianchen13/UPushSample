package com.umeng.message.demo;

import android.app.Application;

import com.umeng.commonsdk.utils.UMUtils;
import com.umeng.message.demo.helper.PushHelper;

/**
 * 应用程序类
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PushHelper.preInit(this);

        initPushSDK();
//        initPushSDK2();

    }

    /**
     * 场景一：需用户隐私政策协议同意后，再初始化
     */
    private void initPushSDK() {
        /*
         * 当用户同意隐私政策协议时，直接进行初始化；
         * 当用户未同意隐私政策协议时，需等用户同意后，再通过PushHelper.init(...)方法进行初始化。
         */
        boolean agreed = MyPreferences.getInstance(getApplicationContext()).hasAgreePrivacyAgreement();
        if (agreed) {
            PushHelper.init(getApplicationContext());
        }
    }

    /**
     * 场景二：如果考虑启动速度等，可在子线程做初始化
     */
    private void initPushSDK2() {
        if (UMUtils.isMainProgress(this)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    PushHelper.init(getApplicationContext());
                }
            }).start();
        }
    }
}
