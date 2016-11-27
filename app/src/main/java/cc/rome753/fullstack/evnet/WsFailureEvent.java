package cc.rome753.fullstack.evnet;

/**
 * Created by crc on 16/11/15.
 */

public class WsFailureEvent {
    private String msg;

    public String getMessage() {
        return msg;
    }

    public WsFailureEvent(String msg){
        this.msg = msg;
    }
}
