package com.twotoasters.multicolumnlistadapter.sample;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class FitWidthImageView extends ImageView {

    private float aspectRatio = 1f;

    public FitWidthImageView(Context context) {
        super(context);
    }

    public FitWidthImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FitWidthImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) (width * aspectRatio);
        super.onMeasure(
                widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        );
    }
}
