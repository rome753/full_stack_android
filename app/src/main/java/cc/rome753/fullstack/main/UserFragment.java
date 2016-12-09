package cc.rome753.fullstack.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.rome753.fullstack.BaseFragment;
import cc.rome753.fullstack.ImageActivity;
import cc.rome753.fullstack.LoginActivity;
import cc.rome753.fullstack.R;
import cc.rome753.fullstack.UserEditActivity;
import cc.rome753.fullstack.Utils;
import cc.rome753.fullstack.bean.User;
import cc.rome753.fullstack.event.HttpHandler;
import cc.rome753.fullstack.manager.ChatManager;
import cc.rome753.fullstack.manager.GlideRoundTransform;
import cc.rome753.fullstack.manager.OkhttpManager;
import cc.rome753.fullstack.manager.UserManager;

/**
 * Created by crc on 16/11/30.
 */

public class UserFragment extends BaseFragment {

    public static UserFragment newInstance() {
        Bundle args = new Bundle();

        UserFragment fragment = new UserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_quote)
    TextView tvQuote;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.btn_logout)
    Button btnLogout;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;

    private User mUser;

    @OnClick(R.id.iv_avatar)
    void showAvatar() {
        ImageActivity.start(mActivity, mUser.avatar);
    }

    @OnClick(R.id.btn_logout)
    void logout() {
        String url = "logout";
        OkhttpManager.get(url, new HttpHandler() {
            @Override
            public void onSuccess(String response) {
                Utils.toast(response);
                UserManager.getUser().logout();
                ChatManager.close();
                LoginActivity.start(mActivity);
                mActivity.finish();
            }

            @Override
            public void onFailure() {
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        requestUser();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, view);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestUser();
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_edit).setVisible(true);
        //menu状态被重置, 需要刷新
        MainActivity.setOnlineDrawable();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_edit){
            UserEditActivity.start(mActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestUser(){
        swipe.setRefreshing(true);
        List<Pair<String, String>> list = new ArrayList<>();
        list.add(new Pair<>("name", UserManager.getUser().getName()));
        String url = "user" + Utils.getUrlStr(list);
        OkhttpManager.get(url, new HttpHandler() {
            @Override
            public void onSuccess(String response) {
                swipe.setRefreshing(false);
                mUser = Utils.decode(response, User.class);
                if(mUser == null) return;

                if(!TextUtils.isEmpty(mUser.avatar)) {
                    int radius = Utils.dp2px(50);
                    Glide.with(mActivity).load(mUser.avatar)
                            .transform(new GlideRoundTransform(mActivity, radius)).into(ivAvatar);
                }

                tvName.setText("姓名: " + mUser.name);
                tvAge.setText("年龄: " + mUser.age);
                tvCity.setText("城市: " + mUser.city);
                tvQuote.setText("签名: " + mUser.quote);
                tvTime.setText("注册时间: " + mUser.register_time);
            }

            @Override
            public void onFailure() {
                swipe.setRefreshing(false);
            }
        });
    }

}
