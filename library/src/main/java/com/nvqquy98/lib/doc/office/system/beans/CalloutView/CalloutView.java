package com.nvqquy98.lib.doc.office.system.beans.CalloutView;

import java.util.List;

import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.system.IControl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Region.Op;
import android.view.MotionEvent;
import android.view.View;

public class CalloutView extends View {
	private float zoom = 1.0f;
	private float mX, mY;
	private static final float TOUCH_TOLERANCE = 4;
	private List<PathInfo> mPathList = null;
	private PathInfo mPathInfo = null;
	private final int offset = 5;
	private IExportListener mListener;
	private int left = 0;
	private int top = 0;
	private IControl control;
	private CalloutManager calloutMgr;
	private Runnable runnable = null;
	private int index = 0;

	public CalloutView(Context context, IControl control,
			IExportListener listener) {
		super(context);
		this.control = control;
		mListener = listener;
		calloutMgr = control.getSysKit().getCalloutManager();
	}

	public void setZoom(float zoom) {
		this.zoom = zoom;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}

	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Rect clipRect = canvas.getClipBounds();
		mPathList = calloutMgr.getPath(index, false);
		if (mPathList != null) {
			for (int i = 0; i < mPathList.size(); i++) {
				PathInfo pathInfo = mPathList.get(i);
				MyPaint paint = new MyPaint();
				paint.setStrokeWidth(pathInfo.width);
				paint.setColor(pathInfo.color);

				canvas.save();
				canvas.clipRect(left, top, clipRect.right, clipRect.bottom);
				canvas.scale(zoom, zoom);
				canvas.drawPath(pathInfo.path, paint);
				canvas.restore();
			}
		}
	}

	public void setClip(int left, int top) {
		this.left = left;
		this.top = top;
	}

	private void touch_start(float x, float y) {
		x /= zoom;
		y /= zoom;
		mX = x;
		mY = y;

		if (calloutMgr.getDrawingMode() == MainConstant.DRAWMODE_CALLOUTDRAW) {
			mPathInfo = new PathInfo();
			mPathInfo.path = new Path();
			mPathInfo.path.moveTo(x, y);
			mPathInfo.color = calloutMgr.getColor();
			mPathInfo.width = calloutMgr.getWidth();
			mPathList = calloutMgr.getPath(index, true);
			mPathList.add(mPathInfo);
		}
	}

	private void touch_move(float x, float y) {
		if (calloutMgr.getDrawingMode() == MainConstant.DRAWMODE_CALLOUTDRAW) {
			x /= zoom;
			y /= zoom;
			float dx = Math.abs(x - mX);
			float dy = Math.abs(y - mY);
			if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
				mPathInfo.path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
				mX = x;
				mY = y;
			}
		}
	}

	private void touch_up() {
		if (calloutMgr.getDrawingMode() == MainConstant.DRAWMODE_CALLOUTDRAW) {
			mPathInfo.path.lineTo(mX, mY);
			mPathInfo.x = mX + 1;
			mPathInfo.y = mY + 1;
		} else if (calloutMgr.getDrawingMode() == MainConstant.DRAWMODE_CALLOUTERASE) {
			if (mPathList != null) {
				for (int i = 0; i < mPathList.size(); i++) {
					PathInfo pathInfo = mPathList.get(i);
					Path path = new Path(pathInfo.path);
					path.lineTo(pathInfo.x, pathInfo.y);
	
					RectF bounds = new RectF();
					path.computeBounds(bounds, false);
	
					Region region = new Region();
					region.setPath(path, new Region((int) bounds.left,
							(int) bounds.top, (int) bounds.right,
							(int) bounds.bottom));
	
					if (region.op(new Region((int) mX - offset, (int) mY - offset,
							(int) mX + offset, (int) mY + offset), Op.INTERSECT)) {
						mPathList.remove(i);
					}
				}
			}
		}
	}

	private void exportImage() {
		if (runnable != null) {
			removeCallbacks(runnable);
		}
		runnable = new Runnable() {
			public void run() {
				mListener.exportImage();
			}
		};
		postDelayed(runnable, 1000);
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (calloutMgr.getDrawingMode() == MainConstant.DRAWMODE_NORMAL) {
			return false;
		}
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touch_start(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			touch_move(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			touch_up();
			invalidate();
			exportImage();
			break;
		}
		return true;
	}
}
