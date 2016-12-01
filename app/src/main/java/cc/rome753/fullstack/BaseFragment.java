package cc.rome753.fullstack;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by crc on 16/11/30.
 */

public abstract class BaseFragment extends Fragment {

    public abstract int setView();
    public abstract void initView();

    protected BaseActivity mActivity;

    protected View mRootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(setView(), container, false);
        ButterKnife.bind(this, mRootView);

        initView();
        return mRootView;
    }

}
