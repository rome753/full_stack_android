package cc.rome753.fullstack;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.OnClick;
import cc.rome753.fullstack.bean.Login;
import cc.rome753.fullstack.bean.Register;
import cc.rome753.fullstack.evnet.HttpHandler;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    public static void start(BaseActivity activity){
        activity.startActivity(new Intent(activity, LoginActivity.class));
    }

    @BindView(R.id.et_username)
    EditText mEtUsername;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.et_email)
    EditText mEtEmail;

    @BindView(R.id.btn_register)
    Button mBtnRegister;
    @BindView(R.id.btn_login)
    Button mBtnLogin;

    @BindView(R.id.btn_switch)
    Button mBtnSwitch;

    @OnClick(R.id.btn_register)
    void register(){
        String u = mEtUsername.getText().toString().trim();
        String p = mEtPassword.getText().toString().trim();
        String e = mEtEmail.getText().toString().trim();

        if(TextUtils.isEmpty(u) || TextUtils.isEmpty(p) || TextUtils.isEmpty(e)){
            Utils.toast("empty");
            return;
        }

        String json = new Gson().toJson(new Register(u,p,e));
        OkhttpManager.post("register", json, new HttpHandler() {

            @Override
            public void onSuccess(String response) {
                Utils.toast(response);
            }

            @Override
            public void onFailure(String reason) {
                Utils.toast(reason);

            }
        });
    }

    @OnClick(R.id.btn_login)
    void login(){
        final String u = mEtUsername.getText().toString().trim();
        String p = mEtPassword.getText().toString().trim();
        if(TextUtils.isEmpty(u) || TextUtils.isEmpty(p)){
            Utils.toast("empty");
            return;
        }

        String json = new Gson().toJson(new Login(u,p));
        mWaitDialog.show();
        OkhttpManager.post("login", json, new HttpHandler() {

            @Override
            public void onSuccess(String response) {
                mWaitDialog.hide();
                Utils.toast(response);
                User.getUser().setName(u);
                ChatroomActivity.start(mActivity);
                finish();
            }

            @Override
            public void onFailure(String reason) {
                mWaitDialog.hide();
                Utils.toast(reason);

            }
        });
    }


    @OnClick(R.id.btn_switch)
    void switchs(){
        String toLogin = getString(R.string.to_login);
        String toRegister = getString(R.string.to_register);
        boolean isLogin = mBtnSwitch.getText().toString().equals(toRegister);

        mBtnSwitch.setText(isLogin ? toLogin : toRegister);
        mEtEmail.setVisibility(isLogin ? View.VISIBLE : View.GONE);
        mBtnRegister.setVisibility(isLogin ? View.VISIBLE : View.GONE);
        mBtnLogin.setVisibility(isLogin ? View.GONE : View.VISIBLE);
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
        mEtEmail.setText("rome753@163.com");
        mEtUsername.setText("超哥");
        mEtPassword.setText("chao");

    }
}

