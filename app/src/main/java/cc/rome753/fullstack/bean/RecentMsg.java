package cc.rome753.fullstack.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;


/**
 * 聊天接收的消息
 * Created by rome753 on 2016/11/26.
 */
@Entity
public class RecentMsg {

    @Id(autoincrement = true)
    protected Long id;

    /**
     * -1 - 我发送的
     * 0 - 发送给所有人
     * 1 - 发送给我
     */
    public int type;
    public String from;
    public String msg;
    public long time;
    public String avatar;
    @Generated(hash = 326631741)
    public RecentMsg(Long id, int type, String from, String msg, long time,
            String avatar) {
        this.id = id;
        this.type = type;
        this.from = from;
        this.msg = msg;
        this.time = time;
        this.avatar = avatar;
    }
    @Generated(hash = 1016904006)
    public RecentMsg() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getFrom() {
        return this.from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getMsg() {
        return this.msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public long getTime() {
        return this.time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public String getAvatar() {
        return this.avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}
