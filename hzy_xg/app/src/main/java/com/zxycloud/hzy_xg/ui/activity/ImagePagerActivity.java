package com.zxycloud.hzy_xg.ui.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxycloud.hzy_xg.R;
import com.zxycloud.hzy_xg.base.activity.BaseLayoutActivity;
import com.zxycloud.hzy_xg.bean.PostBean.ImagePagerBean;
import com.zxycloud.hzy_xg.bean.base.ImageUploadItemBean;
import com.zxycloud.hzy_xg.utils.Const;
import com.zxycloud.hzy_xg.utils.GlideUtils;
import com.zxycloud.hzy_xg.widget.photoview.HackyViewPager;
import com.zxycloud.hzy_xg.widget.photoview.PhotoView;

import java.util.List;

/**
 * 图片预览页
 *
 * @author leiming
 * @date 2018/6/26 17:18
 */
public class ImagePagerActivity extends BaseLayoutActivity {

    private static final String STATE_POSITION = "STATE_POSITION";
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_DESCRIPE = "image_descripe";
    public static final String EXTRA_IMAGE_URLS = "image_urls";

    private HackyViewPager mPager;
    private int pagerPosition;
    private String imageDescripe;
    private TextView indicator;
    private List<ImageUploadItemBean> urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.picture_preview);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_pager;
    }

    @Override
    protected void findViews() {
        mPager = getView(R.id.pager);
        indicator = getView(R.id.indicator);
    }

    @Override
    protected void formatViews() {
        ImagePagerAdapter mAdapter = new ImagePagerAdapter();
        mPager.setAdapter(mAdapter);

        if (! TextUtils.isEmpty(imageDescripe)) {
            indicator.setText(imageDescripe);
        }
        // 更新下标
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
            }

        });
        mPager.setCurrentItem(pagerPosition);
    }

    @Override
    protected void formatData() {

    }


    @Override
    protected void getBundle(Bundle bundle) {
        if (Const.notEmpty(bundle)) {
            ImagePagerBean imagePagerBean = (ImagePagerBean) bundle.getSerializable(EXTRA_IMAGE_URLS);
            if (Const.notEmpty(imagePagerBean)) {
                urls = imagePagerBean.getImages();
            }
            pagerPosition = bundle.getInt(EXTRA_IMAGE_INDEX);
            imageDescripe = bundle.getString(EXTRA_IMAGE_DESCRIPE);
        }
    }

    @Override
    public void onClick(View v) {

    }

    private class ImagePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Const.judgeListNull(urls);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            GlideUtils.loadImageView(mContext, urls.get(position).getImgUrl(), photoView);

            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
