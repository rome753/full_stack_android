package cc.rome753.fullstack.event;

import cc.rome753.fullstack.bean.User;

/**
 * Created by crc on 16/11/15.
 */

public class WsComesEvent {

    public User user;

    public WsComesEvent(String name, String avatar){
        User user = new User();
        user.name = name;
        user.avatar = avatar;
        this.user = user;
    }
}
