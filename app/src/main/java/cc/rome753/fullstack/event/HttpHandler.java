package cc.rome753.fullstack.event;

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

    /**
     * 调用者json返回信息或reason字符串
     * @param response json返回信息或reason字符串
     */
    public abstract void onSuccess(String response);

    /**
     * 通知调用者失败, 不传递信息
     */
    public abstract void onFailure();

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
                final String reason = new String(b.reason.getBytes(), "utf-8");
                final int error_code = b.error_code;
                switch (error_code){
                    case 200: // 无返回数据, 只有reason
                        handleSuccess(reason);
                        break;
                    case 201: // 有返回数据
                        handleSuccess(s);
                        break;
                    default:
                        handleFailure(reason);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                handleFailure("请求失败: " + e.getMessage());
            } finally {
                response.close();
            }
        }else{
            handleFailure("请求失败: " + response.code());
        }
    }

    private void handleFailure(final String reason){
        App.sHandler.post(new Runnable() {
            @Override
            public void run() {
                Utils.toast(reason);
                onFailure();
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
