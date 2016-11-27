package cc.rome753.fullstack;

import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by rome753 on 2016/11/26.
 */

public class User {
    
    private static final String SP_USER = "user";
    
    private String id;
    private String name;
    public boolean online;
    
    SharedPreferences mPrefs = App.sContext.getSharedPreferences(SP_USER, 0);
    
    private User(){}
    
    public static User getUser(){
        return Holder.sUser;
    }
    
    private static class Holder{
        private static final User sUser = new User();
    }
    
    public void setId(String id){
        if(TextUtils.isEmpty(id)) return;
        this.id = id;
        mPrefs.edit().putString("id", id).apply();
    }
    
    public String getId(){
        if(TextUtils.isEmpty(id)){
            id = mPrefs.getString("id", "");
        }
        return id;
    }
    
    public void setName(String name){
        if(TextUtils.isEmpty(name)) return;
        this.name = name;
        mPrefs.edit().putString("name", name).apply();
    }
    
    public String getName(){
        if(TextUtils.isEmpty(name)){
            name = mPrefs.getString("name", "");
        }
        return name;
    }

}
