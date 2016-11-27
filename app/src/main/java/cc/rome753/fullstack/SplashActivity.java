package cc.rome753.fullstack;

import android.text.TextUtils;

public class SplashActivity extends BaseActivity {


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
                    ChatroomActivity.start(mActivity);
                }
                finish();
            }
        }, 500);
    }

    @Override
    public void initView() {

    }

}
