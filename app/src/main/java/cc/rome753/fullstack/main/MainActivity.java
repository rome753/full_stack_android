package cc.rome753.fullstack.main;

import android.content.Intent;
import android.graphics.drawable.LevelListDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import cc.rome753.fullstack.BaseActivity;
import cc.rome753.fullstack.R;
import cc.rome753.fullstack.Utils;
import cc.rome753.fullstack.event.WsFailureEvent;
import cc.rome753.fullstack.event.WsOpenEvent;

public class MainActivity extends BaseActivity {

    public static void start(BaseActivity activity) {
        Intent i = new Intent(activity, MainActivity.class);
        activity.startActivity(i);
    }

    private static final int FRAGMENT_COUNT = 3;
    private static final String TAG_CHAT_FRAGMENT = "chat-fragment";
    private static final String TAG_FIND_FRAGMENT = "find-fragment";
    private static final String TAG_USER_FRAGMENT = "user-fragment";

    //管理底部tab按钮
    @BindView(R.id.bottombar)
    BottomBar mBottombar;

    //管理主页的fragment
    private FragmentNavigator mNavigator;

    //actionbar上显示是否在线(websocket连接状态)
    private static MenuItem mOnlineMenu;

    private LevelListDrawable mOnlineDrawable;

    @Override
    public int setView() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        initFragmentNavigator(savedInstanceState);

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

    @Override
    protected void initFragmentNavigator(Bundle savedInstanceState) {
        mNavigator.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        mOnlineMenu = menu.findItem(R.id.action_online);
        mOnlineDrawable = (LevelListDrawable) mOnlineMenu.getIcon();
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWebSocketOpenEvent(WsOpenEvent event) {
        mOnlineDrawable.setLevel(10);
        Utils.toast("聊天连接成功");
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onWebSocketCloseEvent(WsCloseEvent event) {
//        mOnlineMenu.setChecked(false);
//        Utils.toast("聊天断开");
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWebSocketFailureEvent(WsFailureEvent event) {
        mOnlineDrawable.setLevel(1);
        Utils.toast("聊天连接断开: " + event.getMessage());
    }

}
