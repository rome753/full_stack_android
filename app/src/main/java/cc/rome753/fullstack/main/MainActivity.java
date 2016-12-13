package cc.rome753.fullstack.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.LevelListDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.Menu;

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
import cc.rome753.fullstack.manager.ChatManager;

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
    private static LevelListDrawable mOnlineDrawable;

    private NetworkReceiver mNetworkReceiver;

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
                        return ChatFragment.newInstance("","");
                    case 1:
                        return FindFragment.newInstance();
                    case 2:
                        return UserFragment.newInstance();
                }
                return ChatFragment.newInstance("","");
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
        mNavigator.onCreate(savedInstanceState);

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

        if(mNetworkReceiver == null){
            mNetworkReceiver = new NetworkReceiver();
        }
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkReceiver, filter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        mOnlineDrawable = (LevelListDrawable) menu.findItem(R.id.action_online).getIcon();
        return true;
    }

    public static void setOnlineDrawable(){
        boolean online = ChatManager.isOnline();
        if(mOnlineDrawable != null) {
            mOnlineDrawable.setLevel(online ? 10 : 1);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWebSocketOpenEvent(WsOpenEvent event) {
        setOnlineDrawable();
        Utils.toast("聊天连接成功");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWebSocketFailureEvent(WsFailureEvent event) {
        setOnlineDrawable();
        Utils.toast("聊天连接断开: " + event.getMessage());
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

    @Override
    protected void onDestroy() {
        if(mNetworkReceiver != null){
            unregisterReceiver(mNetworkReceiver);
        }
        super.onDestroy();
    }

    class NetworkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            //网络连接上时开启聊天连接
            if(info != null && info.isConnected()) {
                ChatManager.open();
            }
        }
    }

}
