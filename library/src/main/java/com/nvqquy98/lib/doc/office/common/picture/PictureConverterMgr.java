/*
 * 文件名称:          PictureConverterThread.java
 *  
 * 编译器:            android2.2
 * 时间:              下午3:30:35
 */
package com.nvqquy98.lib.doc.office.common.picture;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nvqquy98.lib.doc.office.constant.EventConstant;
import com.nvqquy98.lib.doc.office.fc.pdf.PDFLib;
import com.nvqquy98.lib.doc.office.system.IControl;
import com.nvqquy98.lib.doc.office.thirdpart.emf.util.EMFUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2013-4-25
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class PictureConverterMgr
{

    public PictureConverterMgr(IControl control)
    {
        this.control = control;
        convertingThread = new ArrayList<Thread>();
        convertingPictPathMap = new HashMap<String, Thread>();
        vectorgraphViews = new HashMap<String, List<Integer>>();
        viewVectorgraphs = new HashMap<Integer, List<String>>();
    }
    
    public void setControl(IControl control)
    {
        this.control = control;
    }    
    
    public synchronized void addConvertPicture(int viewIndex, byte type , String srcPath, String dstPath, int width, int height, boolean singleThread)
    {
        control.actionEvent(EventConstant.SYS_SET_PROGRESS_BAR_ID, true);
        
        if(singleThread)
        {
        	convertWMF_EMF(type, srcPath, dstPath, width, height, true);
        }
        else
        {
    		VectorgraphConverterThread thread = new VectorgraphConverterThread(this, type, srcPath, dstPath, width, height);        
    		
    		convertingThread.add(thread);
    		convertingPictPathMap.put(dstPath, thread);
            
            List<Integer> listIndex = new ArrayList<Integer>();
            listIndex.add(viewIndex);
            vectorgraphViews.put(dstPath, listIndex);
            
            if(viewVectorgraphs.get(viewIndex) == null)
            {
            	List<String> listPath = new ArrayList<String>();
            	listPath.add(dstPath);
                viewVectorgraphs.put(viewIndex, listPath);
            }
            else
            {
            	viewVectorgraphs.get(viewIndex).add(dstPath);
            }
            
            if(convertingThread.size() == 1)
            {
            	//is not converting now, so start the thread
            	convertingThread.get(convertingThread.size() - 1).start();
            }
        }
    }
    
    public void convertWMF_EMF(byte type, String sourPath, String destPath, int picWidth, int picHeight, boolean thumbnail)
    {
    	 try
         {
    		 Bitmap sBitmap = null;
             if(type == Picture.WMF)
             {
                 int ret = PDFLib.getPDFLib().wmf2Jpg(sourPath, destPath, picWidth, picHeight);    		     
                 sBitmap = BitmapFactory.decodeFile(destPath);                 
             }
             else if(type == Picture.EMF)
             {
            	 sBitmap = EMFUtil.convert(sourPath, destPath, picWidth, picHeight);
             }
             
             if(control != null && (convertingPictPathMap.get(destPath) == null || control.getView() == null))
             {
            	 //has disposed
            	 return;
             }
             
             if(sBitmap != null)
             {
            	 control.getSysKit().getPictureManage().addBitmap(destPath, sBitmap);
                 remove(destPath);
                 
                 if(!thumbnail)
                 {
                	 control.actionEvent(EventConstant.TEST_REPAINT_ID, null);
                 }
             }
             else
             {
            	 remove(destPath);
             }
         }
    	 catch(OutOfMemoryError e)
    	 {
    		 if(control.getSysKit().getPictureManage().hasBitmap())
             {
                 control.getSysKit().getPictureManage().clearBitmap();
                 convertWMF_EMF(type, sourPath, destPath, picWidth, picHeight, thumbnail);
             }
    		 else
    		 {
    			 control.getSysKit().getErrorKit().writerLog(e);
    			 remove(destPath);
    		 }
    	 }
         catch(Exception e)
         {
        	 if(control != null && (convertingPictPathMap.get(destPath) == null || control.getView() == null))
             {
            	 //has disposed
            	 return;
             }
        	 
        	 control.getSysKit().getErrorKit().writerLog(e);
             remove(destPath);
         }
    }
    
    
    public synchronized void addConvertPicture(int viewIndex, String srcPath, String dstPath, String picType, boolean singleThread)
    {
        control.actionEvent(EventConstant.SYS_SET_PROGRESS_BAR_ID, true);
        
        if(singleThread)
        {
        	convertPNG(srcPath, dstPath, picType, true);
        }
        else
        {
        	PictureConverterThread thread = new PictureConverterThread(this, srcPath, dstPath, picType);        
    		
    		convertingThread.add(thread);
    		convertingPictPathMap.put(dstPath, thread);
            
            List<Integer> listIndex = new ArrayList<Integer>();
            listIndex.add(viewIndex);
            vectorgraphViews.put(dstPath, listIndex);
            
            if(viewVectorgraphs.get(viewIndex) == null)
            {
            	List<String> listPath = new ArrayList<String>();
            	listPath.add(dstPath);
                viewVectorgraphs.put(viewIndex, listPath);
            }
            else
            {
            	viewVectorgraphs.get(viewIndex).add(dstPath);
            }
            
            if(convertingThread.size() == 1)
            {
            	//is not converting now, so start the thread
            	convertingThread.get(convertingThread.size() - 1).start();
            }
        }
    }
    
    public void convertPNG(String sourPath, String destPath, String picType, boolean thumbnail)
    {
    	 try
         {
    		 boolean ret = PDFLib.getPDFLib().convertToPNG(sourPath, destPath, picType);
             
             if(control != null && (convertingPictPathMap.get(destPath) == null || control.getView() == null))
             {
            	 //has disposed
            	 return;
             }
             
             if(ret)
             {
            	 InputStream in = new FileInputStream(destPath);
            	 Bitmap sBitmap = BitmapFactory.decodeStream(in, null, null);
            	 if(sBitmap != null)
            	 {
            		 control.getSysKit().getPictureManage().addBitmap(destPath, sBitmap);
                     remove(destPath);
                     
                     if(!thumbnail)
                     {
                    	 control.actionEvent(EventConstant.TEST_REPAINT_ID, null);
                     }
            	 }
            	 else
            	 {
            		 remove(destPath);
            	 }
             }
             else
             {
            	 remove(destPath);
             }
         }
    	 catch(OutOfMemoryError e)
    	 {
    		 if(control.getSysKit().getPictureManage().hasBitmap())
             {
                 control.getSysKit().getPictureManage().clearBitmap();
                 convertPNG(sourPath, destPath, picType, thumbnail);
             }
    		 else
    		 {
    			 control.getSysKit().getErrorKit().writerLog(e);
    			 remove(destPath);
    		 }
    	 }
         catch(Exception e)
         {
        	 if(control != null && (convertingPictPathMap.get(destPath) == null || control.getView() == null))
             {
            	 //has disposed
            	 return;
             }
        	 
        	 control.getSysKit().getErrorKit().writerLog(e);
             remove(destPath);
         }
    }
    
    /**
     * 
     * @param path
     */
    public void remove(String path)
    {
    	synchronized(control)
    	{
    		if(convertingPictPathMap != null)
            {
    			Thread thread = convertingPictPathMap.remove(path);
    			convertingThread.remove(thread);    			
    			
            	List<Integer> updateViewList = null;
            	List<Integer> viewList = vectorgraphViews.remove(path);
            	for(int i = 0; i < viewList.size(); i++)
            	{
            		int viewIndex = viewList.get(i);
            		List<String> vectorgraphs = viewVectorgraphs.get(viewIndex);
            		vectorgraphs.remove(path);
            		if(vectorgraphs.size() == 0)
            		{
            			//all vector graphs contained in this view have been converted
            			//so notify to update this view
            			viewVectorgraphs.remove(viewIndex);
            			
            			if(updateViewList == null)
            			{
            				updateViewList = new ArrayList<Integer>();
            			}
            			
            			updateViewList.add(viewIndex);
            		}
            	}
            	
            	if(convertingThread.size() > 0)
    			{
            		//check current view vector graphs
            		List<String> vectorgraphs = viewVectorgraphs.get(control.getCurrentViewIndex());
            		if(vectorgraphs != null && vectorgraphs.size() > 0)
            		{
            			//start current view vector graph converting thread
            			convertingPictPathMap.get(vectorgraphs.get(0)).start();
            		}
            		else
            		{
            			//start the last vector graph converting thread
        				convertingThread.get(convertingThread.size() - 1).start();
            		}    				
    			}
            	
            	if(updateViewList != null && updateViewList.size() > 0)
            	{
            		if(updateViewList.contains(control.getCurrentViewIndex()))
            		{
            			control.actionEvent(EventConstant.APP_GENERATED_PICTURE_ID, null);
            		}            		
            		
                    control.actionEvent(EventConstant.SYS_VECTORGRAPH_PROGRESS, updateViewList);
            	}
            	
                if(convertingPictPathMap.size() == 0)
                {
                    control.actionEvent(EventConstant.SYS_SET_PROGRESS_BAR_ID, false);                
                }
            }
    	}        
    }
    
    public boolean hasConvertingVectorgraph(int viewIndex)
    {
    	synchronized(control)
    	{
    		return viewVectorgraphs.containsKey(viewIndex);
    	}    	
    }
    
    /**
     * 
     * @param path
     * @return
     */
    public boolean isPictureConverting(String path)
    {
    	synchronized(control)
    	{
    		return vectorgraphViews.containsKey(path);
    	}
    }
    
    /**
     * call when vector graph has been converted, but with different view index
     * eg. different pages has same vector graph
     * @param path
     * @param viewIndex
     */
    public void appendViewIndex(String path, int viewIndex)
    {
    	synchronized(control)
    	{
    		if(isPictureConverting(path))
        	{
        		vectorgraphViews.get(path).add(viewIndex);
        		
        		if(viewVectorgraphs.get(viewIndex) == null)
                {
                	List<String> listPath = new ArrayList<String>();
                	listPath.add(path);
                    viewVectorgraphs.put(viewIndex, listPath);
                }
                else
                {
                	viewVectorgraphs.get(viewIndex).add(path);
                }
        	}
    	}    	
    }
    
    public synchronized void dispose()
    {
    	if(convertingPictPathMap != null)
        {
            Iterator<Thread> iter = convertingPictPathMap.values().iterator();
            while(iter.hasNext())
            {
                try
                {
                    iter.next().interrupt();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                
            }
            convertingPictPathMap.clear();
            
            vectorgraphViews.clear();
            viewVectorgraphs.clear();
        }
    }
    private IControl control;
    //last-in，first-out
    private List<Thread> convertingThread;
    //
    private Map<String, Thread> convertingPictPathMap;
    //vector graph path and view indexs which contains this vector graph
    private Map<String, List<Integer>> vectorgraphViews;
    //view index and vector graphs which is contained in this view
    private Map<Integer, List<String>> viewVectorgraphs;
}
