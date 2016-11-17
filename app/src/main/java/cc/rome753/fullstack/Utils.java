package cc.rome753.fullstack;

import android.widget.Toast;

/**
 * Created by Administrator on 2016/11/17.
 */

public class Utils {

    public static void toast(String s){
        Toast.makeText(App.sContext, s, Toast.LENGTH_SHORT).show();
    }
}
