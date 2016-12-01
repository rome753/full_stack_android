package cc.rome753.fullstack.event;

/**
 * Created by crc on 16/11/15.
 */

public class WsMessageEvent {
    private String msg;

    public String getMessage() {
        return msg;
    }

    public WsMessageEvent(String msg){
        this.msg = msg;
    }
}
