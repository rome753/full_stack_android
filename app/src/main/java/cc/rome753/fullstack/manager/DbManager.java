package cc.rome753.fullstack.manager;

import java.util.List;

import cc.rome753.fullstack.App;
import cc.rome753.fullstack.bean.ChatMsg;
import cc.rome753.fullstack.dao.gen.ChatMsgDao;
import cc.rome753.fullstack.dao.gen.DaoMaster;
import cc.rome753.fullstack.dao.gen.DaoSession;

/**
 * Created by crc on 16/12/14.
 */
public class DbManager {

    private static DbManager mInstance;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private ChatMsgDao mChatMsgDao;

    private DbManager() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(App.sContext, "fullstack-db", null);
        DaoMaster mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
        mChatMsgDao = mDaoSession.getChatMsgDao();
    }

    public static DbManager getInstance() {
        if (mInstance == null) {
            mInstance = new DbManager();
        }
        return mInstance;
    }

    /**
     * 消息记录保存到数据库
     * @param chatMsg
     */
    public void addChatMsg(ChatMsg chatMsg){
        mChatMsgDao.insert(chatMsg);
    }

    /**
     * 获取某人发来的消息
     * @param from
     * @return
     */
    public List<ChatMsg> getChatMsgFrom(String from){
        List<ChatMsg> chatMsgList = mChatMsgDao.queryBuilder().where(
                ChatMsgDao.Properties.From.eq(from),//来自某人
                ChatMsgDao.Properties.Type.notEq(0)//type!=0, 非聊天室消息
        ).list();
        return chatMsgList;
    }

    /**
     * 获取聊天室的消息
     * @param type 0 聊天室
     * @return
     */
    public List<ChatMsg> getChatMsgType(int type){
        List<ChatMsg> chatMsgList = mChatMsgDao.queryBuilder().where(ChatMsgDao.Properties.Type.eq(type)).list();
        return chatMsgList;
    }
}
