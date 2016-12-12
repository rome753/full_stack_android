package cc.rome753.fullstack.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.rome753.fullstack.BaseFragment;
import cc.rome753.fullstack.ChatActivity;
import cc.rome753.fullstack.R;
import cc.rome753.fullstack.Utils;
import cc.rome753.fullstack.bean.response.OnlineUsers;
import cc.rome753.fullstack.callback.OnItemClickListener;
import cc.rome753.fullstack.event.HttpHandler;
import cc.rome753.fullstack.event.WsComesEvent;
import cc.rome753.fullstack.event.WsLeavesEvent;
import cc.rome753.fullstack.manager.OkhttpManager;
import cc.rome753.fullstack.view.DividerItemDecoration;

import static cc.rome753.fullstack.main.MainActivity.setOnlineDrawable;
import static cc.rome753.fullstack.manager.ChatManager.ROBOT_NAME;

/**
 * Created by crc on 16/11/30.
 */

public class FindFragment extends BaseFragment {

    @BindView(R.id.rvUsers)
    RecyclerView mRvUsers;
    @BindView(R.id.swipe)
    SwipeRefreshLayout mSwipe;

    UserAdapter mAdapter;

    View mHeaderView;

    List<String> mUserList;

    public static FindFragment newInstance() {
        Bundle args = new Bundle();

        FindFragment fragment = new FindFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        ButterKnife.bind(this, view);

        mUserList = new ArrayList<>();

        mAdapter = new UserAdapter();
        mRvUsers.setLayoutManager(new LinearLayoutManager(mActivity));
        mRvUsers.setItemAnimator(new DefaultItemAnimator());
        mRvUsers.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST));
        mAdapter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(int positon, Object data) {
                String name = (String) data;
                ChatActivity.start(mActivity, name);
            }
        });
        mRvUsers.setAdapter(mAdapter);

        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData();
            }
        });

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        requestData();
    }

    protected void requestData(){
        mSwipe.setRefreshing(true);
        OkhttpManager.get("online_users", new HttpHandler() {
            @Override
            public void onSuccess(String response) {
                mSwipe.setRefreshing(false);
                OnlineUsers online = Utils.decode(response, OnlineUsers.class);
                if(online == null) return;

                mUserList.clear();
                mUserList.addAll(online.online_users);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure() {
                mSwipe.setRefreshing(false);
            }
        });
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWsComesEvent(WsComesEvent event) {
        String name = event.name;
        if(!mUserList.contains(name)){
            mUserList.add(name);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWsLeavesEvent(WsLeavesEvent event) {
        String name = event.name;
        if(mUserList.contains(name)){
            mUserList.remove(name);
            mAdapter.notifyDataSetChanged();
        }
    }

    class UserAdapter extends RecyclerView.Adapter{

        private OnItemClickListener onItemClickListener;

        void setOnItemClickListener(OnItemClickListener onItemClickListener){
            this.onItemClickListener = onItemClickListener;
        }

        /**
         *  0 - header
         *  1 - item
         * @param position
         * @return
         */
        @Override
        public int getItemViewType(int position) {
            return position == 0 ? 0 : 1;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            if(viewType == 0){
                if(mHeaderView == null) {
                    //todo HeaderView的不同布局
                    mHeaderView = LayoutInflater.from(mActivity).inflate(R.layout.item_online_user, parent, false);
                    mHeaderView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ChatActivity.start(mActivity, ROBOT_NAME);
                        }
                    });
                }
                HeaderViewHolder holder = new HeaderViewHolder(mHeaderView);
                holder.tvName.setText(ROBOT_NAME);
                return holder;
            }
            View view = LayoutInflater.from(mActivity).inflate(R.layout.item_online_user, parent, false);
            ItemViewHolder holder = new ItemViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            if(getItemViewType(position) == 0){
                return;
            }
            final String name = mUserList.get(position - 1);
            ((ItemViewHolder)holder).tvName.setText(name);
            ((ItemViewHolder) holder).container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null){
                        onItemClickListener.onItemClick(holder.getAdapterPosition(), name);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mUserList.size() + 1;
        }

        class HeaderViewHolder extends RecyclerView.ViewHolder{

            @BindView(R.id.iv_avatar)
            ImageView ivAvatar;
            @BindView(R.id.tv_name)
            TextView tvName;

            HeaderViewHolder(View headerView) {
                super(headerView);
                ButterKnife.bind(this, headerView);
            }
        }

        class ItemViewHolder extends RecyclerView.ViewHolder{

            @BindView(R.id.iv_avatar)
            ImageView ivAvatar;
            @BindView(R.id.tv_name)
            TextView tvName;
            @BindView(R.id.container)
            View container;

            ItemViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //menu状态被重置, 需要刷新
        setOnlineDrawable();
    }

}
