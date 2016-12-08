package cc.rome753.fullstack.manager;

import android.support.annotation.NonNull;

import java.io.File;
import java.util.concurrent.TimeUnit;

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

//    public static final String HOST = "www.rome753.cc/";
//    public static final String HOST = "192.168.31.247:8000/";
    public static final String HOST = "192.168.1.28:8000/";

    private final static String url_prefix="http://" + HOST;

    private static class Holder{
        static final OkHttpClient sClient = new OkHttpClient.Builder()
                .readTimeout(3000, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(3000, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(3000, TimeUnit.SECONDS)//设置连接超时时间
                .cookieJar(new CookieManager())
                .build();
    }

    static OkHttpClient getClient() {
        return Holder.sClient;
    }

    public static void get(String url, HttpHandler handler){
        String finalUrl = url_prefix + url;
        Request request = new Request.Builder().url(finalUrl).build();
        Call call = getClient().newCall(request);
        call.enqueue(handler);
    }

    public static void post(String url, String json, HttpHandler handler){
        String finalUrl = url_prefix + url;
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder().url(finalUrl).post(body).build();
        Call call = getClient().newCall(request);
        call.enqueue(handler);
    }

    public static void upload(String url, @NonNull File file, HttpHandler handler){
        String finalUrl = url_prefix + url;
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
