package cc.rome753.fullstack;

import android.content.Intent;
import android.text.TextUtils;

import cc.rome753.fullstack.main.MainActivity;
import cc.rome753.fullstack.manager.User;

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
    public void initData() {

        App.sHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(TextUtils.isEmpty(User.getUser().getId())){
                    LoginActivity.start(mActivity);
                }else{
                    MainActivity.start(mActivity);
                }
                finish();
            }
        }, 100);
    }

    @Override
    public void initView() {

    }

}
