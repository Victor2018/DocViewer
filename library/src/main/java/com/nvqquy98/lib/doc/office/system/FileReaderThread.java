/*
 * 文件名称:          FileReaderThread.java
 *
 * 编译器:            android2.2
 * 时间:              下午8:11:02
 */
package com.nvqquy98.lib.doc.office.system;

import com.nvqquy98.lib.doc.bean.FileType;
import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.fc.doc.DOCReader;
import com.nvqquy98.lib.doc.office.fc.doc.DOCXReader;
import com.nvqquy98.lib.doc.office.fc.doc.TXTReader;
import com.nvqquy98.lib.doc.office.fc.pdf.PDFReader;
import com.nvqquy98.lib.doc.office.fc.ppt.PPTReader;
import com.nvqquy98.lib.doc.office.fc.ppt.PPTXReader;
import com.nvqquy98.lib.doc.office.fc.xls.XLSReader;
import com.nvqquy98.lib.doc.office.fc.xls.XLSXReader;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import timber.log.Timber;

/**
 * 读取文档线程
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-3-12
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:
 * <p>
 * <p>
 */
public class FileReaderThread extends Thread {

    /**
     * @param handler
     * @param filePath
     * @param encoding
     */
    public FileReaderThread(IControl control, Handler handler, String filePath, int docSourceType, int fileType, String encoding) {
        this.control = control;
        this.handler = handler;
        this.filePath = filePath;
        this.fileType = fileType;
        this.docSourceType = docSourceType;
        this.encoding = encoding;
    }

    /**
     *
     */
    public void run() {
        // show progress
        Message msg = new Message();
        msg.what = MainConstant.HANDLER_MESSAGE_SHOW_PROGRESS;
        handler.handleMessage(msg);
        msg = new Message();
        msg.what = MainConstant.HANDLER_MESSAGE_DISMISS_PROGRESS;
        try {
            IReader reader = null;
            String fileName = filePath.toLowerCase();
            // doc
            if (fileName.endsWith(MainConstant.FILE_TYPE_DOC)
                    || fileName.endsWith(MainConstant.FILE_TYPE_DOT)
                    || fileType == FileType.DOC) {
                reader = new DOCReader(control, filePath, docSourceType);
            }
            // docx
            else if (fileName.endsWith(MainConstant.FILE_TYPE_DOCX)
                    || fileName.endsWith(MainConstant.FILE_TYPE_DOTX)
                    || fileName.endsWith(MainConstant.FILE_TYPE_DOTM)
                    || fileType == FileType.DOCX) {
                reader = new DOCXReader(control, filePath, docSourceType);
            }
            // txt
            else if (fileName.endsWith(MainConstant.FILE_TYPE_TXT) || fileType == FileType.TXT) {
                reader = new TXTReader(control, filePath, docSourceType, encoding);
            }
            // xls
            else if (fileName.endsWith(MainConstant.FILE_TYPE_XLS)
                    || fileName.endsWith(MainConstant.FILE_TYPE_XLT)
                    || fileType == FileType.XLS) {
                reader = new XLSReader(control, filePath, docSourceType);
            }
            // xlsx
            else if (fileName.endsWith(MainConstant.FILE_TYPE_XLSX)
                    || fileName.endsWith(MainConstant.FILE_TYPE_XLTX)
                    || fileName.endsWith(MainConstant.FILE_TYPE_XLTM)
                    || fileName.endsWith(MainConstant.FILE_TYPE_XLSM)
                    || fileType == FileType.XLSX) {
                reader = new XLSXReader(control, filePath, docSourceType);
            }
            // ppt
            else if (fileName.endsWith(MainConstant.FILE_TYPE_PPT)
                    || fileName.endsWith(MainConstant.FILE_TYPE_POT)
                    || fileType == FileType.PPT) {
                reader = new PPTReader(control, filePath, docSourceType);
            }
            // pptx
            else if (fileName.endsWith(MainConstant.FILE_TYPE_PPTX)
                    || fileName.endsWith(MainConstant.FILE_TYPE_PPTM)
                    || fileName.endsWith(MainConstant.FILE_TYPE_POTX)
                    || fileName.endsWith(MainConstant.FILE_TYPE_POTM)
                    || fileType == FileType.PPTX) {
                reader = new PPTXReader(control, filePath, docSourceType);
            }
            // PDF document
            else if (fileName.endsWith(MainConstant.FILE_TYPE_PDF) || fileType == FileType.PDF) {
                reader = new PDFReader(control, filePath);
            }
            // other
            else {
                reader = new TXTReader(control, filePath, docSourceType, encoding);
            }
            Timber.e(getName(), "reader = " + reader.getClass().getSimpleName());
            // 把IReader实例传出
            Message mesReader = new Message();
            mesReader.obj = reader;
            mesReader.what = MainConstant.HANDLER_MESSAGE_SEND_READER_INSTANCE;
            handler.handleMessage(mesReader);
            msg.obj = reader.getModel();
            reader.dispose();
            msg.what = MainConstant.HANDLER_MESSAGE_SUCCESS;
            //handler.handleMessage(msg);
        } catch (OutOfMemoryError eee) {
            msg.what = MainConstant.HANDLER_MESSAGE_ERROR;
            msg.obj = eee;
        } catch (Exception ee) {
            msg.what = MainConstant.HANDLER_MESSAGE_ERROR;
            msg.obj = ee;
        } catch (AbortReaderError ee) {
            msg.what = MainConstant.HANDLER_MESSAGE_ERROR;
            msg.obj = ee;
        } finally {
            handler.handleMessage(msg);
            control = null;
            handler = null;
            encoding = null;
            filePath = null;
        }
    }

    //
    private String encoding;
    //
    private String filePath;
    private int fileType;
    private int docSourceType;
    //
    private Handler handler;
    //
    private IControl control;
}
