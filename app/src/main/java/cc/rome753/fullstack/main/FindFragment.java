package cc.rome753.fullstack.main;

import android.os.Bundle;

import cc.rome753.fullstack.BaseFragment;
import cc.rome753.fullstack.R;

/**
 * Created by crc on 16/11/30.
 */

public class FindFragment extends BaseFragment {

    public static FindFragment newInstance() {
        Bundle args = new Bundle();

        FindFragment fragment = new FindFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int setView() {
        return R.layout.fragment_find;
    }

    @Override
    public void initView() {
    }

}
