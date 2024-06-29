package com.nvqquy98.lib.doc.office.system.beans.CalloutView;

import android.graphics.Paint;

class MyPaint extends Paint {
	public MyPaint() {
		super();
		setAntiAlias(true);
		setDither(true);
		setStyle(Paint.Style.STROKE);
		setStrokeJoin(Paint.Join.ROUND);
		setStrokeCap(Paint.Cap.ROUND);
	}
}
