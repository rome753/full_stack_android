package cc.rome753.fullstack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import cc.rome753.fullstack.main.ChatFragment;


public class ChatActivity extends BaseActivity {

    public static void start(BaseActivity activity, String name, String avatar) {
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

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent){
        mName = intent.getStringExtra("name");
        mAvatar = intent.getStringExtra("avatar");

        boolean isPair = !TextUtils.isEmpty(mName);
        if(isPair) {
            setTitle("和 " + mName + " 聊天中。。。");
        }else{
            setTitle("群聊中。。。");
        }

        FragmentManager manager = getSupportFragmentManager();
        mChatFragment = (ChatFragment) manager.findFragmentByTag(TAG_CHAT_FRAGMENT);

        if(mChatFragment != null){
            if(isPair){
                if(mChatFragment.isPair) return;
            }else{
                if(mName.equals(mChatFragment.getName())) return;
            }
        }
        if(mChatFragment == null || (isPair != mChatFragment.isPair)){
            if(isPair) {
                mChatFragment = ChatFragment.newInstance(mName, mAvatar);
            }else{
                mChatFragment = ChatFragment.newInstance();
            }
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.container, mChatFragment, TAG_CHAT_FRAGMENT);
            transaction.commit();
        }
    }
}
