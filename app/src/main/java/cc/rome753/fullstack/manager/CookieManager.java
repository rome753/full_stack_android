package cc.rome753.fullstack.manager;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import cc.rome753.fullstack.App;
import cc.rome753.fullstack.Utils;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;


/**
 * Created by rome753 on 2016/11/23.
 */

public class CookieManager implements CookieJar{

    private static final String SP_COOKIES = "okhttp_cookies";
    private static final String SP_COOKIES_NAME = "cookies";


    static ConcurrentHashMap<String, List<Cookie>> sUrlCookiesMap = new ConcurrentHashMap<>();

    static SharedPreferences sPrefs = App.sContext.getSharedPreferences(SP_COOKIES, 0);

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        save(url, cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return load(url);
    }

    private static List<Cookie> load(HttpUrl url){
        List<Cookie> cookies = sUrlCookiesMap.get(SP_COOKIES_NAME);
        if(cookies == null){
            String json = sPrefs.getString(SP_COOKIES_NAME, "");
            cookies = Utils.decodeList(json, Cookie.class);
            if(cookies != null) sUrlCookiesMap.put(SP_COOKIES_NAME, cookies);
            cookies = new ArrayList<>();
        }
        return cookies;
    }

    private static void save(HttpUrl url, List<Cookie> cookies){
        if(Utils.isEmpty(cookies)) return;
        sUrlCookiesMap.put(SP_COOKIES_NAME, cookies);
        for(Cookie c : cookies){//从cookie中获取id和name保存到User类中
            if("fsid".equals(c.name())) UserManager.getUser().setId(c.value());
            if("fsname".equals(c.name())) UserManager.getUser().setName(c.value());
        }
        String json = new Gson().toJson(cookies);
        sPrefs.edit().putString(SP_COOKIES_NAME, json).apply();
    }

    public static void clear(){
        sUrlCookiesMap.clear();
        sPrefs.edit().clear().apply();
    }

}
