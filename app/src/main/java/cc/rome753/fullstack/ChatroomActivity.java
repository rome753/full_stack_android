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

import cc.rome753.fullstack.evnet.WsCloseEvent;
import cc.rome753.fullstack.evnet.WsFailureEvent;
import cc.rome753.fullstack.evnet.WsMsg2AllEvent;
import cc.rome753.fullstack.evnet.WsOpenEvent;


public class ChatroomActivity extends BaseActivity {

    public static void start(BaseActivity activity){
        Intent i = new Intent(activity, ChatroomActivity.class);
        activity.startActivity(i);
    }

    TextView tv;
    EditText et;
    ScrollView sv;

    NotificationManagerCompat manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = NotificationManagerCompat.from(this);

        tv = (TextView) findViewById(R.id.tv);
        et = (EditText) findViewById(R.id.et);
        sv = (ScrollView) findViewById(R.id.sv);

        ChatManager.connect();
    }

    public void onClick(View v){
        String msg = et.getText().toString().trim();
        if(ChatManager.send2All(msg)) {
            et.setText("");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWebSocketOpenEvent(WsOpenEvent event){
        Toast.makeText(this, "open", Toast.LENGTH_SHORT).show();
    }
    
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWebSocketCloseEvent(WsCloseEvent event){
        Toast.makeText(this, "Close", Toast.LENGTH_SHORT).show();
    }
    
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWsMsg2AllEvent(WsMsg2AllEvent event){
        if(tv != null){
            tv.append(event.from+"说："+event.msg + "\n");
        }
    }  
    
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWebSocketFailureEvent(WsFailureEvent event){
        if(et != null){
            et.setText(event.getMessage());
        }
    }

    public void showNotice(String msg){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setTicker(msg);
        builder.setSmallIcon(android.R.drawable.ic_menu_send);
        builder.setContentTitle(msg);
        builder.setContentText(msg);
        builder.setOngoing(false);

        manager.notify(0,builder.build());
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

    @Override
    public int setView() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }
}
