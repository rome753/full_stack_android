package cc.rome753.fullstack.bean;

/**
 * 聊天发送的消息
 * Created by rome753 on 2016/11/26.
 */

public class ChatSend {
    /**
     * 0 - 向服务器发送cookie
     * 1 - 发送给所有人
     * 2 - 发送给某人
     * 3 - 发送给机器人
     */
    public int type;
    public String to;
    public String msg;

    public ChatSend() {
    }

    public ChatSend(int type, String to, String msg) {

        this.type = type;
        this.to = to;
        this.msg = msg;
    }
}
