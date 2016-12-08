package cc.rome753.fullstack.manager;

import android.content.SharedPreferences;
import android.text.TextUtils;

import cc.rome753.fullstack.App;

/**
 * Created by rome753 on 2016/11/26.
 */

public class UserManager {
    
    private static final String SP_USER = "user";
    
    private String id;
    private String name;
    
    SharedPreferences mPrefs;
    
    private UserManager(){}
    
    public static UserManager getUser(){
        return Holder.sUser;
    }
    
    private static class Holder{
        private static final UserManager sUser = new UserManager();
    }
    
    public void setId(String id){
        if(TextUtils.isEmpty(id)) return;
        this.id = id;
        getPrefs().edit().putString("id", id).apply();
    }
    
    public String getId(){
        if(TextUtils.isEmpty(id)){
            id = getPrefs().getString("id", "");
        }
        return id;
    }
    
    public void setName(String name){
        if(TextUtils.isEmpty(name)) return;
        this.name = name;
        getPrefs().edit().putString("name", name).apply();
    }
    
    public String getName(){
        if(TextUtils.isEmpty(name)){
            name = getPrefs().getString("name", "");
        }
        return name;
    }

    private SharedPreferences getPrefs(){
        if(mPrefs == null)
            mPrefs = App.sContext.getSharedPreferences(SP_USER, 0);
        return mPrefs;
    }

    public void logout(){
        id = "";
        name = "";
        getPrefs().edit().clear().apply();
        CookieManager.clear();
    }

}
