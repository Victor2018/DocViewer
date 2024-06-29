/*
 * 文件名称:          PictureKit.java
 *
 * 编译器:            android2.2
 * 时间:              下午4:12:38
 */

package com.nvqquy98.lib.doc.office.common.picture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.nvqquy98.lib.doc.office.common.pictureefftect.PictureCroppedInfo;
import com.nvqquy98.lib.doc.office.common.pictureefftect.PictureEffectInfo;
import com.nvqquy98.lib.doc.office.common.pictureefftect.PictureEffectUtil;
import com.nvqquy98.lib.doc.office.constant.EventConstant;
import com.nvqquy98.lib.doc.office.pg.animate.IAnimation;
import com.nvqquy98.lib.doc.office.pg.animate.ShapeAnimation;
import com.nvqquy98.lib.doc.office.system.IControl;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 图片处理工具类
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-11-16
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:
 * <p>
 * <p>
 */
public class PictureKit {
    private static final String FAIL = "Fail";
    //
    private static final PictureKit kit = new PictureKit();

    private static int VectorMaxZOOM = 3;

    private static int VectorMaxSize = 1024 * 1024;

    /**
     * @return
     */
    public static PictureKit instance() {
        return kit;
    }

    /**
     *
     */
    private PictureKit() {
        paint.setAntiAlias(true);
        ;
    }

    /**
     * @param canvas     画布
     * @param pic        绘制图片对象
     * @param x          x值
     * @param y          y值
     * @param zoom       是否缩放
     * @param destWidth  如果缩放，指定缩放后的宽度
     * @param destHeight 如果缩放，指定缩放后的高度
     * @throws OutOfMemoryError
     */
    public synchronized void drawPicture(Canvas canvas, IControl control, int viewIndex, Picture pic, float x, float y,
                                         float zoom, float destWidth, float destHeight, PictureEffectInfo effectInfor) throws OutOfMemoryError {
        drawPicture(canvas, control, viewIndex, pic, x, y, zoom, destWidth, destHeight, effectInfor, null);
    }

    private void applyEffect(Paint paint, PictureEffectInfo effectInfor) {
        if (effectInfor != null) {
            ColorMatrix cMatrix = new ColorMatrix();
            //black&white
            if (effectInfor.getBlackWhiteThreshold() != null) {
                cMatrix.set(PictureEffectUtil.getBlackWhiteArray(effectInfor.getBlackWhiteThreshold()));
            } else if (effectInfor.isGrayScale() != null && effectInfor.isGrayScale()) {
                cMatrix.set(PictureEffectUtil.getGrayScaleArray());
            }
            //brightness and contrast
            Float brightness = effectInfor.getBrightness();
            Float contrast = effectInfor.getContrast();
            if (brightness != null && contrast != null) {
                ColorMatrix cm = new ColorMatrix();
                cm.set(PictureEffectUtil.getBrightAndContrastArray(brightness.intValue(), contrast));
                cMatrix.preConcat(cm);
            } else if (brightness != null) {
                ColorMatrix cm = new ColorMatrix();
                cm.set(PictureEffectUtil.getBrightnessArray(brightness.intValue()));
                cMatrix.preConcat(cm);
            } else if (contrast != null) {
                ColorMatrix cm = new ColorMatrix();
                cm.set(PictureEffectUtil.getContrastArray(contrast));
                cMatrix.preConcat(cm);
            }
            paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));
        }
    }

    /**
     * @param canvas     画布
     * @param pic        绘制图片对象
     * @param x          x值
     * @param y          y值
     * @param zoom       是否缩放
     * @param destWidth  如果缩放，指定缩放后的宽度
     * @param destHeight 如果缩放，指定缩放后的高度
     * @throws OutOfMemoryError
     */
    public synchronized void drawPicture(Canvas canvas, IControl control, int viewIndex, Picture pic, float x, float y,
                                         float zoom, float destWidth, float destHeight, PictureEffectInfo effectInfor, IAnimation animation) throws OutOfMemoryError {
        if (pic != null && pic.getTempFilePath() != null) {
            if (animation != null && animation.getCurrentAnimationInfor().getAlpha() == 0) {
                return;
            }
            String ret = drawPicture(canvas, control, viewIndex, pic.getTempFilePath(), pic.getPictureType(), null,
                    x, y, zoom, destWidth, destHeight, effectInfor, animation);
            if (ret != null) {
                if (ret.equalsIgnoreCase(FAIL)) {
                    //picture is too large, do not decode it any more
                    pic.setTempFilePath(null);
                } else {
                    //wmf image to jpg
                    pic.setPictureType(Picture.PNG);
                    pic.setTempFilePath(ret);
                }
            }
        }
    }

    private boolean drawCropedPicture(Canvas canvas, float x, float y, float destWidth, float destHeight,
                                      Bitmap sBitmap, PictureCroppedInfo croppedInfor) {
        if (croppedInfor != null) {
            Rect destCrop = null;
            Rect srcCrop = null;
            int srcWidth = sBitmap.getWidth();
            int srcHeight = sBitmap.getHeight();
            int left = (int) (srcWidth * croppedInfor.getLeftOff());
            int top = (int) (srcHeight * croppedInfor.getTopOff());
            int right = (int) (srcWidth * (1 - croppedInfor.getRightOff()));
            int bottom = (int) (srcHeight * (1 - croppedInfor.getBottomOff()));
            destCrop = new Rect(left, top, right, bottom);
            left = left >= 0 ? left : 0;
            top = top >= 0 ? top : 0;
            right = right >= srcWidth ? srcWidth : right;
            bottom = bottom >= srcHeight ? srcHeight : bottom;
            srcCrop = new Rect(left, top, right, bottom);
            canvas.save();
            Matrix matrix = new Matrix();
            float zoomX = ((float) destWidth) / (destCrop.width());
            float zoomY = ((float) destHeight) / (destCrop.height());
            matrix.postScale(zoomX, zoomY);
            float offX = destCrop.left * zoomX;
            float offY = destCrop.top * zoomY;
            matrix.postTranslate(x - offX, y - offY);
            offX = offX >= 0 ? 0 : offX;
            offY = offY >= 0 ? 0 : offY;
            canvas.clipRect(x - offX,
                    y - offY,
                    x - offX + srcCrop.width() * zoomX,
                    y - offY + srcCrop.height() * zoomY);
            canvas.drawBitmap(sBitmap, matrix, paint);
            canvas.restore();
        }
        return true;
    }

    /**
     * @param canvas     画布
     * @param data       byte数组的图片数据
     * @param offset     数组的开始位置
     * @param len        长度
     * @param x          x值
     * @param y          y值
     * @param zoom       是否缩放
     * @param destWidth  如果缩放，指定缩放后的宽度
     * @param destHeight 如果缩放，指定缩放后的高度
     * @return F  单个文件都会OOM, decode失败;  Null not drawing picture ; other content: wmf2Jpg file path
     */
    private String drawPicture(Canvas canvas, IControl control, int viewIndex, String path, byte imageType, Options options, float x, float y, float zoom, float destWidth,
                               float destHeight, PictureEffectInfo effectInfor, IAnimation animation) {
        try {
            Bitmap sBitmap = control.getSysKit().getPictureManage().getBitmap(path);
            if (sBitmap == null) {
                if (!isDrawPictrue()) {
                    return null;
                }
                if (control.getSysKit().getPictureManage().isConverting(path)) {
                    control.getSysKit().getPictureManage().appendViewIndex(path, viewIndex);
                    return null;
                }
                if (imageType == Picture.WMF || imageType == Picture.EMF) {
                    if (control.isSlideShow()) {
                        control.getSysKit().getAnimationManager().killAnimationTimer();
                    }
                    int w = (int) (destWidth / zoom);
                    int h = (int) (destHeight / zoom);
                    if (w * h < VectorMaxSize) {
                        double z = Math.sqrt(VectorMaxSize / (w * h));
                        if (z > VectorMaxZOOM) {
                            z = VectorMaxZOOM;
                        }
                        w = (int) Math.round(w * z);
                        h = (int) Math.round(h * z);
                    }
                    String dst = control.getSysKit().getPictureManage().convertVectorgraphToPng(viewIndex, imageType, path,
                            w, h, control.isSlideShow());
                    if (control.isSlideShow()) {
                        control.getSysKit().getAnimationManager().restartAnimationTimer();
                        control.actionEvent(EventConstant.TEST_REPAINT_ID, null);
                    }
                    return dst;
                } else {
                    try {
                        InputStream in = new FileInputStream(path);
                        sBitmap = BitmapFactory.decodeStream(in, null, options);
                        if (sBitmap == null) {
                            //load fail, so call library to convert it to normal png image
                            if (control.isSlideShow()) {
                                control.getSysKit().getAnimationManager().killAnimationTimer();
                            }
                            String dst = null;
                            if (imageType == Picture.JPEG) {
                                // android only supports RGB color space, but some JPEG picture is
                                // CMYK color space, it returns null when call BitmapFactory.decodeStream,
                                // so call lib to load picture
                                dst = control.getSysKit().getPictureManage().convertToPng(viewIndex, path, Picture.JPEG_TYPE, control.isSlideShow());
                            } else if (imageType == Picture.PNG) {
                                dst = control.getSysKit().getPictureManage().convertToPng(viewIndex, path, Picture.PNG_TYPE, control.isSlideShow());
                            }
                            if (control.isSlideShow()) {
                                control.getSysKit().getAnimationManager().restartAnimationTimer();
                                control.actionEvent(EventConstant.TEST_REPAINT_ID, null);
                            }
                            return dst;
                        }
                    } catch (Exception e) {
                        return FAIL;
                    }
                }
                if (sBitmap == null) {
                    return FAIL;
                }
                control.getSysKit().getPictureManage().addBitmap(path, sBitmap);
            }
            if (animation != null) {
                ShapeAnimation shapeAnim = animation.getShapeAnimation();
                int paraBegin = shapeAnim.getParagraphBegin();
                int paraEnd = shapeAnim.getParagraphEnd();
                if (paraBegin == ShapeAnimation.Para_All && paraEnd == ShapeAnimation.Para_All
                        || (paraBegin == ShapeAnimation.Para_BG && paraEnd == ShapeAnimation.Para_BG)) {
                    int a = animation.getCurrentAnimationInfor().getAlpha();
                    paint.setAlpha(a);
                    float rate = a / 255f * 0.5f;
                    float centerX = x + destWidth / 2;
                    float centerY = y + destHeight / 2;
                    x = centerX - destWidth * rate;
                    y = centerY - destHeight * rate;
                    destWidth *= rate * 2;
                    destHeight *= rate * 2;
                }
            }
            //picture effect
            //transparent
            boolean isTransparentBMP = false;
            if (effectInfor != null && effectInfor.getTransparentColor() != null) {
                Bitmap bmp = createTransparentBitmapFromBitmap(sBitmap, effectInfor.getTransparentColor());
                if (bmp != null) {
                    sBitmap = bmp;
                    isTransparentBMP = true;
                }
            }
            //alpha
            if (effectInfor != null && effectInfor.getAlpha() != null) {
                paint.setAlpha(effectInfor.getAlpha());
            }
            //other effect
            applyEffect(paint, effectInfor);
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            if (effectInfor == null || effectInfor.getPictureCroppedInfor() == null) {
                Matrix matrix = new Matrix();
                matrix.postScale(((float) destWidth) / sBitmap.getWidth(),
                        ((float) destHeight) / sBitmap.getHeight());
                matrix.postTranslate(x, y);
                canvas.drawBitmap(sBitmap, matrix, paint);
            } else {
                drawCropedPicture(canvas, x, y, destWidth, destHeight, sBitmap, effectInfor.getPictureCroppedInfor());
            }
            paint.reset();
            if (isTransparentBMP) {
                sBitmap.recycle();
            }
            return null;
        } catch (OutOfMemoryError e) {
            if (control.getSysKit().getPictureManage().hasBitmap()) {
                control.getSysKit().getPictureManage().clearBitmap();
                return drawPicture(canvas, control, viewIndex, path, imageType, options, x, y, zoom, destWidth, destHeight, effectInfor, animation);
            } else {
                if (options == null) {
                    options = new Options();
                    options.inSampleSize = 2;
                } else {
                    options.inSampleSize *= 2;
                }
                return drawPicture(canvas, control, viewIndex, path, imageType, options, x, y, zoom, destWidth, destHeight, effectInfor, animation);
            }
        } catch (Exception e) {
            return FAIL;
        }
    }

    /**
     * create a transparent bitmap from an existing bitmap by replacing certain
     * color with transparent
     *
     * @param bitmap the original bitmap with a color you want to replace
     * @return a replaced color immutable bitmap
     */
    public Bitmap createTransparentBitmapFromBitmap(Bitmap bitmap, int replaceThisColor) {
        if (bitmap != null) {
            final int diff = 10;
            int picw = bitmap.getWidth();
            int pich = bitmap.getHeight();
            int[] pix = new int[picw * pich];
            bitmap.getPixels(pix, 0, picw, 0, 0, picw, pich);
            for (int y = 0; y < pich; y++) {
                // from left to right
                for (int x = 0; x < picw; x++) {
                    int index = y * picw + x;
                    int r = (pix[index] >> 16) & 0xff;
                    int g = (pix[index] >> 8) & 0xff;
                    int b = pix[index] & 0xff;
                    int tr = (replaceThisColor >> 16) & 0xff;
                    int tg = (replaceThisColor >> 8) & 0xff;
                    int tb = replaceThisColor & 0xff;
                    if (Math.abs(tr - r) <= diff
                            && Math.abs(tg - g) <= diff
                            && Math.abs(tb - b) <= diff) {
                        pix[index] = 0;
                    }
                }
            }
            return Bitmap.createBitmap(pix, picw, pich, Bitmap.Config.ARGB_4444);
        }
        return null;
    }

    /**
     * @param canvas
     * @param bitmap
     * @param x
     * @param y
     * @param zoom
     * @param destWidth
     * @param destHeight
     */
    public void drawPicture(Canvas canvas, IControl control, Bitmap bitmap, float x, float y, boolean zoom,
                            float destWidth, float destHeight) {
        try {
            if (bitmap == null) {
                return;
            }
            Matrix matrix = new Matrix();
            matrix.postScale(((float) destWidth) / bitmap.getWidth(),
                    ((float) destHeight) / bitmap.getHeight());
            matrix.postTranslate(x, y);
            canvas.drawBitmap(bitmap, matrix, paint);
        } catch (OutOfMemoryError e) {
            control.getSysKit().getErrorKit().writerLog(e);
        }
    }

    /**
     * @param pic
     * @return
     */
    public boolean isVectorPicture(Picture pic) {
        if (pic != null) {
            byte imageType = pic.getPictureType();
            if (imageType == Picture.WMF || imageType == Picture.EMF) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return Returns the isDrawPictrue.
     */
    public boolean isDrawPictrue() {
        return isDrawPictrue;
    }

    /**
     * @param isDrawPictrue The isDrawPictrue to set.
     */
    public void setDrawPictrue(boolean isDrawPictrue) {
        this.isDrawPictrue = isDrawPictrue;
    }

    //
    private Paint paint = new Paint();
    //
    private boolean isDrawPictrue = true;
    //
    //private String filePath;    
}
