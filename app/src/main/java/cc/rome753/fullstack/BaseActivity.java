package cc.rome753.fullstack;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by rome753 on 2016/11/17.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public abstract int setView();

    protected BaseActivity mActivity;

    public ActionBar mActionBar;

    protected SweetAlertDialog mDialog;

    protected void showDialog(){
        if(mDialog == null){
            mDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        }
        mDialog.setTitleText("...");
        mDialog.setCancelable(false);
        mDialog.show();
    }

    protected void hideDialog(){
        if(mDialog != null){
            mDialog.hide();
        }
    }


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

    }

    @Override
    protected void onDestroy() {
        if(mDialog != null){
            mDialog.dismiss();
        }
        super.onDestroy();
    }

    /**
     * 左上角返回键finish页面
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
