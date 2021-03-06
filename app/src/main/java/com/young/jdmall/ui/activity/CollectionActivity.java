package com.young.jdmall.ui.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.young.jdmall.R;
import com.young.jdmall.bean.NewsProductInfoBean;
import com.young.jdmall.network.BaseObserver;
import com.young.jdmall.network.RetrofitFactory;
import com.young.jdmall.ui.adapter.CollectionAdapter;
import com.young.jdmall.ui.utils.PreferenceUtils;
import com.young.jdmall.ui.view.RecyclerLoadMoreView;
import com.young.jdmall.ui.view.RecyclerRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;

/*
 *  创建者:   tiao
 *  创建时间:  2017/8/3 0003 18:57
 *  描述：    TODO
 */
public class CollectionActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.rl_container)
    RecyclerLoadMoreView mRlContainer;
    @BindView(R.id.rl_refresh)
    RecyclerRefreshLayout mRlRefresh;
    private CollectionAdapter mCollectionAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.GRAY);
        }
//        initData();
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        mRlContainer.setLayoutManager(manager);
        mCollectionAdapter = new CollectionAdapter(this);

        mRlContainer.setAdapter(mCollectionAdapter);
        mCollectionAdapter.setOnRefreshListener(new RecyclerLoadMoreView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
    }

    private void initData() {
        final Observable<NewsProductInfoBean> newsObservable = RetrofitFactory.getInstance().listCollectProduct(PreferenceUtils.getUserId(this), 1, 10);
        newsObservable.compose(compose(this.<NewsProductInfoBean>bindToLifecycle())).subscribe(new BaseObserver<NewsProductInfoBean>(this) {
            @Override
            protected void onHandleSuccess(NewsProductInfoBean newsProductInfoBean) {
                if (newsProductInfoBean.getProductList() != null) {
                    mCollectionAdapter.setNewsProductInfoBeen(newsProductInfoBean.getProductList());
                    mRlContainer.onLoadSuccess();

                }
            }

            @Override
            protected void onHandleError(String msg) {
                super.onHandleError(msg);
                mRlContainer.onLoadFailure();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mRlContainer.onLoadFailure();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
        mRlRefresh.setOnRefreshListener(new RecyclerRefreshLayout.OnRefreshListener() {
            @Override
            public void OnRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(2000);
                        mRlRefresh.post(new Runnable() {
                            @Override
                            public void run() {
                                mRlRefresh.closeRefresh();
                            }
                        });
                    }
                }).start();
            }
        });
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }
}
