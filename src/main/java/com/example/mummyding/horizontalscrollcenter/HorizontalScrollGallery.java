package com.example.mummyding.horizontalscrollcenter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by MummyDing on 2017/2/9.
 */

public class HorizontalScrollGallery extends RecyclerView {

    private static final String TAG = "HorizontalScrollGallery";
    private LinearLayoutManager mLayoutManager;
    private Context mContext;
    private GalleryAdapter mAdapter;
    private boolean isFirstTime = true;

    public HorizontalScrollGallery(Context context) {
        this(context, null);
    }

    public HorizontalScrollGallery(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalScrollGallery(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    private void init() {
        mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        setLayoutManager(mLayoutManager);
        mAdapter = new GalleryAdapter(mContext);
        setAdapter(mAdapter);
//        scrollToCenter(Integer.MAX_VALUE / 2);
        scrollToPosition(Integer.MAX_VALUE / 2);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        View firstView = mLayoutManager.findViewByPosition(mLayoutManager.findFirstVisibleItemPosition());
        View lastView = mLayoutManager.findViewByPosition(mLayoutManager.findLastVisibleItemPosition());
        boolean isScrollRight = velocityX >= 0;
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int viewWidth = firstView.getWidth();
        int leftEdge = firstView.getRight();
        int rightEdge = screenWidth - lastView.getLeft();
        int standardSpec = (screenWidth % viewWidth) / 2;
        if (isScrollRight) {
            if (leftEdge <= standardSpec) {
                smoothScrollBy(-(standardSpec - leftEdge), 0);
            } else {
                smoothScrollBy(-((viewWidth - leftEdge) + standardSpec), 0);
            }
        } else {
            if (rightEdge <= standardSpec) {
                smoothScrollBy((standardSpec - rightEdge), 0);
            } else {
                smoothScrollBy((viewWidth - rightEdge + standardSpec), 0);
            }
        }
        return true;
    }

    private void scrollToCenter(int position) {
        View view = mLayoutManager.findViewByPosition(position);
        int leftEdge = view.getLeft();
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int viewWidth = view.getWidth();
        int scrollSpec = leftEdge - (screenWidth - viewWidth) / 2;
        smoothScrollBy(scrollSpec, 0);
    }

    @Override
    public void onScrollStateChanged(int state) {
        if (isFirstTime && state == SCROLL_STATE_IDLE) {
            isFirstTime = false;
            scrollToCenter(Integer.MAX_VALUE / 2);
        }
        super.onScrollStateChanged(state);
    }

    private class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.VH> {

        private Context mContext;

        public GalleryAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            VH vh = new VH(LayoutInflater.from(mContext).inflate(R.layout.item_horizontal, parent, false));
            return vh;
        }

        @Override
        public void onBindViewHolder(VH holder, final int position) {
            holder.mTextView.setText("标签" + position);
            holder.mTextView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    scrollToCenter(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return Integer.MAX_VALUE;
        }
        class VH extends RecyclerView.ViewHolder {
            TextView mTextView;
            public VH(View itemView) {
                super(itemView);
                mTextView = (TextView) itemView.findViewById(R.id.text_label);
            }
        }

    }
}
