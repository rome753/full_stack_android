package cc.rome753.fullstack;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/17.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public abstract int setView();

    // add for MainActivity
    protected void initFragmentNavigator(Bundle savedInstanceState){}

    protected BaseActivity mActivity;

    public ActionBar mActionBar;

    protected AlertDialog mWaitDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setView());
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            mActionBar = getSupportActionBar();
        }

        mActivity = this;

        mWaitDialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setIcon(R.drawable.ic_launcher)
                .setMessage("处理中。。。")
                .create();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
