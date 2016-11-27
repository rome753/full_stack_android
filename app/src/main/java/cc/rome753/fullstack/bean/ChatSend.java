package cc.rome753.fullstack.bean;

/**
 * Created by rome753 on 2016/11/26.
 */

public class ChatSend {
    /**
     * 0 - send id
     * 1 - send to someone
     * 2 - send to all
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
