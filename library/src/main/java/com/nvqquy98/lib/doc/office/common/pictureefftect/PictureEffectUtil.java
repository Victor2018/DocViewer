/*
 * 文件名称:          PictureEffectMgr.java
 *  
 * 编译器:            android2.2
 * 时间:              下午3:16:27
 */
package com.nvqquy98.lib.doc.office.common.pictureefftect;

/**
 * TODO: ColorMatrix
 * ColorMatrixFilter default value
 * 1,0,0,0,0
 * 0,1,0,0,0
 * 0,0,1,0,0
 * 0,0,0,1,0
 * 
 * brightness
 * brightness([-255,255])
 * 1,0,0,0,N
 * 0,1,0,0,N
 * 0,0,1,0,N
 * 0,0,0,1,0
 * 
 * Color reverse
 * -1,0,0,0,255
 * 0,-1,0,0,255
 * 0,0,-1,0,255
 * 0,0,0,1,0
 * 
 * gray scale
 * 0.3086, 0.6094, 0.0820, 0, 0
 * 0.3086, 0.6094, 0.0820, 0, 0
 * 0.3086, 0.6094, 0.0820, 0, 0
 * 0    , 0    , 0    , 1, 0
 * 
 * saturation([0,>=2])
 * 0.3086*(1-N) + N, 0.6094*(1-N)    , 0.0820*(1-N)    , 0, 0,
 * 0.3086*(1-N)   ,  0.6094*(1-N) + N, 0.0820*(1-N)    , 0, 0,
 * 0.3086*(1-N)   ,  0.6094*(1-N)    , 0.0820*(1-N) + N, 0, 0,
 * 0              , 0                , 0               , 1, 0

 * contrast([0,10])
 * N,0,0,0,128*(1-N)
 * 0,N,0,0,128*(1-N)
 * 0,0,N,0,128*(1-N)
 * 0,0,0,1,0
 * 
 * Black&White
 * Threshold([0,256])
 * 0.3086*256,0.6094*256,0.0820*256,0,-256*N
 * 0.3086*256,0.6094*256,0.0820*256,0,-256*N
 * 0.3086*256,0.6094*256,0.0820*256,0,-256*N
 * 0, 0, 0, 1, 0
 * 
 * Color rotation, such as:
 * 0,1,0,0,0
 * 0,0,1,0,0
 * 1,0,0,0,0
 * 0,0,0,1,0
 * //---------------
 * 0,0,1,0,0
 * 1,0,0,0,0
 * 0,1,0,0,0
 * 0,0,0,1,0
 * Only display a channel, such as:
 * 1,0,0,0,0
 * 0,0,0,0,0
 * 0,0,0,0,0
 * 0,0,0,1,0
 * just display Red channel
 * 
 * 
 * 
 * http://blog.163.com/mdzhg@126/blog/static/1633215682010423113048711/
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2013-1-14
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class PictureEffectUtil
{
    /**
     * no effect applied when brightness is 0
     * @param brightness range:[-255,255]
     * @return
     */
    public static float[] getBrightnessArray(int brightness)
    {
        return new float[] 
            { 1, 0, 0, 0, brightness, 
              0, 1, 0, 0, brightness,
              0, 0, 1, 0, brightness,
              0, 0, 0, 1, 0 };
    }
    
    public static float[] getReverseColorArray()
    {
        return new float[] 
            { -1, 0, 0, 0, 255, 
              0, -1, 0, 0, 255,
              0, 0, -1, 0, 255,
              0, 0, 0, 1, 0 };
    }
    
    /**
     * gray scale
     * @param brightness
     * @return
     */
    public static float[] getGrayScaleArray()
    {
        return new float[]
            { 0.3086f, 0.6094f, 0.0820f, 0, 0,
              0.3086f, 0.6094f, 0.0820f, 0, 0,
              0.3086f, 0.6094f, 0.0820f, 0, 0,
              0     ,   0     ,     0  , 1, 0};
    }
    
    /**
     * 
     * @param sat ([0,>=2])
     * @return
     */
    public static float[] getSaturationArray(float sat)
    {
        return new float[]
            {
                0.3086f * (1 - sat) + sat, 0.6094f *(1 - sat)    , 0.0820f * (1 - sat)    , 0, 0,
                0.3086f * (1 - sat)   ,  0.6094f *(1 - sat) + sat, 0.0820f * (1 - sat)    , 0, 0,
                0.3086f * (1 - sat)   ,  0.6094f *(1 - sat)    , 0.0820f * (1 - sat) + sat, 0, 0,
                0              , 0                , 0               , 1, 0
            };
    }
    
    /**
     * 
     * @param contrast ([0,10]) no effect applied when contrast is 1
     * @return 
     */
    public static float[] getContrastArray(float contrast)
    {
        return new float[]
            { 
                contrast, 0, 0, 0, 128*(1-contrast),
                0f, contrast, 0, 0, 128*(1-contrast),
                0f, 0, contrast, 0, 128*(1-contrast),
                0f, 0, 0, 1, 0
            };
    }
    
    public static float[] getBrightAndContrastArray(float bright, float contrast)
    {
        return new float[]
            { 
                contrast, 0, 0, 0, bright ,
                0f, contrast, 0, 0, bright,
                0f, 0, contrast, 0, bright ,
                0f, 0, 0, 1, 0
            };
    }
    /**
     * 
     * @param threshode [0,255]
     * @return
     */
    public static float[] getBlackWhiteArray(float threshode)
    {
        return new float[]
            { 
            0.3086f * 256, 0.6094f * 256, 0.0820f * 256, 0,-256 * threshode,
            0.3086f * 256, 0.6094f * 256, 0.0820f * 256, 0,-256 * threshode,
            0.3086f * 256, 0.6094f * 256, 0.0820f * 256, 0,-256 * threshode,
            0, 0, 0, 1, 0
            };
    }
}
