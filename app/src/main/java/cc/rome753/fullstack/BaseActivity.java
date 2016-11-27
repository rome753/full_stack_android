package cc.rome753.fullstack;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/17.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public abstract int setView();
    public abstract void initData();
    public abstract void initView();

    protected BaseActivity mActivity;

    protected AlertDialog mWaitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setView());
        ButterKnife.bind(this);

        mActivity = this;

        initData();
        initView();

        mWaitDialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setIcon(R.drawable.ic_launcher)
                .setMessage("处理中。。。")
                .create();
    }
}
