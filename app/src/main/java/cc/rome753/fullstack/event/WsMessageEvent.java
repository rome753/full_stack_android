package cc.rome753.fullstack.event;

/**
 * Created by crc on 16/11/15.
 */

public class WsMessageEvent {

    public String from;

    public String msg;

    public WsMessageEvent(String from, String msg){
        this.from = from;
        this.msg = msg;
    }
}
