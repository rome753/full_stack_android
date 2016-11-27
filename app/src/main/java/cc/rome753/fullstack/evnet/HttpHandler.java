package cc.rome753.fullstack.evnet;

import java.io.IOException;

import cc.rome753.fullstack.App;
import cc.rome753.fullstack.Utils;
import cc.rome753.fullstack.bean.response.Base;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/17.
 */

public abstract class HttpHandler implements Callback {

    public abstract void onSuccess(String response);
    public abstract void onFailure(String reason);

    @Override
    public void onFailure(Call call,final IOException e) {
        e.printStackTrace();
        handleFailure(e.getMessage());
    }

    @Override
    public void onResponse(Call call, Response response) {
        if(response.isSuccessful()){
            try {
                String s = response.body().string();
                Base b = Utils.decode(s, Base.class);
                if(b == null) return;
                final String uft8 = new String(b.reason.getBytes(), "utf-8");
                final int error_code = b.error_code;
                if(error_code >= 200 && error_code < 300){
                    handleSuccess(uft8);
                }else {
                    handleFailure(uft8);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                response.close();
            }
        }else{
            handleFailure(response.code() + "");
        }
    }

    private void handleFailure(final String reason){
        App.sHandler.post(new Runnable() {
            @Override
            public void run() {
                onFailure(reason);
            }
        });
    }

    private void handleSuccess(final String response){
        App.sHandler.post(new Runnable() {
            @Override
            public void run() {
                onSuccess(response);
            }
        });
    }
}
