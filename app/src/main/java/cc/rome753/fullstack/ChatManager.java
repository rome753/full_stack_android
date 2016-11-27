package cc.rome753.fullstack;

import android.util.Log;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import cc.rome753.fullstack.bean.ChatMsg;
import cc.rome753.fullstack.bean.ChatSend;
import cc.rome753.fullstack.evnet.WsCloseEvent;
import cc.rome753.fullstack.evnet.WsFailureEvent;
import cc.rome753.fullstack.evnet.WsMsg2AllEvent;
import cc.rome753.fullstack.evnet.WsMsg2MeEvent;
import cc.rome753.fullstack.evnet.WsOpenEvent;
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

public class ChatManager {

    private ChatManager(){}

    final static String url="ws://"+OkhttpManager.HOST+"chat";

    private static WebSocket ws;

    public static void connect(){
        if(ws != null) return;
        OkHttpClient client = OkhttpManager.getClient();
        Request request = new Request.Builder().url(url).build();
        WebSocketCall webSocketCall = WebSocketCall.create(client, request);
        webSocketCall.enqueue(new WebSocketListener() {

            @Override
            public void onOpen(okhttp3.ws.WebSocket webSocket, Response response) {
                Log.d("WebSocket", "onOpen");
                EventBus.getDefault().post(new WsOpenEvent());
                ws = webSocket;
                User.getUser().online = true;
                //注册fsid和name
                String idName = User.getUser().getId()+User.getUser().getName();
                sendId(idName);
            }

            @Override
            public void onFailure(IOException e, Response response) {
                Log.d("WebSocket","onFailure: "+e.getMessage());
                EventBus.getDefault().post(new WsFailureEvent(e.getMessage()));
                ws = null;
                User.getUser().online = false;
            }

            @Override
            public void onMessage(ResponseBody message) throws IOException {
                final String msg = message.source().readByteString().utf8();
                Log.d("WebSocket", "onMessage:" + msg);
                if (message.contentType() == WebSocket.TEXT) {
                    ChatMsg wsMsg = new Gson().fromJson(msg, ChatMsg.class);
                    switch (wsMsg.type){
                        case 0://to all
                            EventBus.getDefault().post(new WsMsg2AllEvent(wsMsg.from, wsMsg.msg));
                            break;
                        case 1://to me
                            EventBus.getDefault().post(new WsMsg2MeEvent(wsMsg.msg));
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                    }
                } else {
                    BufferedSource source = message.source();
                    Log.d("WebSocket", "onMessage:" + source.readByteString());
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
                EventBus.getDefault().post(new WsCloseEvent());
                ws = null;
                User.getUser().online = false;
            }
        });
    }

    private static boolean sendId(String id){
        ChatSend c = new ChatSend(0, "", id);
        return send(c);
    }

    public static boolean send2All(String msg){
        ChatSend c = new ChatSend(1, "", msg);
        return send(c);
    }

    public static boolean send2User(String to, String msg){
        ChatSend c = new ChatSend(2, to, msg);
        return send(c);
    }

    private static boolean send(ChatSend chatSend){
        if(ws != null){
            try {
                String json = new Gson().toJson(chatSend);
                ws.sendMessage(RequestBody.create(WebSocket.TEXT, json));
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //reconnect...
        connect();
        return false;
    }

}
