/*
 * 文件名称:          PictureCroppedInfor.java
 *  
 * 编译器:            android2.2
 * 时间:              下午5:36:40
 */
package com.nvqquy98.lib.doc.office.common.pictureefftect;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2013-1-15
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
/**
 * picture cropped information
 * @author jqin
 *
 */
public class PictureCroppedInfo
{

    public PictureCroppedInfo(float leftOff, float topOff, float rightOff, float bottomOff)
    {
        this.leftOff = leftOff;
        this.topOff = topOff;
        this.rightOff = rightOff;
        this.bottomOff = bottomOff;
    }        
    
    public float getLeftOff()
    {
        return leftOff;
    }
    public void setLeftOff(float leftOff)
    {
        this.leftOff = leftOff;
    }
    public float getTopOff()
    {
        return topOff;
    }
    public void setTopOff(float topOff)
    {
        this.topOff = topOff;
    }
    public float getRightOff()
    {
        return rightOff;
    }
    public void setRightOff(float rightOff)
    {
        this.rightOff = rightOff;
    }
    public float getBottomOff()
    {
        return bottomOff;
    }
    public void setBottomOff(float bottomOff)
    {
        this.bottomOff = bottomOff;
    }


    private float leftOff;
    private float topOff;
    private float rightOff;
    private float bottomOff;
}
