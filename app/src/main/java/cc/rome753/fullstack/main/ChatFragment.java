package cc.rome753.fullstack.main;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import cc.rome753.fullstack.BaseFragment;
import cc.rome753.fullstack.R;
import cc.rome753.fullstack.Utils;
import cc.rome753.fullstack.event.WsCloseEvent;
import cc.rome753.fullstack.event.WsFailureEvent;
import cc.rome753.fullstack.event.WsMsg2AllEvent;
import cc.rome753.fullstack.event.WsOpenEvent;
import cc.rome753.fullstack.manager.ChatManager;

/**
 * Created by crc on 16/11/30.
 */
public class ChatFragment extends BaseFragment {

    @BindView(R.id.tv)
    TextView mTv;
    @BindView(R.id.sv)
    ScrollView mSv;
    @BindView(R.id.et)
    EditText mEt;
    @BindView(R.id.btn)
    Button mBtn;

    public static ChatFragment newInstance() {
        Bundle args = new Bundle();

        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int setView() {
        return R.layout.fragment_chat;
    }

    @Override
    public void initView() {
        ChatManager.connect();
    }

    @OnClick(R.id.btn)
    public void sendMsg() {
        String msg = mEt.getText().toString().trim();
        if (ChatManager.send2All(msg)) {
            mEt.setText("");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWebSocketOpenEvent(WsOpenEvent event) {
        Utils.toast("open");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWebSocketCloseEvent(WsCloseEvent event) {
        Utils.toast("close");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWsMsg2AllEvent(WsMsg2AllEvent event) {
        if (mTv != null) {
            mTv.append(event.from + "说：" + event.msg + "\n");
            mSv.fullScroll(View.FOCUS_DOWN);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWebSocketFailureEvent(WsFailureEvent event) {
        if (mEt != null) {
            mEt.setText(event.getMessage());
        }
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

}
