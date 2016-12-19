package cc.rome753.fullstack;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Pair;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import cc.rome753.fullstack.manager.GlideRoundTransform;

import static cc.rome753.fullstack.App.sContext;

/**
 * Created by rome753 on 2016/11/17.
 */

public class Utils {

    public static void toast(String s){
        Toast.makeText(sContext, s, Toast.LENGTH_SHORT).show();
    }

    public static <T> T decode(String json, Class<T> clazz){
        T result = new Gson().fromJson(json, clazz);
        return result;
    }

    public static <T> List<T> decodeList(String json, Class<T> clazz){
        Type type = new ListParameterizedType(clazz);
        List<T> result =  new Gson().fromJson(json, type);
        return result;
    }

    public static String getUrlStr(List<Pair<String,String>> list){
        if(isEmpty(list)) return "";

        StringBuilder sb = new StringBuilder();
        for(Pair<String, String> pair : list){
            try {
                if(sb.length() > 0) sb.append("&");
                sb.append(URLEncoder.encode(pair.first,"utf-8"));
                sb.append("=");
                sb.append(URLEncoder.encode(pair.second,"utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return "?" + sb.toString();
    }

    public static <T> boolean isEmpty(List<T> list){
        return list == null || list.size() == 0;
    }


    private static class ListParameterizedType implements ParameterizedType {

        private Type type;

        private ListParameterizedType(Type type) {
            this.type = type;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[] {type};
        }

        @Override
        public Type getRawType() {
            return ArrayList.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }

    }

    public static String saveBitmap(Bitmap bmp) {
        String cachePath = sContext.getExternalCacheDir().getPath();
        File file = new File(cachePath, "temp.jpg");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 85, fos);
            fos.flush();
            fos.close();
            return file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    // 将px转换成dp值
    public static int px2dp(int px) {
        float scale = sContext.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    // 将dp转换成px值
    public static int dp2px(float dp) {
        final float scale = sContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    // 将sp转换成px值
    public static int sp2px(float spValue) {
        final float fontScale = sContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 隐藏键盘, activity的windowSoftInputMode会影响
     * @param activity
     */
    public static void hideKeyboard(BaseActivity activity){
        InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
    }

    public static void loadAvatar(Context context, String url, ImageView imageView, int radiusDp){
        if(!TextUtils.isEmpty(url)){
            int radius = Utils.dp2px(radiusDp);
            Glide.with(context).load(url).transform(new GlideRoundTransform(context, radius))
                    .into(imageView);

        }
    }
}
