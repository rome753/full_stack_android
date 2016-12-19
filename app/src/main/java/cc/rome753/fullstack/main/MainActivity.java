package cc.rome753.fullstack.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.LevelListDrawable;
import android.net.ConnectivityManager;
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
import cc.rome753.fullstack.ChatConnectionService;
import cc.rome753.fullstack.R;
import cc.rome753.fullstack.Utils;
import cc.rome753.fullstack.bean.User;
import cc.rome753.fullstack.event.WsComesEvent;
import cc.rome753.fullstack.event.WsFailureEvent;
import cc.rome753.fullstack.event.WsLeavesEvent;
import cc.rome753.fullstack.event.WsMessageEvent;
import cc.rome753.fullstack.event.WsOpenEvent;
import cc.rome753.fullstack.manager.ChatManager;

public class MainActivity extends BaseActivity {

    public static void start(BaseActivity activity) {
        Intent i = new Intent(activity, MainActivity.class);
        activity.startActivity(i);
    }

    private static final int FRAGMENT_COUNT = 3;
    private static final String TAG_RECENT_FRAGMENT = "recent-fragment";
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
        EventBus.getDefault().register(this);

        mNavigator = new FragmentNavigator(getSupportFragmentManager(), new FragmentNavigatorAdapter() {

            @Override
            public Fragment onCreateFragment(int position) {
                switch (position){
                    case 0:
                        return RecentFragment.newInstance();
                    case 1:
                        return FindFragment.newInstance();
                    case 2:
                        return UserFragment.newInstance();
                }
                return RecentFragment.newInstance();
            }

            @Override
            public String getTag(int position) {
                switch (position){
                    case 0:
                        return TAG_RECENT_FRAGMENT;
                    case 1:
                        return TAG_FIND_FRAGMENT;
                    case 2:
                        return TAG_USER_FRAGMENT;
                }
                return TAG_RECENT_FRAGMENT;
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
                    case R.id.tab_recent:
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_online){
        }
        return super.onOptionsItemSelected(item);
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
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWebSocketFailureEvent(WsFailureEvent event) {
        setOnlineDrawable();
        Utils.toast("聊天连接断开: " + event.getMessage());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWsComesEvent(WsComesEvent event) {
        FindFragment fragment = (FindFragment) mNavigator.getFragment(1);
        User user = event.user;
        fragment.addUser(user);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWsLeavesEvent(WsLeavesEvent event) {
        FindFragment fragment = (FindFragment) mNavigator.getFragment(1);
        String name = event.name;
        fragment.removeUser(name);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWsMessageEvent(WsMessageEvent event) {
        RecentFragment fragment = (RecentFragment) mNavigator.getFragment(0);
        fragment.loadMsg();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(new Intent(this, ChatConnectionService.class));
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        stopService(new Intent(this, ChatConnectionService.class));
        ChatManager.close();
        if(mNetworkReceiver != null){
            unregisterReceiver(mNetworkReceiver);
        }
        super.onDestroy();
    }

    class NetworkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
//            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo info = manager.getActiveNetworkInfo();
//            //网络连接上时开启聊天连接
//            if(info != null && info.isConnected()) {
//                ChatManager.open();
//            }
        }
    }

}
