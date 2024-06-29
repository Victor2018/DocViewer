package com.cherry.doc.util;

import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.nvqquy98.lib.doc.office.fc.hwpf.HWPFDocument;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.CharacterRun;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.Paragraph;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.Picture;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.Range;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.Table;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.TableCell;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.TableIterator;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.TableRow;
import com.nvqquy98.lib.doc.office.fc.poifs.filesystem.POIFSFileSystem;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: WordUtils
 * Author: Victor
 * Date: 2023/11/01 10:20
 * Description:
 * -----------------------------------------------------------------
 */

public class WordUtils {
    private final static String TAG = "WordUtils";
    public String htmlPath;
    private String docPath;
    private String picturePath;
    private String dir;
    private int imgWidth = 0;
    private List<Picture> pictures;
    private TableIterator tableIterator;
    private int presentPicture = 0;
    private FileOutputStream output;

    //写入html文件用到的标签 适配手机屏幕
    private String htmlBegin =
            "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<meta charset=\"utf-8\" name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no\">"+
                    "</head>" +
                    "<body>";
    private String htmlEnd = "</body></html>";
    private String tableBegin = "<table style=\"border-collapse:collapse\" border=1 bordercolor=\"black\">";
    private String tableEnd = "</table>";
    private String rowBegin = "<tr>", rowEnd = "</tr>";
    private String columnBegin = "<td>", columnEnd = "</td>";
    private String lineBegin = "<p>", lineEnd = "</p>";
    private String centerBegin = "<center>", centerEnd = "</center>";
    private String boldBegin = "<b>", boldEnd = "</b>";
    private String underlineBegin = "<u>", underlineEnd = "</u>";
    private String italicBegin = "<i>", italicEnd = "</i>";
    private String fontSizeTag = "<font size=\"%d\">";
    private String fontColorTag = "<font color=\"%s\">";
    private String fontEnd = "</font>";
    private String spanColor = "<span style=\"color:%s;\">", spanEnd = "</span>";
    private String divRight = "<div align=\"right\">", divEnd = "</div>";
    private String imgBegin = "<img src=\"%s\" width=\"100%\" height=\"auto\">";

    private static WordUtils instance = null;
    private WordUtils(BasicSet basicSet) {
        initSetting(basicSet);
    }
    public static WordUtils getInstance(BasicSet basicSet) {
        if (instance == null) {
            instance = new WordUtils(basicSet);
        }
        return instance;
    }

    /**
     * doc、docx 转 html
     */
    public String word2html(){
        try {
            output = new FileOutputStream(new File(htmlPath));
            presentPicture = 0;
            output.write(htmlBegin.getBytes());
            if (docPath.endsWith(".doc")) {
                readDOC();
            } else if (docPath.endsWith(".docx")) {
                readDOCX();
            }
            output.write(htmlEnd.getBytes());
            output.close();
            Log.e("html_save_Path",htmlPath);
            return htmlPath;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    //读取word中的内容并写入html文件中
    //.doc文件处理
    private void readDOC() throws Exception {
        FileInputStream in = new FileInputStream(docPath);
        //PoiFs 主要类 管理整个文件系统生命周期
//        POIFSFileSystem pfs = new POIFSFileSystem(in);
        //获取文档所有的数据结构 可以说是一个“文档对象”
        HWPFDocument hwpf = new HWPFDocument(in);
        //HWPF对象中心类
        Range range = hwpf.getRange();
        //获取所有图片放在List<Pictures>中
        pictures = hwpf.getPicturesTable().getAllPictures(picturePath);
        //迭代器
        tableIterator = new TableIterator(range);
        // 得到页面所有的段落数
        int numParagraphs = range.numParagraphs();
        // 遍历段落数
        for (int i = 0; i < numParagraphs; i++) {
            // 得到文档中的每一个段落
            Paragraph p = range.getParagraph(i);
            //表格处理
            if (p.isInTable()) {
                Log.e("isInTable","isInTable");
                int temp = i;
                if (tableIterator.hasNext()) {
                    Table table = tableIterator.next();
                    output.write(tableBegin.getBytes());
                    int rows = table.numRows();
                    for (int r = 0; r < rows; r++) {
                        output.write(rowBegin.getBytes());
                        TableRow row = table.getRow(r);
                        int cols = row.numCells();
                        int rowNumParagraphs = row.numParagraphs();
                        int colsNumParagraphs = 0;
                        for (int c = 0; c < cols; c++) {
                            output.write(columnBegin.getBytes());
                            TableCell cell = row.getCell(c);
                            int max = temp + cell.numParagraphs();
                            colsNumParagraphs = colsNumParagraphs + cell.numParagraphs();
                            for (int cp = temp; cp < max; cp++) {
                                Paragraph p1 = range.getParagraph(cp);
                                output.write(lineBegin.getBytes());
                                writeParagraphContent(p1);
                                output.write(lineEnd.getBytes());
                                temp++;
                            }
                            output.write(columnEnd.getBytes());
                        }
                        int max1 = temp + rowNumParagraphs;
                        for (int m = temp + colsNumParagraphs; m < max1; m++) {
                            temp++;
                        }
                        output.write(rowEnd.getBytes());
                    }
                    output.write(tableEnd.getBytes());
                }
                i = temp;
            } else {
                output.write(lineBegin.getBytes());
                writeParagraphContent(p);
                output.write(lineEnd.getBytes());
            }
        }
    }
    //.docx文件处理
    private void readDOCX() throws  Exception{
        //docx用zip打开
        ZipFile docxFile = new ZipFile(new File(docPath));
        //getEntry返回zip里的文件项 Error=>null
        //word/document.xml 里是文档内容  （文字 图片）
        ZipEntry sharedStringXML = docxFile.getEntry("word/document.xml");
        //getInputStream返回一个输入流 读取指定zip文件项
        InputStream inputStream = docxFile.getInputStream(sharedStringXML);
        //Xml.newPullParser() 返回一个解析器
        XmlPullParser xmlParser = Xml.newPullParser();
        //设置解析器要处理的输入流
        xmlParser.setInput(inputStream, "utf-8");
        boolean isTable = false; // 表格
        boolean isSize = false; // 文字大小
        boolean isColor = false; // 文字颜色
        boolean isCenter = false; // 居中对齐
        boolean isRight = false; // 靠右对齐
        boolean isItalic = false; // 斜体
        boolean isUnderline = false; // 下划线
        boolean isBold = false; // 加粗
        boolean isRegion = false; // 在那个区域中
        int pic_index = 1; // docx中的图片名从image1开始，所以索引从1开始
        //返回当前事件类型 [START_TAG, END_TAG,END_DOCUMENT , TEXT, etc.]
        int event_type = xmlParser.getEventType();
        //当不等于结束标记时
        while (event_type != XmlPullParser.END_DOCUMENT) {
            switch (event_type) {
                //开始标记
                case XmlPullParser.START_TAG: // 开始标签
                    //每一行的标签名 如：document body p 等等
                    String tagBegin = xmlParser.getName();
                    //equalsIgnoreCase忽略大小写去比较
                    // p为一个段落（表格除外）
                    if (tagBegin.equalsIgnoreCase("p") && !isTable) {// 检测到段落，如果在表格中就无视
                        output.write(lineBegin.getBytes());
                    }
                    //r标签
                    if (tagBegin.equalsIgnoreCase("r")) {
                        isRegion = true;
                    }
                    // 判断对齐方式
                    if (tagBegin.equalsIgnoreCase("jc")) {
                        String align = xmlParser.getAttributeValue(0);
                        if (align.equals("center")) {
                            output.write(centerBegin.getBytes());
                            isCenter = true;
                        }
                        if (align.equals("right")) {
                            output.write(divRight.getBytes());
                            isRight = true;
                        }
                    }
                    // color标签 判断文字颜色
                    if (tagBegin.equalsIgnoreCase("color")) {
                        //获取颜色值
                        String color = xmlParser.getAttributeValue(0);
                        //写入输入流
                        //拼接参数 "<span style=\"color:%s;\">", spanEnd = "</span>";
                        output.write(String.format(spanColor, "#000").getBytes());
                        isColor = true;
                    }
                    // 判断文字大小
                    if (tagBegin.equalsIgnoreCase("sz")) {
                        if (isRegion == true) {
                            int size = getSize(Integer.valueOf(xmlParser.getAttributeValue(0)));
                            output.write(String.format(fontSizeTag, size).getBytes());
                            isSize = true;
                        }
                    }
                    if (tagBegin.equalsIgnoreCase("tbl")) { // 检测到表格
                        output.write(tableBegin.getBytes());
                        isTable = true;
                    } else if (tagBegin.equalsIgnoreCase("tr")) { // 表格行
                        output.write(rowBegin.getBytes());
                    } else if (tagBegin.equalsIgnoreCase("tc")) { // 表格列
                        output.write(columnBegin.getBytes());
                    }
                    if (tagBegin.equalsIgnoreCase("pic")) { // 检测到图片

                            /*ZipEntry pic_entry = FileUtils.getPicEntry(docxFile, "word", ""+pic_index);
                            if (pic_entry != null) {
                                byte[] pictureBytes = FileUtils.getPictureBytes(docxFile, pic_entry);
                                writeDocumentPicture(pictureBytes);
                            }
                            pic_index++; // 转换一张后，索引+1*/
                    }
                    if(tagBegin.equalsIgnoreCase("cNvPr")){// 检测到图片
                        String picName =  xmlParser.getAttributeValue(2);
                        //Log.e("检测到图片","检测到图片"+picName);
                        ZipEntry pic_entry = FileUtils.getPicEntry(docxFile, "word", picName);
                        if (pic_entry != null) {
                            byte[] pictureBytes = FileUtils.getPictureBytes(docxFile, pic_entry);
                            writeDocumentPicture(pictureBytes);
                        }
                    }

                    if (tagBegin.equalsIgnoreCase("b")) { // 检测到加粗
                        isBold = true;
                    }
                    if (tagBegin.equalsIgnoreCase("u")) { // 检测到下划线
                        isUnderline = true;
                    }
                    if (tagBegin.equalsIgnoreCase("i")) { // 检测到斜体
                        isItalic = true;
                    }
                    // 检测到文本
                    if (tagBegin.equalsIgnoreCase("t")) {
                        if (isBold == true) { // 加粗
                            output.write(boldBegin.getBytes());
                        }
                        if (isUnderline == true) { // 检测到下划线，输入<u>
                            output.write(underlineBegin.getBytes());
                        }
                        if (isItalic == true) { // 检测到斜体，输入<i>
                            output.write(italicBegin.getBytes());
                        }
                        // 如果当前事件是START_TAG，
                        // 那么如果下一个元素是TEXT，那么返回元素内容;
                        // 如果下一个事件是END_TAG，那么返回空字符串，
                        // 否则抛出异常。
                        String text = xmlParser.nextText();
                        output.write(text.getBytes()); // 写入文本
                        if (isItalic == true) { // 输入斜体结束标签</i>
                            output.write(italicEnd.getBytes());
                            isItalic = false;
                        }
                        if (isUnderline == true) { // 输入下划线结束标签</u>
                            output.write(underlineEnd.getBytes());
                            isUnderline = false;
                        }
                        if (isBold == true) { // 输入加粗结束标签</b>
                            output.write(boldEnd.getBytes());
                            isBold = false;
                        }
                        if (isSize == true) { // 输入字体结束标签</font>
                            output.write(fontEnd.getBytes());
                            isSize = false;
                        }
                        if (isColor == true) { // 输入跨度结束标签</span>
                            output.write(spanEnd.getBytes());
                            isColor = false;
                        }
//						if (isCenter == true) { // 输入居中结束标签</center>。要在段落结束之前再输入该标签，因为该标签会强制换行
//							output.write(centerEnd.getBytes());
//							isCenter = false;
//						}
                        if (isRight == true) { // 输入区块结束标签</div>
                            output.write(divEnd.getBytes());
                            isRight = false;
                        }
                    }
                    break;
                // 结束标签
                case XmlPullParser.END_TAG:
                    String tagEnd = xmlParser.getName();
                    if (tagEnd.equalsIgnoreCase("tbl")) { // 输入表格结束标签</table>
                        output.write(tableEnd.getBytes());
                        isTable = false;
                    }
                    if (tagEnd.equalsIgnoreCase("tr")) { // 输入表格行结束标签</tr>
                        output.write(rowEnd.getBytes());
                    }
                    if (tagEnd.equalsIgnoreCase("tc")) { // 输入表格列结束标签</td>
                        output.write(columnEnd.getBytes());
                    }
                    if (tagEnd.equalsIgnoreCase("p")) { // 输入段落结束标签</p>，如果在表格中就无视
                        if (isTable == false) {
                            if (isCenter == true) { // 输入居中结束标签</center>
                                output.write(centerEnd.getBytes());
                                isCenter = false;
                            }
                            output.write(lineEnd.getBytes());
                        }
                    }
                    if (tagEnd.equalsIgnoreCase("r")) {
                        isRegion = false;
                    }
                    break;
                default:
                    break;
            }
            event_type = xmlParser.next();
        }
    }
    //获取字体大小
    private int getSize(int sizeType) {
        if (sizeType >= 1 && sizeType <= 8) {
            return 1;
        } else if (sizeType >= 9 && sizeType <= 11) {
            return 2;
        } else if (sizeType >= 12 && sizeType <= 14) {
            return 3;
        } else if (sizeType >= 15 && sizeType <= 19) {
            return 4;
        } else if (sizeType >= 20 && sizeType <= 29) {
            return 5;
        } else if (sizeType >= 30 && sizeType <= 39) {
            return 6;
        } else if (sizeType >= 40) {
            return 7;
        } else {
            return 3;
        }
    }
    //获取颜色
    private String getColor(int colorType) {
        if (colorType == 1) {
            return "#000000";
        } else if (colorType == 2) {
            return "#0000FF";
        } else if (colorType == 3 || colorType == 4) {
            return "#00FF00";
        } else if (colorType == 5 || colorType == 6) {
            return "#FF0000";
        } else if (colorType == 7) {
            return "#FFFF00";
        } else if (colorType == 8) {
            return "#FFFFFF";
        } else if (colorType == 9 || colorType == 15) {
            return "#CCCCCC";
        } else if (colorType == 10 || colorType == 11) {
            return "#00FF00";
        } else if (colorType == 12 || colorType == 16) {
            return "#080808";
        } else if (colorType == 13 || colorType == 14) {
            return "#FFFF00";
        } else {
            return "#000000";
        }
    }
    //往html文件写入图片
    public void writeDocumentPicture(byte[] pictureBytes) throws IOException {
        picturePath = FileUtils.createFile(dir+"/pic", "img_" + presentPicture + ".jpg");
        FileUtils.writeFile(picturePath, pictureBytes);
        presentPicture++;
        String imageString = String.format(imgBegin, "file://"+picturePath);
        Log.e("imageString",imageString);
        output.write(imageString.getBytes());
    }
    //往html文件写入文字
    public void writeParagraphContent(Paragraph paragraph) throws IOException {
        //段落
        Paragraph p = paragraph;
        //段落字符数
        int pnumCharacterRuns = p.numCharacterRuns();
        for (int j = 0; j < pnumCharacterRuns; j++) {
            //CharacterRun文本类 getCharacterRun根据索引获取文本
            CharacterRun run = p.getCharacterRun(j);
            if (run.getPicOffset() == 0 || run.getPicOffset() >= 1000) {
                if (presentPicture < pictures.size()) {
                    //写入图片
                    writeDocumentPicture(pictures.get(presentPicture).getContent());
                }
            } else {
                //文本
                String text = run.text();
                if (text.length() >= 2 && pnumCharacterRuns < 2) {
                    output.write(text.getBytes());
                } else {
                    //格式获取
                    String fontSizeBegin = String.format(fontSizeTag, getSize(run.getFontSize()));
                    String fontColorBegin = String.format(fontColorTag, getColor(run.getColor()));
                    output.write(fontSizeBegin.getBytes());
                    output.write(fontColorBegin.getBytes());
                    if (run.isBold()) {
                        output.write(boldBegin.getBytes());
                    }
                    if (run.isItalic()) {
                        output.write(italicBegin.getBytes());
                    }
                    output.write(text.getBytes());
                    if (run.isBold()) {
                        output.write(boldEnd.getBytes());
                    }
                    if (run.isItalic()) {
                        output.write(italicEnd.getBytes());
                    }
                    output.write(fontEnd.getBytes());
                    output.write(fontEnd.getBytes());
                }
            }
        }
    }

    private void initSetting(BasicSet basicSet){
        htmlPath = basicSet.getHtmlPath();
        docPath = basicSet.getDocPath();
        picturePath = basicSet.getPicturePath();
        dir = basicSet.getDir();
        htmlBegin = basicSet.getHtmlBegin();
        htmlEnd  = basicSet.getHtmlEnd();
        tableBegin = basicSet.getTableBegin();
        tableEnd = basicSet.getTableEnd();
        rowBegin = basicSet.getRowBegin();
        columnBegin = basicSet.getColumnBegin();
        lineBegin = basicSet.getLineBegin();
        centerBegin = basicSet.getCenterBegin();
        boldBegin = basicSet.getBoldBegin();
        underlineBegin = basicSet.getUnderlineBegin();
        italicBegin = basicSet.getItalicBegin();
        fontSizeTag = basicSet.getFontSizeTag();
        fontColorTag = basicSet.getFontColorTag();
        fontEnd  = basicSet.getFontEnd();
        spanColor = basicSet.getSpanColor();
        divRight = basicSet.getDivRight();
        imgBegin = basicSet.getImgBegin();
    }
}
