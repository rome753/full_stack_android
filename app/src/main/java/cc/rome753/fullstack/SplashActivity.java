package cc.rome753.fullstack;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import cc.rome753.fullstack.main.MainActivity;
import cc.rome753.fullstack.manager.UserManager;

public class SplashActivity extends BaseActivity {

    public static void start(BaseActivity activity){
        Intent i = new Intent(activity, SplashActivity.class);
        activity.startActivity(i);
    }

    @Override
    public int setView() {
        return R.layout.activity_splash;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.sHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(TextUtils.isEmpty(UserManager.getUser().getId())){
                    LoginActivity.start(mActivity);
                }else{
                    MainActivity.start(mActivity);
                }
                finish();
            }
        }, 100);
    }

}
