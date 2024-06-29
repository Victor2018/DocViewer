
package com.nvqquy98.lib.doc.office.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import com.nvqquy98.lib.doc.office.constant.MainConstant;

public class FileKit
{
    //
    private static FileKit mt = new FileKit();
    
    /**
     * 
     */
    private FileKit()
    {
        
    }
    
    //instance
    public static FileKit instance()
    {
        return mt;
    }
 
    /**
     * delete file
     * @param file
     */
    public void deleteFile(File file)
    {
        if (file.isDirectory())
        {
            File[] files = file.listFiles();
            if (files != null)
            {             
                for (File tempFile : files)
                {
                    deleteFile(tempFile);
                }
            }
            file.delete();
        }
        else
        {
            file.delete();
        }
    }

    /**
     * paste file
     * @param fromFile
     * @param toFile
     */
    public void pasteFile(File fromFile, File toFile)
    {
        if (fromFile.isDirectory())
        {
            copyFolder(fromFile, toFile);
        }
        else
        {
            copyFile(fromFile, toFile);
        }
    }
    
    /**
     * copy file
     * @param fromFile
     * @param toFile
     */
    public void copyFile(File fromFile, File toFile)
    {
        if (toFile.exists())
        {
            toFile.delete();
        }
        
        try
        {
            FileInputStream fosfrom = new FileInputStream(fromFile);
            FileOutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[8192];
            int c;
            while ((c = fosfrom.read(bt)) > 0)
            {
                fosto.write(bt, 0, c); //write the context to the new file
            }
            fosfrom.close();
            fosto.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
  
    /**
     * copy file folder
     * @param fromFile
     * @param toFile
     */
    public void copyFolder(File fromFile, File toFile)
    {
        if (!toFile.exists())
        {
            toFile.mkdir();
        }
        
        String toPath = toFile.getAbsolutePath();
        File[] files = fromFile.listFiles();
        if (files != null)
        {
            for (File tempFile : files)
            {
                if (toPath.endsWith(File.separator))
                {
                    pasteFile(tempFile, new File(toPath + tempFile.getName()));
                }
                else
                {
                    pasteFile(tempFile, new File(toPath + File.separator + tempFile.getName()));
                }               
            }
        }
    }
    
    /**
     * 判断是否支持
     */
    public boolean isSupport(String fileName)
    {
        // word
        fileName = fileName.toLowerCase();
        return fileName.indexOf(".") > 0 &&
           (fileName.endsWith(MainConstant.FILE_TYPE_DOC)
            || fileName.endsWith(MainConstant.FILE_TYPE_DOCX)
            || fileName.endsWith(MainConstant.FILE_TYPE_XLS)
            || fileName.endsWith(MainConstant.FILE_TYPE_XLSX)
            || fileName.endsWith(MainConstant.FILE_TYPE_PPT)
            || fileName.endsWith(MainConstant.FILE_TYPE_PPTX)
            || fileName.endsWith(MainConstant.FILE_TYPE_TXT)
            || fileName.endsWith(MainConstant.FILE_TYPE_DOT)
            || fileName.endsWith(MainConstant.FILE_TYPE_DOTX)
            || fileName.endsWith(MainConstant.FILE_TYPE_DOTM)
            || fileName.endsWith(MainConstant.FILE_TYPE_XLT)
            || fileName.endsWith(MainConstant.FILE_TYPE_XLTX)
            || fileName.endsWith(MainConstant.FILE_TYPE_XLTM)
            || fileName.endsWith(MainConstant.FILE_TYPE_XLSM)
            || fileName.endsWith(MainConstant.FILE_TYPE_POT)
            || fileName.endsWith(MainConstant.FILE_TYPE_PPTM)
            || fileName.endsWith(MainConstant.FILE_TYPE_POTX)
            || fileName.endsWith(MainConstant.FILE_TYPE_POTM)
            || fileName.endsWith(MainConstant.FILE_TYPE_PDF));
    }
    
    /**
     * 判断文件是否标星
     * @param fileName
     * @return
     */
    public boolean isFileMarked(String filePath, List<File> fileList)
    {
        if (filePath == null ||fileList == null || fileList.size() == 0)
        {
            return false;
        }
        for (File file: fileList)
        {
            if (filePath.equals(file.getAbsolutePath()))
            {
                return true;
            }
        }
        return false;
    }
}
