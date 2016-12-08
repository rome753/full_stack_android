package cc.rome753.fullstack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import cc.rome753.fullstack.event.WsCloseEvent;
import cc.rome753.fullstack.event.WsFailureEvent;
import cc.rome753.fullstack.event.WsMsg2AllEvent;
import cc.rome753.fullstack.event.WsOpenEvent;
import cc.rome753.fullstack.manager.ChatManager;


public class ChatroomActivity extends BaseActivity {

    public static void start(BaseActivity activity) {
        Intent i = new Intent(activity, ChatroomActivity.class);
        activity.startActivity(i);
    }

    @BindView(R.id.tv)
    TextView mTv;
    @BindView(R.id.sv)
    ScrollView mSv;
    @BindView(R.id.et)
    EditText mEt;

    NotificationManagerCompat manager;

    @Override
    public int setView() {
        return R.layout.activity_chatroom;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onClick(View v) {
        String msg = mEt.getText().toString().trim();
        if (ChatManager.send2All(msg)) {
            mEt.setText("");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWebSocketOpenEvent(WsOpenEvent event) {
        Toast.makeText(this, "open", Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWebSocketCloseEvent(WsCloseEvent event) {
        Toast.makeText(this, "Close", Toast.LENGTH_SHORT).show();
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

    public void showNotice(String msg) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setTicker(msg);
        builder.setSmallIcon(android.R.drawable.ic_menu_send);
        builder.setContentTitle(msg);
        builder.setContentText(msg);
        builder.setOngoing(false);

        manager.notify(0, builder.build());
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }


}
