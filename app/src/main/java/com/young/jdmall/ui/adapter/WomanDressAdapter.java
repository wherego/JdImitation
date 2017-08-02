package com.young.jdmall.ui.adapter;

import android.content.Context;
import android.view.View;

import com.young.jdmall.bean.CategoryBaseBean;
import com.young.jdmall.ui.widget.CategoryBaseRightItemView;

import java.util.List;

/**
 * Created by Administrator on 2017/7/30.
 * 包名:com.example.administrator.jdx.Adapter
 * 时间:2017/7/30
 */

public class WomanDressAdapter extends CategoryBaseRightAdapter<CategoryBaseBean> {

    private static final String TAG = "WomanDressAdapter";
    private List<CategoryBaseBean> mDataList;

    public WomanDressAdapter(Context context, List<CategoryBaseBean> dataList) {
        super(context, dataList);
        mDataList = dataList;
    }

    @Override
    protected void onBindItemView(View convertView, int position) {
        ((CategoryBaseRightItemView) convertView).bind(position,mDataList);
    }

    @Override
    protected View getItemVIew(int position) {
        return new CategoryBaseRightItemView(getContext());
    }
}
