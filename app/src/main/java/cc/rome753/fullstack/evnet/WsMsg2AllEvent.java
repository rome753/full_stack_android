package cc.rome753.fullstack.evnet;

/**
 * Created by crc on 16/11/15.
 */

public class WsMsg2AllEvent {

    public String from;

    public String msg;

    public WsMsg2AllEvent(String from, String msg){
        this.from = from;
        this.msg = msg;
    }
}