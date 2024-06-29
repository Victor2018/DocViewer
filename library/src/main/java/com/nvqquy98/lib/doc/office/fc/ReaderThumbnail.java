/*
 * 文件名称:          PTTReaderThumbnail.java
 *  
 * 编译器:            android2.2
 * 时间:              下午4:18:41
 */
package com.nvqquy98.lib.doc.office.fc;

import java.io.File;
import java.io.FileInputStream;

import com.nvqquy98.lib.doc.bean.DocSourceType;
import com.nvqquy98.lib.doc.office.fc.fs.filesystem.CFBFileSystem;
import com.nvqquy98.lib.doc.office.fc.fs.filesystem.Property;
import com.nvqquy98.lib.doc.office.fc.fs.storage.LittleEndian;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackagePart;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackageRelationship;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackageRelationshipTypes;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.ZipPackage;
import com.nvqquy98.lib.doc.office.fc.pdf.PDFLib;
import com.nvqquy98.lib.doc.office.fc.ppt.PPTReader;
import com.nvqquy98.lib.doc.office.java.awt.Dimension;
import com.nvqquy98.lib.doc.office.pg.control.PGEditor;
import com.nvqquy98.lib.doc.office.pg.model.PGModel;
import com.nvqquy98.lib.doc.office.pg.view.SlideDrawKit;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Rect;


/**
 * get thumbnail of PPT document
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-12-13
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class ReaderThumbnail
{
    //
    private static ReaderThumbnail kit = new ReaderThumbnail();
    
    public static  ReaderThumbnail instance()
    {
        return kit;
    }    
    
    /**
     * 
     * @param filePath
     * @return
     * @throws Exception
     */
    public Bitmap getThumbnailForPPT(String filePath, int width, int height)
        throws Exception
    {
        /*if (true)
        {
            return getThumbnailForPPT_Small(filePath, width, height);
        }*/
        FileInputStream fis = new FileInputStream(new File(filePath));
        CFBFileSystem poifs = new CFBFileSystem(fis, true); 
        Property property = poifs.getProperty("\u0005SummaryInformation");
        if (property != null)
        {
            byte[] data = property.getDocumentRawData();
            
            int offset = 0;
            int byteOrder = LittleEndian.getUShort(data, offset);
            offset += LittleEndian.SHORT_SIZE;
            
            int format = LittleEndian.getUShort(data, offset);
            offset += LittleEndian.SHORT_SIZE;
            
            int osVersion = (int) LittleEndian.getUInt(data, offset);
            offset += LittleEndian.INT_SIZE;
            
            //int classID = new ClassID(src, o);
            // skip ClassID length
            offset += 16;
            
            int sectionCount = LittleEndian.getInt(data, offset);
            offset += LittleEndian.INT_SIZE;
            if (sectionCount < 0)
            {
                //throw new HPSFRuntimeException("Section count " + sectionCount +
                //    " is negative.");
                return null;
            }
            for (int i = 0; i < sectionCount; i++)
            {
                //final Section s = new Section(data, offset);
                Bitmap thumbnail = readSection(data, offset, width, height);
                if(thumbnail != null)
                {
                    return thumbnail;
                }
                // skip ClassID length + 4
                offset += 16 + LittleEndian.INT_SIZE;
            }
        }
        return null;
    }
    
    /**
     * \
     * @param data
     * @param offset
     * @param width thumbnail width
     * @param height thumbnail height
     * @return
     */
    private Bitmap readSection(byte[] data, int offset, int width, int height)
    {
        int read_off = offset;        
        //int formatID = new ClassID(data, o1);
        // skip ClassID
        read_off += 16;
        /*
         * Read the offset from the stream's start and positions to
         * the section header.
         */
        int section_offset = (int)LittleEndian.getUInt(data, read_off);
        read_off = section_offset;
        /*
         * Read the section length.
         */
        int size = (int) LittleEndian.getUInt(data, read_off);
        read_off += LittleEndian.INT_SIZE;
        /*
         * Read the number of properties.
         */
        final int propertyCount = (int) LittleEndian.getUInt(data, read_off);
        read_off += LittleEndian.INT_SIZE;
        
        int pro_off = read_off;
        int propertyID = -1;
        int propertyOffset = 0;
        for (int i = 0; i < propertyCount; i++)
        {
            //ple = new PropertyListEntry();
            /* Read the property ID. */
            propertyID =  (int) LittleEndian.getUInt(data, pro_off);
            pro_off += LittleEndian.INT_SIZE;

            /* Offset from the section's start. */
            propertyOffset = (int) LittleEndian.getUInt(data, pro_off);
            pro_off += LittleEndian.INT_SIZE;

            //this is thumbnail property
            if (propertyID == 17)
            {
                break;
            }
        }
        // 
        if (propertyID == 17)
        {
            int tOffset = propertyOffset + section_offset;
            // I don't know
            int type = (int)LittleEndian.getUInt(data, tOffset);
            tOffset += LittleEndian.INT_SIZE;
            // thumbnail size
            int tSize = (int)LittleEndian.getUInt(data, tOffset);
            tOffset += LittleEndian.INT_SIZE;
            /*
             * thumbanil OS type  
             *          = 0, no image data associated with the property
             *          =-1, Windows
             *          =-2, Macintosh 
             */
            int osType = (int)LittleEndian.getUInt(data, tOffset);
            tOffset += LittleEndian.INT_SIZE;
            /*
             * thumbnail image data type
             *          = 0, no image data
             *          = 0x0003, WMF
             *          = 0x000E, EMF
             *          = 0x0333, JPEG
             */
            int picType = (int)LittleEndian.getUInt(data, tOffset);
            tOffset += LittleEndian.INT_SIZE;
            
            // windows
            if (osType == -1)
            {
                int pic_data_offset = propertyOffset + section_offset;
                // WMF
                if (picType == 0x0003)
                {   
                    pic_data_offset += 24;
                }
                // EMF
                else if (picType == 0x000E)
                {
                    
                }
                // JPEG
                else if (picType == 0x0333)
                {
                    
                }
                if (pic_data_offset > propertyOffset + section_offset)
                {
                    try
                    {   
                        if (picType == 0x0003)
                        {   
                            // WMF
                            //String pathWMF = MainControl.sysKit.getPictureManage().writeTempFile(data, pic_data_offset, data.length - pic_data_offset);
                            //String path = pathWMF.substring(0, pathWMF.length() -4) + "wmf.tmp"; 
                            //PDFLib.getPDFLib().wmf2Jpg(pathWMF, path, width, height);
                            
                            //return BitmapFactory.decodeFile(path);
//                            FileOutputStream out = new FileOutputStream("thumbnail" + ".wmf");
//                            out.write(data, pic_data_offset, data.length - pic_data_offset);
//                            out.close();
                        }
                        else if (picType == 0x0333)
                        {
                            // JPEG
                            return BitmapFactory.decodeByteArray(data, pic_data_offset, data.length - pic_data_offset);
                        }
                        
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * 
     * @param filePath
     * @param width
     * @param height
     * @return
     */
    private Bitmap getThumbnailForPPT_Small(String filePath, int width, int height)
    {
        try
        {
            //long t = System.currentTimeMillis();
            PPTReader reader  =  new PPTReader(null, filePath, DocSourceType.PATH,true);
            PGModel model = (PGModel)reader.getModel();
            //long t1 = System.currentTimeMillis();
            //Timber.e("read time ", String.valueOf(t1 - t));
            if (model != null)
            {
                Dimension d = model.getPageSize();
                float zoom = (float)(Math.min(width / d.getWidth(), height / d.getHeight()));
                PGEditor editor = new PGEditor(null);
                Bitmap bitmap = SlideDrawKit.instance().getThumbnail(model, editor, model.getSlide(0), zoom);
                //Timber.e("read time ", String.valueOf(System.currentTimeMillis() - t1));
                return bitmap;
                
            }
        }
        catch (Exception e)
        {            
        }
        return null;
        
    }
    
    /**
     * 
     * @return
     */
    public Bitmap getThumbnailForPPTX(String filePath) throws Exception
    {
        ZipPackage zipPackage = new ZipPackage(filePath);
        
        /*URL url = new URL("http://172.25.3.147:8080/ppt_test_2007.pptx");
        zipPackage = new ZipPackage(url.openStream());*/
        
        /*InputStream is = SocketClient.instance().getFile("E:/workdocument/reader/testdocument/ppt_test_2007.pptx");
        zipPackage = new ZipPackage(is);*/  
        
        PackageRelationship thumbnail = zipPackage.getRelationshipsByType(
            PackageRelationshipTypes.THUMBNAIL).getRelationship(0);
        if (thumbnail == null)
        {
            return null;
        }
        PackagePart part = zipPackage.getPart(thumbnail.getTargetURI());
        if (part == null)
        {
            return null;
        }
        return BitmapFactory.decodeStream(part.getInputStream());
    }
    
    /**
     * 
     * @param filePath
     * @param zoom (0 < thumbnail zoom value <= 50)
     * @return
     * @throws Exception
     */
    public Bitmap getThumbnailForPDF(String filePath, float zoom) throws Exception
    {
        try
        {
            PDFLib lib = PDFLib.getPDFLib();            
            lib.openFileSync(filePath);
            if (lib.hasPasswordSync())
            {
                return null;
            }
            Rect rect = lib.getAllPagesSize()[0];
            int w = (int)(rect.width() * zoom);
            int h = (int)(rect.height() * zoom);
            Bitmap bitmap = null;
            try
            {
                bitmap = Bitmap.createBitmap(w, h,  Config.ARGB_8888);
                lib.drawPageSync(bitmap, 0, w, h, 0, 0, w, h, 1);
            }
            catch(OutOfMemoryError e)
            {
                
            }
            return bitmap;
        }
        catch (Exception e)
        {
            return  null;
        }
    }
}
