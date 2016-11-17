package cc.rome753.fullstack;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

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
}
