package com.young.jdmall.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.young.jdmall.R;
import com.young.jdmall.bean.CategoryBaseBean;
import com.young.jdmall.network.BaseObserver;
import com.young.jdmall.network.RetrofitFactory;
import com.young.jdmall.ui.adapter.CategoryLeftAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

import static android.os.Build.VERSION_CODES.M;


/**
 * Created by BjyJyk on 2017/7/30.
 */

public class CategoryFragment extends BaseFragment {

    private static final String TAG = "CategoryFragment";

    @BindView(R.id.catecary_left_list)
    public RecyclerView mCategoryLeftList;
    @BindView(R.id.category_right_contains)
    FrameLayout mCategoryRightContains;
    private Context mContext;
    private CategoryLeftAdapter mCategoryLeftAdapter;
    private CategoryBaseBean mCategoryBean;
    private List<CategoryBaseRightListFragment> fragments = new ArrayList();

    @RequiresApi(api = M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = LayoutInflater.from(getContext()).inflate(R.layout.catecary_fragment, null);
        ButterKnife.bind(this, rootview);
        mContext = getContext();
        init();
        //默认选中第0个


        return rootview;
    }

    private void initFragment() {
        //初始化Fragment
        for (int i = 0; i < 4; i++) {
            fragments.add(new MomAreaFragment(mCategoryBean));
            fragments.add(new FashionLadiesFragment(mCategoryBean));
            fragments.add(new BabyArticlesFragment(mCategoryBean));
            fragments.add(new DailyArticlesFragment(mCategoryBean));
            fragments.add(new ChildrenClothesFragment(mCategoryBean));
            fragments.add(new ChildrenToysFragment(mCategoryBean));
        }


    }


    private int lastcheck = 0;

    public void selectFragment(int position) {
        CategoryBaseRightListFragment baseCategoryRightFragment = fragments.get(position);
        getFragmentManager().beginTransaction().replace(R.id.category_right_contains, baseCategoryRightFragment).commit();
        if (position >4) {
            if (lastcheck < position) {
                mCategoryLeftList.smoothScrollToPosition(position +4);
                lastcheck = position;
            } else {
                lastcheck = position;
                  mCategoryLeftList.smoothScrollToPosition(position -4 );
            }
        }else {
            mCategoryLeftList.smoothScrollToPosition(0);
        }


        }

        /**
         * 初始化
         */

    private void init() {
        //加载网络数据
        loadNetwork();


        //RecyclerView的初始化
        mCategoryLeftList.setLayoutManager(new LinearLayoutManager(getActivity()));

        mCategoryLeftAdapter = new CategoryLeftAdapter(mContext, this);

        mCategoryLeftList.setAdapter(mCategoryLeftAdapter);
    }

    private void loadNetwork() {
        Observable<CategoryBaseBean> categoryObservable = RetrofitFactory.getInstance().listCategory();
        categoryObservable.compose(compose(this.<CategoryBaseBean>bindToLifecycle())).subscribe(new BaseObserver<CategoryBaseBean>(getActivity()) {
            @Override
            protected void onHandleSuccess(CategoryBaseBean categoryBean) {
                mCategoryBean = categoryBean;
                //加载成功再添加Fragment
                initFragment();
                selectFragment(0);
            }
            @Override
            protected void onHandleError(String msg) {
                Log.d(TAG, "onHandleError: 加载失败");
            }
        });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    
}
