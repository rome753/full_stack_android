package cc.rome753.fullstack.manager;

import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import cc.rome753.fullstack.bean.ChatMsg;
import cc.rome753.fullstack.bean.ChatSend;
import cc.rome753.fullstack.event.WsComesEvent;
import cc.rome753.fullstack.event.WsFailureEvent;
import cc.rome753.fullstack.event.WsLeavesEvent;
import cc.rome753.fullstack.event.WsMessageEvent;
import cc.rome753.fullstack.event.WsOpenEvent;
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

    public static final String ALL_NAME = "";
    public static final String ROBOT_NAME = "lara";

    private static String SCHEME="ws://";

    public static String PATH = "chat";

    private static WebSocketCall mWebSocketCall;

    private static WebSocket mWebSocket;

    private static Handler mHandler;

    /**
     * 打开聊天ws连接， 先过滤掉1s内的重复请求
     */
    public static void open(){
//        if(mWebSocket == null) {
//            if(mHandler == null) mHandler = new Handler(){
//                @Override
//                public void handleMessage(Message msg) {
                    openChat();
//                }
//            };
//            mHandler.removeMessages(0);
//            Message message = Message.obtain();
//            message.what = 0;
//            mHandler.sendMessageDelayed(message, 1000);
//        }
    }
    private static void openChat(){
        Log.d("WebSocket", "openChat");
        OkHttpClient client = OkhttpManager.getClient();
        Request request = new Request.Builder().url(SCHEME + OkhttpManager.getHost() + PATH).build();
        mWebSocketCall = WebSocketCall.create(client, request);
        mWebSocketCall.enqueue(new WebSocketListener() {

            @Override
            public void onOpen(okhttp3.ws.WebSocket webSocket, Response response) {
                Log.d("WebSocket", "onOpen");
                mWebSocket = webSocket;
                EventBus.getDefault().post(new WsOpenEvent());
                //注册fsid和name
                String idName = UserManager.getUser().getId()+ UserManager.getUser().getName();
                sendId(idName);
            }

            @Override
            public void onFailure(IOException e, Response response) {
                Log.d("WebSocket","onFailure: "+e.getMessage());
                mWebSocket = null;
                EventBus.getDefault().post(new WsFailureEvent(e.getMessage()));
            }

            @Override
            public void onMessage(ResponseBody message) throws IOException {
                final String msg = message.source().readByteString().utf8();
                Log.d("WebSocket", "onMessage:" + msg);
                if (message.contentType() == WebSocket.TEXT) {
                    ChatMsg wsMsg = new Gson().fromJson(msg, ChatMsg.class);
                    switch (wsMsg.type){
                        case 0://to all
                        case 1://to me
                            EventBus.getDefault().post(new WsMessageEvent(wsMsg.from, wsMsg.msg));
                            break;
                        case 2:// comes
                            EventBus.getDefault().post(new WsComesEvent(wsMsg.from, wsMsg.msg));
                            break;
                        case 3:// leaves
                            EventBus.getDefault().post(new WsLeavesEvent(wsMsg.from));
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
                mWebSocket = null;
//                EventBus.getDefault().post(new WsCloseEvent());
            }
        });
    }

    public static void close(){
        if(mWebSocket != null){
            try {
                mWebSocket.close(1000, "主动关闭");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean sendId(String id){
        ChatSend c = new ChatSend(0, "", id);
        return send(c);
    }

    public static boolean send2All(String msg){
        ChatSend c = new ChatSend(1, ALL_NAME, msg);
        return send(c);
    }

    public static boolean send2User(String to, String msg){
        ChatSend c = new ChatSend(2, to, msg);
        return send(c);
    }

    public static boolean send2Robot(String msg){
        ChatSend c = new ChatSend(3, ROBOT_NAME, msg);
        return send(c);
    }

    private static boolean send(ChatSend chatSend){
        if(mWebSocket != null){
            try {
                String json = new Gson().toJson(chatSend);
                mWebSocket.sendMessage(RequestBody.create(WebSocket.TEXT, json));
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 聊天是否在线
     * @return
     */
    public static boolean isOnline(){
        return mWebSocket != null;
    }

}
