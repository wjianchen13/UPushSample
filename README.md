## U-Push SDK Sample

本项目为友盟推送SDK集成的示例工程

### 账号申请
在[U-Push官网](http://message.umeng.com/)添加应用申请Appkey。  
详细路径：U-Push官网->应用->新建应用->创建新应用。

### 集成说明
1. app/build.gradle中替换您的应用id
```groovy
android {
    defaultConfig {
        applicationId "您的应用id"
    }
}
```

2. PushConstants类中替换Appkey、MessageSecret和Channel等
```java
class PushConstants {
    /**
     * 应用申请的Appkey
     */
    public static final String APP_KEY = "应用申请的Appkey";

    /**
     * 应用申请的UmengMessageSecret
     */
    public static final String MESSAGE_SECRET = "应用申请的UmengMessageSecret";

    /**
     * 渠道名称
     */
    public static final String CHANNEL = "Umeng";
}
```

3. PushHelper类初步封装PushSDK的初始化
```java
class PushHelper {
    /**
     * 预初始化。
     * 场景：用户未同意隐私政策协议授权时
     *
     * @param context 应用上下文
     */
    public static void preInit(Context context) {
        ...
    }
    
    /**
     * 初始化。
     * 场景：用户已同意隐私政策协议授权时
     *
     * @param context 应用上下文
     */
    public static void init(Context context) {
        ...
    }

    /**
     * 注册设备推送通道（小米、华为等设备的推送）
     * @param context 应用上下文
     */
    private static void registerDeviceChannel(Context context) {
        ...
    }
}
```

4. MyApplication类onCreate()方法中调用预初始化或初始化
```java
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
```

5. MainActivity类中，用户同意隐私政策协议后，执行初始化
```java
public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //用户点击隐私协议同意按钮后，初始化PushSDK
        findViewById(R.id.btn_privacy_agreement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPreferences.getInstance(getApplicationContext()).setAgreePrivacyAgreement(true);
                PushHelper.init(getApplicationContext());
            }
        });
    }
}
```

6. 控制台报错说明，该报错不影响推送业务，请忽略
```
java.lang.ClassNotFoundException: com.ut.mini.UTAnalytics
```

**具体使用，请参考UPushSample工程**

修改记录：
distributionUrl=https\://services.gradle.org/distributions/gradle-6.1.1-all.zip
        classpath 'com.android.tools.build:gradle:4.0.1'

修改meven
        maven { url 'https://repo1.maven.org/maven2/' }


添加下面代码
        pushAgent.setResourcePackageName("com.umeng.message.demo");





















