package cc.rome753.fullstack.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import butterknife.BindView;
import cc.rome753.fullstack.BaseActivity;
import cc.rome753.fullstack.R;

public class MainActivity extends BaseActivity {

    public static void start(BaseActivity activity) {
        Intent i = new Intent(activity, MainActivity.class);
        activity.startActivity(i);
    }

    private static final int FRAGMENT_COUNT = 3;
    private static final String TAG_CHAT_FRAGMENT = "chat-fragment";
    private static final String TAG_FIND_FRAGMENT = "find-fragment";
    private static final String TAG_USER_FRAGMENT = "user-fragment";

    @BindView(R.id.bottombar)
    BottomBar mBottombar;

    private FragmentNavigator mNavigator;

    @Override
    public int setView() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        mNavigator = new FragmentNavigator(getSupportFragmentManager(), new FragmentNavigatorAdapter() {

            @Override
            public Fragment onCreateFragment(int position) {
                switch (position){
                    case 0:
                        return ChatFragment.newInstance();
                    case 1:
                        return FindFragment.newInstance();
                    case 2:
                        return UserFragment.newInstance();
                }
                return ChatFragment.newInstance();
            }

            @Override
            public String getTag(int position) {
                switch (position){
                    case 0:
                        return TAG_CHAT_FRAGMENT;
                    case 1:
                        return TAG_FIND_FRAGMENT;
                    case 2:
                        return TAG_USER_FRAGMENT;
                }
                return TAG_CHAT_FRAGMENT;
            }

            @Override
            public int getCount() {
                return FRAGMENT_COUNT;
            }

        }, R.id.contentContainer);
        mNavigator.setDefaultPosition(0);
    }

    @Override
    protected void initFragmentNavigator(Bundle savedInstanceState) {
        mNavigator.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        mBottombar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId){
                    case R.id.tab_chat:
                        mNavigator.showFragment(0);
                        break;
                    case R.id.tab_find:
                        mNavigator.showFragment(1);
                        break;
                    case R.id.tab_user:
                        mNavigator.showFragment(2);
                        break;
                }
            }
        });


    }

}
