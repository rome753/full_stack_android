package cc.rome753.fullstack.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;


/**
 * 聊天接收的消息
 * Created by rome753 on 2016/11/26.
 */
@Entity
public class ChatMsg {

    @Id(autoincrement = true)
    private Long id;

    /**
     * 0 - 发送给所有人
     * 1 - 发送给我
     * 2 - 某人来了
     * 3 - 某人走了
     */
    public int type;
    public String from;
    public String msg;
    public long time;

    @Generated(hash = 73958405)
    public ChatMsg(Long id, int type, String from, String msg, long time) {
        this.id = id;
        this.type = type;
        this.from = from;
        this.msg = msg;
        this.time = time;
    }

    @Generated(hash = 1355502543)
    public ChatMsg() {
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
