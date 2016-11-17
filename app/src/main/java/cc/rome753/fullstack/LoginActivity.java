package cc.rome753.fullstack;

import android.text.TextUtils;
import android.widget.EditText;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.OnClick;
import cc.rome753.fullstack.bean.User;
import cc.rome753.fullstack.evnet.HttpHandler;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_username)
    EditText mEtUsername;
    @BindView(R.id.et_password)
    EditText mEtPassword;

    @OnClick(R.id.btn_login)
    void login(){
        String u = mEtUsername.getText().toString().trim();
        String p = mEtPassword.getText().toString();
        if(TextUtils.isEmpty(u) || TextUtils.isEmpty(p)){
            Utils.toast("empty");

        }

        String json = new Gson().toJson(new User(u,p));
        OkhttpManager.post("login", json, new HttpHandler() {
            @Override
            public void onSuccess(String response) {
                Utils.toast("login success: "+response);
            }

            @Override
            public void onFailure(String reason) {
                Utils.toast("login fail: "+reason);

            }
        });
    }


    @Override
    public int setView() {
        return R.layout.activity_login;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }
}

