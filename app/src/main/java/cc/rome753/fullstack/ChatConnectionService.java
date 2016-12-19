package cc.rome753.fullstack;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

import cc.rome753.fullstack.manager.ChatManager;

/**
 * 后台服务, 每5秒检测socket连接, 若断开, 自动连接
 */
public class ChatConnectionService extends Service {

    Thread mThread;

    boolean isRun;

    public ChatConnectionService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isRun = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(mThread == null){
            mThread = new Thread(){
                @Override
                public void run() {
                    while (isRun){
                        ChatManager.open();
                        SystemClock.sleep(5000);
                    }
                }
            };
            mThread.start();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        isRun = false;
        super.onDestroy();
    }
}
