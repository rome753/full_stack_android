package cc.rome753.fullstack.main;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import cc.rome753.fullstack.BaseFragment;
import cc.rome753.fullstack.LoginActivity;
import cc.rome753.fullstack.R;
import cc.rome753.fullstack.Utils;
import cc.rome753.fullstack.event.HttpHandler;
import cc.rome753.fullstack.manager.OkhttpManager;
import cc.rome753.fullstack.manager.User;

/**
 * Created by crc on 16/11/30.
 */

public class UserFragment extends BaseFragment {

    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;

    @OnClick(R.id.iv_avatar)
    void uploadAvatar(){
        OkhttpManager.upload("avatar", new File("/storage/sdcard1/playground.png"), new HttpHandler() {
            @Override
            public void onSuccess(String response) {
                Utils.toast(response);
            }

            @Override
            public void onFailure(String reason) {
                Utils.toast(reason);
            }
        });
    }

    @OnClick(R.id.btn_logout)
    void logout(){
        OkhttpManager.get("logout", null, new HttpHandler() {
            @Override
            public void onSuccess(String response) {
                Utils.toast(response);
                User.getUser().logout();
                LoginActivity.start(mActivity);
                mActivity.finish();
            }

            @Override
            public void onFailure(String reason) {
                Utils.toast(reason);
            }
        });
    }

    public static UserFragment newInstance() {
        Bundle args = new Bundle();

        UserFragment fragment = new UserFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int setView() {
        return R.layout.fragment_user;
    }

    @Override
    public void initView() {
        Glide.with(mActivity).load(Uri.parse("http://imgsrc.baidu.com/forum/pic/item/ae366e061d950a7b21eb6dba0dd162d9f3d3c9b4.jpg")).into(ivAvatar);
    }
}
