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

/**
 * Created by rome753 on 2016/12/13.
 */

public class NotificationUtils {

    public static void showMsg(String from, String msg){
        if(from.equals(UserManager.getUser().getName())){
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
                        .setContentText(from + "： " + msg);

// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, ChatActivity.class);
        resultIntent.putExtra("name", from);
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
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());
    }
}
