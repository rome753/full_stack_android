package cc.rome753;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cc.rome753.bean.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by crc on 16/11/15.
 */

public enum OkhttpManager {

    INSTANCE;

//    final static String url="http://www.rome753.cc/";
    final static String url="http://192.168.1.30:8000/";

    OkhttpManager(){
        client = new OkHttpClient.Builder()
                .readTimeout(3000, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(3000, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(3000, TimeUnit.SECONDS)//设置连接超时时间
                .build();
    }

    private OkHttpClient client;

    public OkHttpClient getClient() {
        return client;
    }

    public static void post(String s, String json){
        s = "login";
        json = new Gson().toJson(new User("chris","124"));
        String finalUrl = url + s;
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder().url(finalUrl).post(body).build();
        Call call = INSTANCE.client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()){
                    String s = null;
                    try {
                        s = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        response.close();
                    }
                    Log.e("test",s);
                }
            }
        });

    }
}
