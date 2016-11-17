package cc.rome753.evnet;

/**
 * Created by crc on 16/11/15.
 */

public class WebSocketMessageEvent {
    private String msg;

    public String getMessage() {
        return msg;
    }

    public WebSocketMessageEvent(String msg){
        this.msg = msg;
    }
}
