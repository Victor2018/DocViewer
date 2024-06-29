/*
 * 文件名称:           PictureManage.java
 *  
 * 编译器:             android2.2
 * 时间:               上午10:40:46
 */
package com.nvqquy98.lib.doc.office.common.picture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.InflaterInputStream;

import com.nvqquy98.lib.doc.office.fc.hslf.usermodel.PictureData;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackagePart;
import com.nvqquy98.lib.doc.office.system.IControl;

import android.graphics.Bitmap;

public class PictureManage
{
    //
    private final int CACHE_SIZE = 8 * 1024 * 1024;
    
    //
    public PictureManage(IControl control)
    {
        this.control = control;
        pictures = new ArrayList<Picture>();
        picIndexs = new HashMap<String, Integer>();
        File file = control.getMainFrame().getTemporaryDirectory();
        if (file == null)
        {
            control.getSysKit().getErrorKit().writerLog(new Throwable("SD Card Error"));
        }
        else
        {
            picTempPath = file.getAbsolutePath() + File.separator + "tempPic";
            file = new File(picTempPath);
            if (!file.exists())
            {
                file.mkdir();
            }
            picTempPath = file.getAbsolutePath() + File.separator + System.currentTimeMillis();
            file = new File(picTempPath);
            if (!file.exists())
            {
                file.mkdir();
            }
        }
    }
    
    /**
     * add picture to pgMode
     * @param pgModel
     * @param picPart
     * @throws Exception
     */
    public int addPicture(PackagePart picPart) throws Exception
    {
        String key = picPart.getPartName().getName();
        Integer index = picIndexs.get(key);
        if (index == null)
        {
            Picture picture = new Picture();
            // picture data 
            picture.setTempFilePath(writeTempFile(picPart));
            picture.setPictureType(picPart.getPartName().getExtension());
            int size = pictures.size();
            pictures.add(picture);
            picIndexs.put(key, size);
            
            return size;
        }
        return index;
    }
    
    /**
     * add picture with PowerPoint
     * @param pgModel
     * @param pData
     * @return
     */
    public int addPicture(PictureData pData)
    {
        Integer index = picIndexs.get(pData.getTempFilePath());
        if (index == null)
        {
            Picture picture = new Picture();
            // 图片数据
            picture.setTempFilePath(pData.getTempFilePath());    
            // 图片类型
            picture.setPictureType((byte)pData.getType());
            
            int size = pictures.size();
            pictures.add(picture);      
            
            picIndexs.put(pData.getTempFilePath(), size);
            return size;
        }
        return index;
    }
    
    /**
     * add picture to Excel
     * @param pgModel
     * @param pData
     * @return
     */
    public int addPicture(Picture picture)
    {        
        // 图片数据
        if (picture.getTempFilePath() == null)
        {
            picture.setTempFilePath(writeTempFile(picture.getData()));
            picture.setData(null);
        }
        else
        {
            int index = getPictureIndex(picture.getTempFilePath());
            if (index >= 0)
            {
                return index;
            }
        }
        
        int size = pictures.size();
        pictures.add(picture);
        
        picIndexs.put(picture.getTempFilePath(), size);
        
        return size;
    }
    
    /**
     * 
     */
    public Picture getPicture(int index)
    {
        if (index < 0 || index >= pictures.size())
        {
            return null;
        }
        return pictures.get(index);
    }
    
    /**
     * 
     */
    public int getPictureIndex(String key)
    {   
        Integer a =  picIndexs.get(key);
        if (a == null)
        {
            return -1;
        }
        return a;
    }

    /**
     * 
     */
    public String writeTempFile(byte[] b)
    {
    	try
    	{
    		return writeTempFile(b, 0, b.length);
    	}
    	catch (Exception e)
        {
            control.getSysKit().getErrorKit().writerLog(e);
        }
    	return null;
    }
    
    public String writeTempFile(InflaterInputStream in)
    {
        String name = String.valueOf(System.currentTimeMillis()) +  ".tmp";
        File file = new File(picTempPath + File.separator + name);
        try
        {
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            
            byte[] buf = new byte[4096];
            int readBytes;
            while ((readBytes = in.read(buf)) > 0)
            {
                out.write(buf, 0, readBytes);
            }
        }
        catch (Exception e)
        {
            control.getSysKit().getErrorKit().writerLog(e);
        }
        return file.getAbsolutePath();
    }
    
    /**
     * 
     */
    public String writeTempFile(byte[] b, int offset, int len)
    {
        String name = String.valueOf(System.currentTimeMillis()) +  ".tmp";
        File file = new File(picTempPath + File.separator + name);
        try
        {
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            out.write(b, offset, len);
            out.close();
        }
        catch (Exception e)
        {
            control.getSysKit().getErrorKit().writerLog(e);
        }
        return file.getAbsolutePath();
    }
    
    /**
     * get picture data
     */
    private String writeTempFile(PackagePart picPart)
    {
        try
        {
            if (picPart != null)
            {
                String name = String.valueOf(System.currentTimeMillis()) +  ".tmp";
                File file = new File(picTempPath + File.separator + name);
                file.createNewFile();
                FileOutputStream out = new FileOutputStream(file);
                // data
                InputStream in = picPart.getInputStream();
                int len;
                byte[] b = new byte[8192];
                while((len = in.read(b, 0, b.length)) != -1)
                {
                    out.write(b, 0, len);
                }
                in.close();
                out.close();
                
                return file.getAbsolutePath();
            }
        }
        catch (Exception e)
        {
            control.getSysKit().getErrorKit().writerLog(e);
        }
        return null;
    }
    
    /**
     * 
     */
    public synchronized Bitmap getBitmap(String key)
    {
        return bitmaps.get(key);
    }
    
    /**
     * 
     */
    public synchronized void addBitmap(String key, Bitmap bitmap)
    {
        if (bitmapTotalCacheSize > CACHE_SIZE)
        {
            String str = bitmaps.entrySet().iterator().next().getKey();
            Bitmap b = bitmaps.get(str);
            bitmapTotalCacheSize -= b.getWidth() * b.getHeight();
            bitmaps.remove(str).recycle();
        }
        bitmapTotalCacheSize += bitmap.getHeight() * bitmap.getHeight();
        bitmaps.put(key, bitmap);
    }
    
    private void checkPictureConverterMgr()
    {
    	if(picConverterMgr ==  null)
        {
            picConverterMgr = new PictureConverterMgr(control);
        }
        else
        {
            picConverterMgr.setControl(control);
        }
    }
    
    /**
     * 
     * @param path
     * @return
     */
    public boolean isConverting(String path)
    {
    	checkPictureConverterMgr();
        return picConverterMgr.isPictureConverting(path);
    }
    
    /**
     * 
     * @param viewIndex
     * @return
     */
    public boolean hasConvertingVectorgraph(int viewIndex)
    {
    	checkPictureConverterMgr();
    	return picConverterMgr.hasConvertingVectorgraph(viewIndex); 	
    }
    
    
    /**
     * call when vector graph has been converted, but with different view index
     * eg. different pages has same vector graph
     * @param path
     * @param viewIndex
     */
    public void appendViewIndex(String path, int viewIndex)
    {
    	checkPictureConverterMgr();
    	if(picConverterMgr != null)
    	{
    		picConverterMgr.appendViewIndex(path, viewIndex);
    	}
    }
    
    /**
     * convert vector graph to normal png
     * @param imageType
     * @param path
     * @param width
     * @param height
     * @return
     */
    public String convertVectorgraphToPng(int viewIndex, byte imageType, String path, int width, int height, boolean singleThread)
    {
        String convertToPath = path.substring(0, path.length() - 4) + "converted.tmp";

        checkPictureConverterMgr();
        
        picConverterMgr.addConvertPicture(viewIndex, imageType, path, convertToPath, width, height, singleThread);
        
        return convertToPath;
    }
    
    /**
     * convert some image which load fail in android OS to normal png
     * @param viewIndex
     * @param path
     * @param picType
     * @param singleThread
     * @return
     */
    public String convertToPng(int viewIndex, String path, String picType, boolean singleThread)
    {
        String convertToPath = path.substring(0, path.length() - 4) + "converted.tmp";

        checkPictureConverterMgr();
        
        picConverterMgr.addConvertPicture(viewIndex, path, convertToPath, picType, singleThread);
        
        return convertToPath;
    }
    
    /**
     * 
     */
    public synchronized void clearBitmap()
    {
        for (Bitmap bitmap : bitmaps.values())
        {
            bitmap.recycle();
        }
        bitmaps.clear();
        bitmapTotalCacheSize = 0;
    }
    
    /**
     * has stored bitmap
     * @return
     */
    public boolean hasBitmap()
    {
        return bitmaps.size() > 0;
    }
    
    
    /**
     * 
     */
    public String getPicTempPath()
    {
        return this.picTempPath;
    }
    
    /**
     * 
     */
    private void deleteTempFile(File folder)
    {
        if (!folder.exists())
        {
            return;
        }
        File[] files = folder.listFiles();
        if (files != null)
        {
            for (File file : files)
            {
                file.delete();
            }
        }
        folder.delete();
    }
    
    /**
     * 
     * @param bitmap
     * @param picType
     * @param fileName
     * @return
     */
    public boolean saveBitmapToFile(Bitmap bitmap, Bitmap.CompressFormat picType, String fileName)
    {   
        File file = new File(picTempPath + File.separatorChar + fileName + ".jpg");
        try
        {
            if (file.exists())
            {
                file.delete();
            }
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(picType, 100, fOut);
            fOut.flush();
            fOut.close();
        }
        catch (Exception e)
        {
            control.getSysKit().getErrorKit().writerLog(e);
            return false;
        }
        return true;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        clearBitmap();
        if (pictures != null)
        {
            for (Picture picture : pictures)
            {
                picture.dispose();
            }
            pictures.clear();
            //pictures = null;
        }
        picIndexs.clear();
        control = null;
        //picIndexs = null;
        //kit = null;
        final File folder =  new File(picTempPath);
        try
        {
            if(picConverterMgr != null)
            {
                picConverterMgr.dispose();                
            }
            
            new Thread()
            {
                public void run()
                {
                    try
                    {
                        deleteTempFile(folder);
                    }
                    catch (Exception e)
                    {
                    }
                }
            }.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        } 
    }
    //
    private String picTempPath;
    // use shape
    private Map<String, Integer> picIndexs;
    //
    private List<Picture> pictures;
    //
    private IControl control;
    //
    private static int bitmapTotalCacheSize;
    //
    private static Map<String, Bitmap> bitmaps = new LinkedHashMap<String, Bitmap>(10);
    
    private PictureConverterMgr picConverterMgr;
}
