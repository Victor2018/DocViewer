package com.cherry.doc.util;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: FileUtils
 * Author: Victor
 * Date: 2023/11/01 10:24
 * Description:
 * -----------------------------------------------------------------
 */

public class FileUtils {
    private final static String TAG = "FileUtils";

    public static String getFileName(String pathandname) {
        int start = pathandname.lastIndexOf("/");
        int end = pathandname.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathandname.substring(start + 1, end);
        } else {
            return "";
        }
    }

    public static String getFileNameWithExt(String pathandname) {
        int start = pathandname.lastIndexOf("/");
        if (start >= 0) {
            return pathandname.substring(start + 1);
        } else {
            return pathandname;
        }
    }

    public static String createFile(String dir_name, String file_name) {
        String dir_path = String.format("%s", dir_name);
        String file_path = String.format("%s/%s", dir_path, file_name);
        try {
            File dirFile = new File(dir_path);
            if (!dirFile.exists()) {
                dirFile.mkdir();
            }
            File myFile = new File(file_path);
            Log.e("dir_path",dir_path);
            Log.e("file_path",file_path);
            myFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file_path;
    }

    public static ZipEntry getPicEntry(ZipFile file, String type, String picName) {
        String entry_jpeg = type + "/media/" + picName;
        String entry_jpg = type + "/media/" + picName;
        String entry_png = type + "/media/" + picName;
        String entry_gif = type + "/media/" + picName;
        String entry_wmf = type + "/media/" + picName;
        ZipEntry pic_entry = null;
        pic_entry = file.getEntry(entry_jpeg);
        // 以下为读取docx的图片 转化为流数组
        if (pic_entry == null) {
            pic_entry = file.getEntry(entry_png);
        }
        if (pic_entry == null) {
            pic_entry = file.getEntry(entry_jpg);
        }
        if (pic_entry == null) {
            pic_entry = file.getEntry(entry_gif);
        }
        if (pic_entry == null) {
            pic_entry = file.getEntry(entry_wmf);
        }
        return pic_entry;
    }

    public static byte[] getPictureBytes(ZipFile file, ZipEntry pic_entry) {
        byte[] pictureBytes = null;
        try {
            InputStream pictIS = file.getInputStream(pic_entry);
            ByteArrayOutputStream pOut = new ByteArrayOutputStream();
            byte[] b = new byte[1000];
            int len = 0;
            while ((len = pictIS.read(b)) != -1) {
                pOut.write(b, 0, len);
            }
            pictIS.close();
            pOut.close();
            pictureBytes = pOut.toByteArray();
            Log.d(TAG, "pictureBytes.length=" + pictureBytes.length);
            if (pictIS != null) {
                pictIS.close();
            }
            if (pOut != null) {
                pOut.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pictureBytes;

    }

//	public static void writePicture(String pic_path, byte[] pictureBytes) {
//		File myPicture = new File(pic_path);
//		try {
//			FileOutputStream outputPicture = new FileOutputStream(myPicture);
//			outputPicture.write(pictureBytes);
//			outputPicture.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

    public static void writeFile(String path, byte[] bytes) {
        File file = new File(path);
        try {
            File dirFile = new File(path.substring(0, path.lastIndexOf("/")));
            if (dirFile.exists() != true) {
                dirFile.mkdirs();
            }
            FileOutputStream output = new FileOutputStream(file);
            output.write(bytes);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveBitmap(String path, Bitmap bitmap) {
        try {
            File dirFile = new File(path.substring(0, path.lastIndexOf("/")));
            if (dirFile.exists() != true) {
                dirFile.mkdirs();
            }
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(path));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
