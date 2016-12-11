package cc.rome753.fullstack.bean;

/**
 * 聊天接收的消息
 * Created by rome753 on 2016/11/26.
 */

public class ChatMsg {

    /**
     * 0 - 发送给所有人
     * 1 - 发送给我
     * 2 - 某人来了
     * 3 - 某人走了
     */
    public int type;
    public String from;
    public String msg;
    public String time;

}
