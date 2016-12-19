package cc.rome753.fullstack.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.rome753.fullstack.BaseFragment;
import cc.rome753.fullstack.R;
import cc.rome753.fullstack.Utils;
import cc.rome753.fullstack.bean.ChatMsg;
import cc.rome753.fullstack.bean.User;
import cc.rome753.fullstack.event.WsMessageEvent;
import cc.rome753.fullstack.manager.ChatManager;
import cc.rome753.fullstack.manager.DbManager;
import cc.rome753.fullstack.manager.UserManager;

/**
 * Created by crc on 16/11/30.
 */
public class ChatFragment extends BaseFragment {

    @BindView(R.id.rv_chat)
    RecyclerView mRvChat;
    @BindView(R.id.et)
    EditText mEt;
    @BindView(R.id.btn)
    Button mBtn;

    /**
     * 是否两人聊天
     */
    public boolean isPair;

    /**
     * 对方用户名
     */
    String mName;
    /**
     * 对方头像
     */
    String mAvatar;

    ChatAdapter mAdapter;
    
    List<ChatMsg> mMsgList;


    public static ChatFragment newInstance() {
        Bundle args = new Bundle();
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ChatFragment newInstance(String name, String avatar) {
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("avatar", avatar);
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mName = getArguments().getString("name");
        mAvatar = getArguments().getString("avatar");

        mName = mName == null ? "" : mName;
        isPair = !TextUtils.isEmpty(mName);

        loadMsg();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, view);

        mAdapter = new ChatAdapter();
        mRvChat.setLayoutManager(new LinearLayoutManager(mActivity));
        mRvChat.setItemAnimator(new DefaultItemAnimator());

        mRvChat.setAdapter(mAdapter);
        if(mMsgList.size() > 0) {
            mRvChat.smoothScrollToPosition(mMsgList.size() - 1);
        }

        mRvChat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    Utils.hideKeyboard(mActivity);
                }
                return false;
            }
        });

        return view;
    }

    public void loadMsg(){
        if(TextUtils.isEmpty(mName)){
            mMsgList = DbManager.getInstance().getChatMsgType(0);
        }else {
            mMsgList = DbManager.getInstance().getChatMsgFrom(mName);
        }
    }

    /**
     * 正在聊天的对方名字
     * @return
     */
    public String getName(){
        return mName;
    }

    class ChatAdapter extends RecyclerView.Adapter{

        final int TYPE_ME = -1;     //我发的消息,放右边
        final int TYPE_OTHER = 1;   //别人发的消息,放左边
        
        ChatMsg getItem(int position){
            return mMsgList.get(position);
        }

        @Override
        public int getItemViewType(int position) {
            ChatMsg msg = getItem(position);
            if(isPair){
                return msg.type/* == -1 ? TYPE_ME : TYPE_OTHER*/;
            }
            return msg.from.equals(UserManager.getUser().getName()) ? TYPE_ME : TYPE_OTHER;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mActivity).inflate(
                    viewType == TYPE_ME ? R.layout.item_chat_right : R.layout.item_chat_left, parent, false);
            ItemViewHolder holder = new ItemViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            final String msg = getItem(position).msg;
            ((ItemViewHolder)holder).tvMsg.setText(msg);

            String avatarUrl = "";
            if(getItemViewType(position) == TYPE_OTHER){
                if(isPair){
                    avatarUrl = mAvatar;
                }else {
                    User user = UserManager.getUser().getOnlineUsers().get(getItem(position).from);
                    avatarUrl = user == null ? "" : user.avatar;
                }
            }else /*if(getItemViewType(position) == TYPE_ME)*/{
                avatarUrl = UserManager.getUser().getAvatar();
            }
            Utils.loadAvatar(mActivity, avatarUrl, ((ItemViewHolder)holder).ivAvatar, 15);
        }

        @Override
        public int getItemCount() {
            return mMsgList.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder{

            @BindView(R.id.iv_avatar)
            ImageView ivAvatar;
            @BindView(R.id.tv_msg)
            TextView tvMsg;
            @BindView(R.id.container)
            View container;

            ItemViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

    }


    @OnClick(R.id.btn)
    public void sendMsg() {
        String msg = mEt.getText().toString().trim();
        if(TextUtils.isEmpty(msg)) return;

        boolean result = isPair ? ChatManager.send2User(mName, msg) : ChatManager.send2All(msg);

        if (result) {
            mEt.setText("");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWsMessageEvent(WsMessageEvent event) {
        ChatMsg msg = event.chatMsg;
        if((msg.type == 0 && !isPair)
                || (msg.type != 0 && msg.from.equals(mName))) {
            mMsgList.add(event.chatMsg);
            mAdapter.notifyItemInserted(mMsgList.size() - 1);
            mRvChat.smoothScrollToPosition(mMsgList.size() - 1);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //menu状态被重置, 需要刷新
        MainActivity.setOnlineDrawable();
    }

}
