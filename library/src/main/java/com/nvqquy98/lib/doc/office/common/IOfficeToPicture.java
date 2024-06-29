/*
 * 文件名称:          OfficeToPicture.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:15:37
 */
package com.nvqquy98.lib.doc.office.common;

import android.graphics.Bitmap;

/**
 * Office 文档转换成图片的接口
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-5-14
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public interface IOfficeToPicture
{
    // 实现IOfficeToPicture接口的类路径
    public static final String INSTANCE_CLASS_PATH = "com.nvqquy98.lib.doc.office.officereader.OfficeToPicture";
    // 视图发生变化时生成picture，例如滚动、fling进行中
    public static final byte VIEW_CHANGING = 0;
    // 视图发生变化结束后，例如滚动、fling、横竖屏切换结束后
    public static final byte VIEW_CHANGE_END = VIEW_CHANGING + 1; 
    

    /**
     * set mode type
     * @param modeType
     */
    public void setModeType(byte modeType);
    
    /**
     * 生成picture模式
     * 
     * @ return  = 0，视图发生变化时生成picture，例如滚动、fling进行中，
     *           = 1，视图发生变化结束后，例如滚动、fling、横竖屏切换结束后。
     */
    public byte getModeType();

    /**
     * 获得converter to picture的Bitmap，如果返回空，则不生成picture
     * 
     * @param visibleWidth  engine组件的可视图宽度
     * @param visibleHeight engine组件的可视图高度
     * @return Bitmap    Bitmap实例
     */
    public Bitmap getBitmap(int visibleWidth, int visibleHeight);

    /**
     * picture 绘制完成，回调方法
     * 
     * @param bitmap  绘制好的图片
     */
    public void callBack(Bitmap bitmap);

    /**
     * 是否以zoom方式生成picture，此zoom是指office engine的size 和 bitmap size 之间的zoom。 
     * 例如 engine的size 1280 * 768，而bitmap size要求是845 * 480，这样的情况是否需要zoom。
     * 
     * @return   true    do zoom
     *           false   don’t zoom
     */
    public boolean isZoom();

    /**
     * 
     */
    public void dispose();
}
