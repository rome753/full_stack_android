package cc.rome753.fullstack.manager;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import cc.rome753.fullstack.App;
import cc.rome753.fullstack.ChatActivity;
import cc.rome753.fullstack.R;
import cc.rome753.fullstack.bean.ChatMsg;

/**
 * Created by rome753 on 2016/12/13.
 */

public class NoticeManager {

    private NoticeManager(){
        mManager = (NotificationManager) App.sContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static NoticeManager getInstance(){
        return Holder.INSTANCE;
    }

    private static class Holder{
        static final NoticeManager INSTANCE = new NoticeManager();
    }

    private NotificationManager mManager;

    public void showNotification(ChatMsg msg){
        //当前有活动显示的话, 不弹通知
        if(!App.isBackground()){
            return;
        }
        //自己发的消息, 不弹通知
        if(msg.type == -1 || msg.from.equals(UserManager.getUser().getName())){
            return;
        }
        Context context = App.sContext;

        Intent intent = new Intent(context, ChatActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setAutoCancel(true)
                        .setFullScreenIntent(pendingIntent, true)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("新消息")
                        .setContentText(msg.from + "： " + msg.msg);

// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, ChatActivity.class);
        String name = msg.type == 0 ? "" : msg.from;
        resultIntent.putExtra("name", name);
// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(ChatActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
// mId allows you to update the notification later on.
        mManager.notify(0, mBuilder.build());
    }

    public void clear(){
        mManager.cancelAll();
    }
}
