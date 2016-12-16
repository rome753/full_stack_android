package cc.rome753.fullstack;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.List;

/**
 * Created by Administrator on 2016/11/17.
 */

public class App extends Application {

    public static Context sContext;

    public static Handler sHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        sHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 当前应用是否在后台
     * @return true-在后台
     */
    public static boolean isBackground() {
        ActivityManager activityManager = (ActivityManager) sContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(sContext.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    Log.i("后台", appProcess.processName);
                    return true;
                }else{
                    Log.i("前台", appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }
}
