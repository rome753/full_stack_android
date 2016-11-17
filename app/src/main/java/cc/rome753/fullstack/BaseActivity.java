package cc.rome753.fullstack;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/17.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public abstract int setView();
    public abstract void initData();
    public abstract void initView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setView());
        ButterKnife.bind(this);

        initData();
        initView();
    }
}
