package cc.rome753.fullstack.manager;

import android.support.annotation.NonNull;

import java.io.File;
import java.util.concurrent.TimeUnit;

import cc.rome753.fullstack.App;
import cc.rome753.fullstack.event.HttpHandler;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by crc on 16/11/15.
 */

public class OkhttpManager {

    private OkhttpManager(){}

    private static String HOST = App.sContext.getSharedPreferences("config", 0)
            .getString("host", "www.rome753.cc/");

    private static String SCHEME="http://";

    public static void setHost(String host){
        if(!HOST.equals(host)){
            HOST = host;
            App.sContext.getSharedPreferences("config", 0)
                    .edit().putString("host", HOST).apply();
        }
    }

    public static String getHost(){
        return HOST;
    }

    private static class Holder{
        static final OkHttpClient sClient = new OkHttpClient.Builder()
                .readTimeout(100, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(100, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(3, TimeUnit.SECONDS)//设置连接超时时间
                .cookieJar(new CookieManager())
                .build();
    }

    static OkHttpClient getClient() {
        return Holder.sClient;
    }

    public static void get(String path, HttpHandler handler){
        String finalUrl = SCHEME + HOST + path;
        Request request = new Request.Builder().url(finalUrl).build();
        Call call = getClient().newCall(request);
        call.enqueue(handler);
    }

    public static void post(String path, String json, HttpHandler handler){
        String finalUrl = SCHEME + HOST + path;
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder().url(finalUrl).post(body).build();
        Call call = getClient().newCall(request);
        call.enqueue(handler);
    }

    public static void upload(String path, @NonNull File file, HttpHandler handler){
        String finalUrl = SCHEME + HOST + path;
        RequestBody fileBody = MultipartBody.create(MediaType.parse("application/octet-stream"), file);
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();
        Request request = new Request.Builder().url(finalUrl).post(body).build();
        Call call = getClient().newCall(request);
        call.enqueue(handler);
    }

}
