package cc.rome753.fullstack.manager;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

import cc.rome753.fullstack.App;
import cc.rome753.fullstack.bean.ChatMsg;
import cc.rome753.fullstack.bean.RecentMsg;
import cc.rome753.fullstack.dao.gen.ChatMsgDao;
import cc.rome753.fullstack.dao.gen.DaoMaster;
import cc.rome753.fullstack.dao.gen.DaoSession;
import cc.rome753.fullstack.dao.gen.RecentMsgDao;

/**
 * Created by crc on 16/12/14.
 */
public class DbManager {

    private static DbManager mInstance;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private ChatMsgDao mChatMsgDao;
    private RecentMsgDao mRecentMsgDao;

    private DbManager() {
        //不同登录用户用不同db
        String name = UserManager.getUser().getName();
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(App.sContext, "fullstack-db-"+name, null);
        mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
        mChatMsgDao = mDaoSession.getChatMsgDao();
        mRecentMsgDao = mDaoSession.getRecentMsgDao();
    }

    /**
     * 用户登出, 注销实例
     */
    public void close(){
        mInstance = null;
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

    /**
     * 更新RecentMsg表中的一条记录, 如果记录不存在, 就插入
     * @param msg
     */
    public void updateRecentMsg(RecentMsg msg){
        WhereCondition condition;
        if(msg.type == 0){
            condition = RecentMsgDao.Properties.Type.eq(0);
        }else{
            condition = RecentMsgDao.Properties.From.eq(msg.from);
        }

        RecentMsg oldMsg = mRecentMsgDao.queryBuilder().where(condition).unique();
        if(oldMsg == null){
            mRecentMsgDao.insert(msg);
        }else {
            msg.setId(oldMsg.getId());
            mRecentMsgDao.update(msg);
        }
    }

    /**
     * 最近聊天列表, 按时间降序排列
     * @return
     */
    public List<RecentMsg> getRecentMsg(){
        return mRecentMsgDao.queryBuilder().orderDesc(RecentMsgDao.Properties.Time).list();
    }
}
