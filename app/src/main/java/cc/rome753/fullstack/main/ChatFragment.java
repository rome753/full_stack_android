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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.rome753.fullstack.BaseFragment;
import cc.rome753.fullstack.R;
import cc.rome753.fullstack.Utils;
import cc.rome753.fullstack.callback.OnItemClickListener;
import cc.rome753.fullstack.event.WsMessageEvent;
import cc.rome753.fullstack.manager.ChatManager;
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
     * 对方用户名，""代表所有人
     */
    String mName;
    String mAvatar;

    ChatAdapter mAdapter;
    
    List<WsMessageEvent> mMsgList;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, view);
        
        mMsgList = new ArrayList<>();
        mAdapter = new ChatAdapter();
        mRvChat.setLayoutManager(new LinearLayoutManager(mActivity));
        mRvChat.setItemAnimator(new DefaultItemAnimator());
        mAdapter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(int positon, Object data) {
            }
        });
        mRvChat.setAdapter(mAdapter);
        return view;
    }

    class ChatAdapter extends RecyclerView.Adapter{

        private OnItemClickListener onItemClickListener;

        void setOnItemClickListener(OnItemClickListener onItemClickListener){
            this.onItemClickListener = onItemClickListener;
        }
        
        WsMessageEvent getItem(int position){
            return mMsgList.get(position);
        }

        /**
         *  0 - left
         *  1 - right
         * @return
         */
        @Override
        public int getItemViewType(int position) {
            boolean isMe = !mName.equals(getItem(position).from);
            return isMe ? 1 : 0;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mActivity).inflate(
                    viewType == 0 ? R.layout.item_chat_left : R.layout.item_chat_right, parent, false);
            ItemViewHolder holder = new ItemViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            final String msg = getItem(position).msg;
            ((ItemViewHolder)holder).tvMsg.setText(msg);

            String avatarUrl = "";
            if(getItemViewType(position) == 0){
                avatarUrl = mAvatar;
            }else if(getItemViewType(position) == 1){
                avatarUrl = UserManager.getUser().getAvatar();
            }
            Utils.loadAvatar(mActivity, avatarUrl, ((ItemViewHolder)holder).ivAvatar, 15);
//            ((ItemViewHolder)holder).container.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(onItemClickListener != null){
//                        onItemClickListener.onItemClick(holder.getAdapterPosition(), name);
//                    }
//                }
//            });
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
        boolean result;
        switch (mName){
            case ChatManager.ALL_NAME:
                result = ChatManager.send2All(msg);
                break;
            case ChatManager.ROBOT_NAME:
                result = ChatManager.send2Robot(msg);
                break;
            default:
                result = ChatManager.send2User(mName, msg);
                break;
        }
        if (result) {
            mEt.setText("");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWsMessageEvent(WsMessageEvent event) {
//        if (mTv != null) {
//            mTv.append(event.from + "说：" + event.msg + "\n");
//            mSv.fullScroll(View.FOCUS_DOWN);
//        }
        mMsgList.add(event);
        mAdapter.notifyItemInserted(mMsgList.size() - 1);
        mRvChat.smoothScrollToPosition(mMsgList.size() - 1);
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

    @Override
    public void onDestroy() {
        //销毁时隐藏键盘, 无效?
        Utils.hideKeyboard(mActivity, mEt);
        super.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        //切换为hide时隐藏键盘
        if(hidden){
            Utils.hideKeyboard(mActivity, mEt);
        }
    }

}
