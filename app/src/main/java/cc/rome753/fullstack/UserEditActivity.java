package cc.rome753.fullstack;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import cc.rome753.fullstack.event.HttpHandler;
import cc.rome753.fullstack.manager.OkhttpManager;

public class UserEditActivity extends BaseActivity {

    public static void start(BaseActivity activity){
        Intent i = new Intent(activity, UserEditActivity.class);
        activity.startActivity(i);
    }

    @BindView(R.id.et_age)
    EditText etAge;
    @BindView(R.id.et_city)
    EditText etCity;
    @BindView(R.id.et_quote)
    EditText etQuote;

    private String age, city, quote;

    @Override
    public int setView() {
        return R.layout.activity_user_edit;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBar.setDisplayHomeAsUpEnabled(true);

    }

    private boolean check(){
        age = etAge.getText().toString().trim();
        city = etCity.getText().toString().trim();
        quote = etQuote.getText().toString().trim();
        return !TextUtils.isEmpty(age)
            && !TextUtils.isEmpty(city)
            && !TextUtils.isEmpty(quote);
    }

    @OnClick(R.id.btn_submit)
    void submit(){
        if(!check()){
            Utils.toast("请填写完整");
            return;
        }
        JSONObject jo = new JSONObject();
        try {
            jo.put("age", age);
            jo.put("city", city);
            jo.put("quote", quote);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = jo.toString();
        OkhttpManager.post("user", json, new HttpHandler() {
            @Override
            public void onSuccess(String response) {
                Utils.toast(response);
                finish();
            }

            @Override
            public void onFailure() {
            }
        });
    }

}
