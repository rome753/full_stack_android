package cc.rome753.fullstack;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import cc.rome753.fullstack.main.ChatFragment;


public class ChatActivity extends BaseActivity {

    public static void start(BaseActivity activity, @NonNull String name, @NonNull String avatar) {
        Intent i = new Intent(activity, ChatActivity.class);
        i.putExtra("name", name);
        i.putExtra("avatar", avatar);
        activity.startActivity(i);
    }

    static final String TAG_CHAT_FRAGMENT = "chat-fragment";

    String mName;
    String mAvatar;

    ChatFragment mChatFragment;

    @Override
    public int setView() {
        return R.layout.activity_chat;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActionBar.setDisplayHomeAsUpEnabled(true);
        mName = getIntent().getStringExtra("name");
        mAvatar = getIntent().getStringExtra("avatar");

        setTitle("和 " + mName + " 聊天中");

        FragmentManager manager = getSupportFragmentManager();
        mChatFragment = (ChatFragment) manager.findFragmentByTag(TAG_CHAT_FRAGMENT);
        if(mChatFragment == null){
            mChatFragment = ChatFragment.newInstance(mName, mAvatar);
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.container, mChatFragment, TAG_CHAT_FRAGMENT);
            transaction.commit();
        }
    }


}
