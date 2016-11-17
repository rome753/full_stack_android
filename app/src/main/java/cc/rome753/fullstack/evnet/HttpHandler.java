package cc.rome753.fullstack.evnet;

import java.io.IOException;

import cc.rome753.fullstack.App;
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
        App.sHandler.post(new Runnable() {
            @Override
            public void run() {
                onFailure(e.getMessage());
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) {
        if(response.isSuccessful()){
            try {
                final String s = response.body().string();
                App.sHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onSuccess(s);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                response.close();
            }
        }
    }
}
