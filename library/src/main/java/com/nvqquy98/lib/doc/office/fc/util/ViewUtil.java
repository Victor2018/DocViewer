package com.nvqquy98.lib.doc.office.fc.util;/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: ViewUtil
 * Author: Victor
 * Date: 2023/11/02 15:03
 * Description:
 * -----------------------------------------------------------------
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;


public class ViewUtil {
    public Bitmap getViewBitmap(ViewGroup viewGroup) {
        //不加下面两句，会报错：width and height must be > 0
        viewGroup.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        int measuredWidth = viewGroup.getMeasuredWidth();
        int measuredHeight = viewGroup.getMeasuredHeight();
        viewGroup.layout(0, 0, measuredWidth, measuredHeight);

        Bitmap bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        viewGroup.draw(canvas);
        return bitmap;
    }
}
