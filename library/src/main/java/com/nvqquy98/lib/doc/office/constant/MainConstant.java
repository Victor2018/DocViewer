/*
 * 文件名称:          MainConstant.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:29:36
 */
package com.nvqquy98.lib.doc.office.constant;

public final class MainConstant
{
    public static final byte APPLICATION_TYPE_WP = 0;

    public static final byte APPLICATION_TYPE_SS = 1;

    public static final byte APPLICATION_TYPE_PPT = 2;

    public static final byte APPLICATION_TYPE_PDF = 3;

    public static final byte APPLICATION_TYPE_TXT = 4;

    public static final String FILE_TYPE_DOC = "doc";

    public static final String FILE_TYPE_DOCX = "docx";

    public static final String FILE_TYPE_XLS = "xls";

    public static final String FILE_TYPE_XLSX = "xlsx";

    public static final String FILE_TYPE_PPT = "ppt";

    public static final String FILE_TYPE_PPTX = "pptx";

    public static final String FILE_TYPE_TXT = "txt";
    //
    public static final String  FILE_TYPE_PDF = "pdf";
    
    public static final String FILE_TYPE_DOT = "dot";
    public static final String FILE_TYPE_DOTX = "dotx";
    public static final String FILE_TYPE_DOTM = "dotm";
    public static final String FILE_TYPE_XLT = "xlt";
    public static final String FILE_TYPE_XLTX = "xltx";
    public static final String FILE_TYPE_XLSM = "xlsm";
    public static final String FILE_TYPE_XLTM = "xltm";
    public static final String FILE_TYPE_POT = "pot";
    public static final String FILE_TYPE_PPTM = "pptm";
    public static final String FILE_TYPE_POTX = "potx";
    public static final String FILE_TYPE_POTM = "potm";

    public static final String INTENT_FILED_FILE_PATH = "filePath";

    public static final String INTENT_FILED_FILE_LIST_TYPE = "fileListType";

    public static final String INTENT_FILED_MARK_FILES = "markFiles";

    public static final String INTENT_FILED_RECENT_FILES = "recentFiles";

    public static final String INTENT_FILED_SDCARD_FILES = "sdcard";

    public static final String INTENT_FILED_MARK_STATUS = "markFileStatus";

    public static final int GAP = 5;
    //
    public static final int ZOOM_ROUND = 10000000;

    public static final float POINT_DPI = 72.0f;

    public static final float MM_TO_POINT = 2.835f;

    public static final float TWIPS_TO_POINT = 1 / 20.0f;

    public static final float POINT_TO_TWIPS = 20.0f;

    public static final float DEFAULT_TAB_WIDTH_POINT = 21;

    public static final int EMU_PER_INCH = 914400;    

    public static final float PIXEL_DPI =  96.f;

    public static final float POINT_TO_PIXEL = PIXEL_DPI / POINT_DPI;

    public static final float PIXEL_TO_POINT = POINT_DPI / PIXEL_DPI;

    public static final float TWIPS_TO_PIXEL = TWIPS_TO_POINT * POINT_TO_PIXEL;

    public static final float PIXEL_TO_TWIPS = PIXEL_TO_POINT * POINT_TO_TWIPS;

    public static final float DEFAULT_TAB_WIDTH_PIXEL = DEFAULT_TAB_WIDTH_POINT * POINT_TO_PIXEL;

    // recently opened files
    public static final String TABLE_RECENT = "openedfiles";
    // starred files
    public static final String TABLE_STAR = "starredfiles";
    // settings
    public static final String TABLE_SETTING ="settings";

    public static final int HANDLER_MESSAGE_SUCCESS = 0;

    public static final int HANDLER_MESSAGE_ERROR = HANDLER_MESSAGE_SUCCESS + 1;

    public static final int HANDLER_MESSAGE_SHOW_PROGRESS = HANDLER_MESSAGE_ERROR + 1;

    public static final int HANDLER_MESSAGE_DISMISS_PROGRESS = HANDLER_MESSAGE_SHOW_PROGRESS + 1;

    public static final int HANDLER_MESSAGE_DISPOSE = HANDLER_MESSAGE_DISMISS_PROGRESS + 1;

    public static final int HANDLER_MESSAGE_SEND_READER_INSTANCE = HANDLER_MESSAGE_DISPOSE;
    
    //zoom
    public static final int STANDARD_RATE = 10000;
    public static final int MAXZOOM = 30000;
    public static final int MAXZOOM_THUMBNAIL = 5000;
    
    // Drawing mode
    //not callout mode
    public static final int DRAWMODE_NORMAL = 0;
    //draw callout
    public static final int DRAWMODE_CALLOUTDRAW = 1;
    //erase callout
    public static final int DRAWMODE_CALLOUTERASE = 2;
}
