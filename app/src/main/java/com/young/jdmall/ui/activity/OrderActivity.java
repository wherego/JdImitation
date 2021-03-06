package com.young.jdmall.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.young.jdmall.R;
import com.young.jdmall.ui.adapter.OrderPageAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 25505 on 2017/8/2.
 */

public class OrderActivity extends BaseActivity {
    @BindView(R.id.ib_sweep)
    ImageView mIbSweep;
    @BindView(R.id.tv_news)
    TextView mTvNews;
    @BindView(R.id.top_container)
    LinearLayout mTopContainer;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.order_vp)
    ViewPager mOrderVp;
    private String[] mTitles = {"全部", "待付款", "待收货", "已完成", "已取消"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        OrderPageAdapter adapter = new OrderPageAdapter(getFragmentManager());
        mOrderVp.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mOrderVp);
    }

    @OnClick({R.id.ib_sweep, R.id.tv_news})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_sweep:
                finish();
                break;
            case R.id.tv_news:
                break;
        }
    }
}
