package cc.rome753.fullstack.bean;

/**
 * Created by rome753 on 2016/11/26.
 */

public class ChatMsg {
    public int type;
    public String from;
    public String msg;
    public String time;

    public ChatMsg() {
    }

    public ChatMsg(int type, String from, String msg, String time) {
        this.type = type;
        this.from = from;
        this.msg = msg;
        this.time = time;
    }
}
