package cc.rome753.fullstack.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.rome753.fullstack.BaseFragment;
import cc.rome753.fullstack.R;
import cc.rome753.fullstack.event.WsMsg2AllEvent;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, view);
        ChatManager.open();
        return view;
    }

    @OnClick(R.id.btn)
    public void sendMsg() {
        String msg = mEt.getText().toString().trim();
        if (ChatManager.send2All(msg)) {
            mEt.setText("");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWsMsg2AllEvent(WsMsg2AllEvent event) {
        if (mTv != null) {
            mTv.append(event.from + "说：" + event.msg + "\n");
            mSv.fullScroll(View.FOCUS_DOWN);
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
