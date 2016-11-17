package cc.rome753;

import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cc.rome753.evnet.WebSocketCloseEvent;
import cc.rome753.evnet.WebSocketFailureEvent;
import cc.rome753.evnet.WebSocketMessageEvent;
import cc.rome753.evnet.WebSocketOpenEvent;

public class MainActivity extends AppCompatActivity {

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

    }

    public void onClick(View v){
        String msg = et.getText().toString().trim();
        WebSocketManager.INSTANCE.send(msg);
        et.setText("");
    }

    public void onClose(View view) {
        OkhttpManager.post("","");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWebSocketOpenEvent(WebSocketOpenEvent event){
        Toast.makeText(this, "open", Toast.LENGTH_SHORT).show();
    }
    
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWebSocketCloseEvent(WebSocketCloseEvent event){
        Toast.makeText(this, "Close", Toast.LENGTH_SHORT).show();
    }
    
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWebSocketMessageEvent(WebSocketMessageEvent event){
        if(tv != null){
            tv.append(event.getMessage() + "\n");
        }
    }  
    
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWebSocketFailureEvent(WebSocketFailureEvent event){
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
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

}
