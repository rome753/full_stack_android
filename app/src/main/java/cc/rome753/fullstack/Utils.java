package cc.rome753.fullstack;

import android.widget.Toast;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/17.
 */

public class Utils {

    public static void toast(String s){
        Toast.makeText(App.sContext, s, Toast.LENGTH_SHORT).show();
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
}
