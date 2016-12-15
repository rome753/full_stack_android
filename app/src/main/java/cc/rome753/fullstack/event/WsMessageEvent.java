package cc.rome753.fullstack.event;

import cc.rome753.fullstack.bean.ChatMsg;

/**
 * Created by crc on 16/11/15.
 */

public class WsMessageEvent {

    public ChatMsg chatMsg;

    public WsMessageEvent(ChatMsg chatMsg) {
        this.chatMsg = chatMsg;
    }
}
