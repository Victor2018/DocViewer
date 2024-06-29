/*
 * 文件名称:          aChartData.java
 *  
 * 编译器:            android2.2
 * 时间:              下午3:16:53
 */
package com.nvqquy98.lib.doc.office.common.shape;

import java.io.File;
import java.io.FileOutputStream;

import com.nvqquy98.lib.doc.office.common.PaintKit;
import com.nvqquy98.lib.doc.office.common.picture.Picture;
import com.nvqquy98.lib.doc.office.system.IControl;
import com.nvqquy98.lib.doc.office.thirdpart.achartengine.chart.AbstractChart;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-1-19
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class AChart  extends AbstractShape
{
    public AChart()
    {
        super();
    }
    
    /**
     * 
     *(non-Javadoc)
     * @see com.nvqquy98.lib.doc.office.common.shape.AbstractShape#getType()
     *
     */
    public short getType()
    {
        return SHAPE_CHART;
    }
    
    /**
     * 
     * @param chart
     */
    public void setAChart(AbstractChart chart)
    {
        this.chart = chart;
    }
    
    /**
     * 
     * @return
     */
    public AbstractChart getAChart()
    {
        return chart;
    }
    
    private void saveChartToPicture(IControl control)
    {
        
    	Bitmap bmp = null;
        try
        {
        	int width = (int)(rect.width * getAChart().getZoomRate());
        	int height = (int)(rect.height * getAChart().getZoomRate());
        	bmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            Canvas canvas = new Canvas(bmp);
            
            //canvas.drawBitmap(this.bmp, matrix, paint);
            chart.draw(canvas, control, 0, 0, width, height, PaintKit.instance().getPaint());

            canvas.save();//Canvas.ALL_SAVE_FLAG

            canvas.restore();
            Picture pic = new Picture(); 
            
            String name = String.valueOf(System.currentTimeMillis()) +  ".tmp";
            File file = new File(control.getSysKit().getPictureManage().getPicTempPath() + File.separator + name);
            file.createNewFile();
            
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            bmp.recycle();
            out.close();
                
            pic.setTempFilePath(file.getAbsolutePath());
            picIndex = control.getSysKit().getPictureManage().addPicture(pic); 
        }
        catch(Exception e)
        {
        	if(bmp != null)
        	{
        		bmp.recycle();
        	}
            control.getSysKit().getErrorKit().writerLog(e);
        }
    }
    
    /**
     * 
     * @return
     */
    public int getDrawingPicture(IControl control)
    {
        if(picIndex == -1)
        {
            saveChartToPicture(control);
        }
        
        return picIndex;
    }
    
    public void dispose()
    {
        super.dispose();
        chart = null;
    }
    
//    private Bitmap bmp;
    private int picIndex = -1;
    
    private AbstractChart chart;
}
