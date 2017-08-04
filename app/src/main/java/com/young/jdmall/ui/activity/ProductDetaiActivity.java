package com.young.jdmall.ui.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gxz.PagerSlidingTabStrip;
import com.young.jdmall.R;
import com.young.jdmall.bean.CommentInfoBean;
import com.young.jdmall.bean.ProductDesInfoBean;
import com.young.jdmall.bean.ProductInfoBean;
import com.young.jdmall.network.BaseObserver;
import com.young.jdmall.network.RetrofitFactory;
import com.young.jdmall.ui.adapter.ItemTitlePagerAdapter;
import com.young.jdmall.ui.fragment.GoodsCommentFragment;
import com.young.jdmall.ui.fragment.GoodsDetailFragment;
import com.young.jdmall.ui.fragment.GoodsInfoFragment;
import com.young.jdmall.ui.widget.DialogConfirmView;
import com.young.jdmall.ui.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;

import static com.young.jdmall.R.id.psts_tabs;
import static com.young.jdmall.R.id.vp_content;

/**
 * Created by 25505 on 2017/8/3.
 */

public class ProductDetaiActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.ll_back)
    LinearLayout mLlBack;
    @BindView(psts_tabs)
    public PagerSlidingTabStrip mPstsTabs;
    @BindView(R.id.tv_title)
    public TextView mTvTitle;
    @BindView(R.id.ll_title_root)
    LinearLayout mLlTitleRoot;
    @BindView(vp_content)
    public NoScrollViewPager mVpContent;
    @BindView(R.id.ll_buy_des)
    LinearLayout mLlBuyDes;
    @BindView(R.id.tv_contact)
    TextView mTvContact;
    @BindView(R.id.tv_shop)
    TextView mTvShop;
    @BindView(R.id.tv_concern)
    TextView mTvConcern;
    @BindView(R.id.iv_concern)
    ImageView mIvConcern;
    @BindView(R.id.ll_concern)
    LinearLayout mLlConcern;
    @BindView(R.id.tv_cart)
    TextView mTvCart;
    @BindView(R.id.tvCount)
    TextView mTvCount;
    @BindView(R.id.tv_buy)
    TextView mTvBuy;

    private List<Fragment> fragmentList = new ArrayList<>();
    public GoodsInfoFragment goodsInfoFragment;
    public GoodsDetailFragment goodsDetailFragment;
    public GoodsCommentFragment goodsCommentFragment;
    private int mId;
    public ProductInfoBean mProductInfoBean;
    public CommentInfoBean mCommentInfoBean;
    public ProductDesInfoBean mDesInfoBean;
    private ItemTitlePagerAdapter mPagerAdapter;
    private boolean isConcern = true;
    public DialogConfirmView mDialogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_detail);
        ButterKnife.bind(this);
        processIntent();
        fragmentList.add(goodsInfoFragment = new GoodsInfoFragment());
        fragmentList.add(goodsDetailFragment = new GoodsDetailFragment());
        fragmentList.add(goodsCommentFragment = new GoodsCommentFragment());
        mPagerAdapter = new ItemTitlePagerAdapter(getFragmentManager(),
                fragmentList, new String[]{"商品", "详情", "评价"});
        mVpContent.setAdapter(mPagerAdapter);
        mVpContent.setOffscreenPageLimit(3);
        mPstsTabs.setViewPager(mVpContent);

    }

    //处理传过来的id进行请求网络
    private void processIntent() {
        if (getIntent() != null) {
            mId = getIntent().getIntExtra("id", 0);
            Log.d("luoyou", "id" + mId);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    //请求网络数据
    private void initData() {
        //请求商品详情
        requestProductDetail();
    }


    private void requestProductDetail() {
        Observable<ProductInfoBean> productObservable = RetrofitFactory.getInstance().listProductInfo(mId);
        productObservable.compose(compose(this.<ProductInfoBean>bindToLifecycle())).subscribe(new BaseObserver<ProductInfoBean>(this) {
            @Override
            protected void onHandleSuccess(ProductInfoBean productInfoBean) {
                mProductInfoBean = productInfoBean;
                //请求商品描述
                requestProductDesc();
            }
        });
    }

    private void requestProductDesc() {
        Observable<ProductDesInfoBean> productDesObservable = RetrofitFactory.getInstance().listProductDes(mId);
        productDesObservable.compose(compose(this.<ProductDesInfoBean>bindToLifecycle())).subscribe(new BaseObserver<ProductDesInfoBean>(this) {
            @Override
            protected void onHandleSuccess(ProductDesInfoBean desInfoBean) {
                mDesInfoBean = desInfoBean;
                //请求商品描述
                requestProductComment();
            }
        });
    }

    private void requestProductComment() {
        Observable<CommentInfoBean> commentObservable = RetrofitFactory.getInstance().listComment(mId, 1, 10);
        commentObservable.compose(compose(this.<CommentInfoBean>bindToLifecycle())).subscribe(new BaseObserver<CommentInfoBean>(this) {
            @Override
            protected void onHandleSuccess(CommentInfoBean commentInfoBean) {
                mCommentInfoBean = commentInfoBean;
                if (commentInfoBean != null) {
                    goodsInfoFragment.setLoopView();
                    goodsCommentFragment.setData(commentInfoBean);
                }
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.tv_contact, R.id.tv_shop, R.id.ll_concern, R.id.tv_cart, R.id.tv_buy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                //判断当前是哪个fragment
                boolean userVisibleHint = goodsInfoFragment.getUserVisibleHint();
                if (userVisibleHint){
                    finish();
                }else{
                    mVpContent.setCurrentItem(0);
                }
                break;
            case R.id.tv_contact:
                // TODO: 2017/8/3  机器人界面

                break;
            case R.id.tv_shop:

                break;
            case R.id.ll_concern:
                //关注
                if (isConcern){
                    mIvConcern.setImageResource(R.mipmap.akc);
                    mTvConcern.setText("已关注");
                    isConcern = false;
                }else{
                    mIvConcern.setImageResource(R.mipmap.akb);
                    mTvConcern.setText("关注");
                    isConcern = true;
                }
                break;
            case R.id.tv_cart:
                Intent intent = new Intent(this,MainActivity.class);
                intent.putExtra("page","detail");
                startActivity(intent);
                break;
            case R.id.tv_buy:
                mDialogView = new DialogConfirmView(this,mProductInfoBean);
                mDialogView.show();
                break;
        }
    }
}