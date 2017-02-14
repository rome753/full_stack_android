package cc.rome753.fullstack.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.rome753.fullstack.BaseFragment;
import cc.rome753.fullstack.ChatActivity;
import cc.rome753.fullstack.R;
import cc.rome753.fullstack.Utils;
import cc.rome753.fullstack.bean.RecentMsg;
import cc.rome753.fullstack.callback.OnItemClickListener;
import cc.rome753.fullstack.manager.DbManager;

/**
 * Created by crc on 16/11/30.
 */
public class RecentFragment extends BaseFragment {

    @BindView(R.id.rv_recent)
    RecyclerView mRvRecent;

    RecentAdapter mAdapter;
    
    List<RecentMsg> mMsgList;

    public static RecentFragment newInstance() {
        Bundle args = new Bundle();
        RecentFragment fragment = new RecentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent, container, false);
        ButterKnife.bind(this, view);

        mMsgList = new ArrayList<>();
        mAdapter = new RecentAdapter();
        mRvRecent.setLayoutManager(new LinearLayoutManager(mActivity));
        mRvRecent.setItemAnimator(new DefaultItemAnimator());

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int positon, Object data) {
                RecentMsg msg = (RecentMsg) data;
                ChatActivity.start(mActivity, msg.type == 0 ? "" : msg.from, msg.avatar);
            }
        });
        mRvRecent.setAdapter(mAdapter);

        loadMsg();

        return view;
    }

    public void loadMsg(){
        mMsgList = DbManager.getInstance().getRecentMsg();
        mAdapter.notifyDataSetChanged();
    }

    class RecentAdapter extends RecyclerView.Adapter{

        private OnItemClickListener onItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        RecentMsg getItem(int position){
            return mMsgList.get(position);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.item_recent, parent, false);
            ItemViewHolder holder = new ItemViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            final RecentMsg msg = getItem(position);

            Utils.loadAvatar(mActivity, msg.avatar, ((ItemViewHolder)holder).ivAvatar, 15);
            ((ItemViewHolder)holder).tvName.setText(msg.type == 0 ? "群聊" : msg.from);
            ((ItemViewHolder)holder).tvMsg.setText(msg.msg);
            String timeline = Utils.timestampToTimelineTime(msg.time);
            ((ItemViewHolder)holder).tvTime.setText(timeline);

            if(onItemClickListener != null){
                ((ItemViewHolder)holder).container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(holder.getAdapterPosition(), msg);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mMsgList.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder{

            @BindView(R.id.iv_avatar)
            ImageView ivAvatar;
            @BindView(R.id.tv_name)
            TextView tvName;
            @BindView(R.id.tv_msg)
            TextView tvMsg;
            @BindView(R.id.tv_time)
            TextView tvTime;
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
        MainActivity.setOnlineDrawable();
    }

}
