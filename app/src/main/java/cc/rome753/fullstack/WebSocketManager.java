package cc.rome753.fullstack;

import android.text.TextUtils;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import cc.rome753.fullstack.evnet.WebSocketCloseEvent;
import cc.rome753.fullstack.evnet.WebSocketFailureEvent;
import cc.rome753.fullstack.evnet.WebSocketMessageEvent;
import cc.rome753.fullstack.evnet.WebSocketOpenEvent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.ws.WebSocket;
import okhttp3.ws.WebSocketCall;
import okhttp3.ws.WebSocketListener;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by crc on 16/11/15.
 */

public enum WebSocketManager {

    INSTANCE;

    WebSocket ws;

    final String url="ws://www.rome753.cc/chat";

    String msg = "";

    WebSocketManager(){
        connect();
    }

    public void connect(){
        OkHttpClient client = OkhttpManager.INSTANCE.getClient();
        Request request = new Request.Builder().url(url).build();
        WebSocketCall webSocketCall = WebSocketCall.create(client, request);
        webSocketCall.enqueue(new WebSocketListener() {

            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                Log.d("WebSocketCall", "onOpen");
                EventBus.getDefault().post(new WebSocketOpenEvent());
                ws = webSocket;
            }

            @Override
            public void onFailure(IOException e, Response response) {
                Log.d("WebSocketCall","onFailure");
                EventBus.getDefault().post(new WebSocketFailureEvent(msg));
                e.printStackTrace();
            }

            @Override
            public void onMessage(ResponseBody message) throws IOException {
                final String msg = message.source().readByteString().utf8();
                Log.d("WebSocketCall", "onMessage:" + msg);
                if (message.contentType() == WebSocket.TEXT) {
                    EventBus.getDefault().post(new WebSocketMessageEvent(msg));
                } else {
                    BufferedSource source = message.source();
                    Log.d("WebSocketCall", "onMessage:" + source.readByteString());
                }
                message.close();
            }

            @Override
            public void onPong(Buffer payload) {
                Log.d("WebSocketCall", "onPong:");
            }

            @Override
            public void onClose(int code, String reason) {
                Log.d("WebSocketCall","onClose: "+reason);
                EventBus.getDefault().post(new WebSocketCloseEvent());
                ws = null;
            }
        });
    }

    public void send(String message){
        msg = message;
        if(!TextUtils.isEmpty(msg) && ws!=null){
            try {
                ws.sendMessage(RequestBody.create(WebSocket.TEXT, msg));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
