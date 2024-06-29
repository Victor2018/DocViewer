package com.cherry.doc.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.Picture;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.TableIterator;

import java.io.FileOutputStream;
import java.util.List;

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: BasicSet
 * Author: Victor
 * Date: 2023/11/01 10:22
 * Description:
 * -----------------------------------------------------------------
 */

public class BasicSet {
    private Context mContext;
    private String htmlPath;
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
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0, user-scalable=yes\" /> " +
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
    private String imgBegin = "<img src=\"%s\" width=300px height=\"auto\" />";

    public BasicSet(Context context, String sourceFilePath, String htmlFilePath, String htmlFileName) {
        this.mContext = context;
        this.docPath = sourceFilePath;
        this.dir = htmlFilePath;
        this.htmlPath = FileUtils.createFile(htmlFilePath, htmlFileName + ".html");
    }

    public String getHtmlPath() {
        return htmlPath;
    }

    public void setHtmlPath(String htmlPath) {
        this.htmlPath = htmlPath;
    }

    public String getDocPath() {
        return docPath;
    }

    public void setDocPath(String docPath) {
        this.docPath = docPath;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public int getImgWidth() {
        return imgWidth;
    }

    public void setImgWidth(int imgWidth) {
        this.imgWidth = imgWidth / 2;
        imgBegin = "<img src=\"%s\" width=" + this.imgWidth + "px height=\"auto\">";
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    public TableIterator getTableIterator() {
        return tableIterator;
    }

    public void setTableIterator(TableIterator tableIterator) {
        this.tableIterator = tableIterator;
    }

    public int getPresentPicture() {
        return presentPicture;
    }

    public void setPresentPicture(int presentPicture) {
        this.presentPicture = presentPicture;
    }

    public FileOutputStream getOutput() {
        return output;
    }

    public void setOutput(FileOutputStream output) {
        this.output = output;
    }

    public String getHtmlBegin() {
        return htmlBegin;
    }

    public void setHtmlBegin(String htmlBegin) {
        this.htmlBegin = htmlBegin;
    }

    public String getHtmlEnd() {
        return htmlEnd;
    }

    public void setHtmlEnd(String htmlEnd) {
        this.htmlEnd = htmlEnd;
    }

    public String getTableBegin() {
        return tableBegin;
    }

    public void setTableBegin(String tableBegin) {
        this.tableBegin = tableBegin;
    }

    public String getTableEnd() {
        return tableEnd;
    }

    public void setTableEnd(String tableEnd) {
        this.tableEnd = tableEnd;
    }

    public String getRowBegin() {
        return rowBegin;
    }

    public void setRowBegin(String rowBegin) {
        this.rowBegin = rowBegin;
    }

    public String getRowEnd() {
        return rowEnd;
    }

    public void setRowEnd(String rowEnd) {
        this.rowEnd = rowEnd;
    }

    public String getColumnBegin() {
        return columnBegin;
    }

    public void setColumnBegin(String columnBegin) {
        this.columnBegin = columnBegin;
    }

    public String getColumnEnd() {
        return columnEnd;
    }

    public void setColumnEnd(String columnEnd) {
        this.columnEnd = columnEnd;
    }

    public String getLineBegin() {
        return lineBegin;
    }

    public void setLineBegin(String lineBegin) {
        this.lineBegin = lineBegin;
    }

    public String getLineEnd() {
        return lineEnd;
    }

    public void setLineEnd(String lineEnd) {
        this.lineEnd = lineEnd;
    }

    public String getCenterBegin() {
        return centerBegin;
    }

    public void setCenterBegin(String centerBegin) {
        this.centerBegin = centerBegin;
    }

    public String getCenterEnd() {
        return centerEnd;
    }

    public void setCenterEnd(String centerEnd) {
        this.centerEnd = centerEnd;
    }

    public String getBoldBegin() {
        return boldBegin;
    }

    public void setBoldBegin(String boldBegin) {
        this.boldBegin = boldBegin;
    }

    public String getBoldEnd() {
        return boldEnd;
    }

    public void setBoldEnd(String boldEnd) {
        this.boldEnd = boldEnd;
    }

    public String getUnderlineBegin() {
        return underlineBegin;
    }

    public void setUnderlineBegin(String underlineBegin) {
        this.underlineBegin = underlineBegin;
    }

    public String getUnderlineEnd() {
        return underlineEnd;
    }

    public void setUnderlineEnd(String underlineEnd) {
        this.underlineEnd = underlineEnd;
    }

    public String getItalicBegin() {
        return italicBegin;
    }

    public void setItalicBegin(String italicBegin) {
        this.italicBegin = italicBegin;
    }

    public String getItalicEnd() {
        return italicEnd;
    }

    public void setItalicEnd(String italicEnd) {
        this.italicEnd = italicEnd;
    }

    public String getFontSizeTag() {
        return fontSizeTag;
    }

    public void setFontSizeTag(String fontSizeTag) {
        this.fontSizeTag = fontSizeTag;
    }

    public String getFontColorTag() {
        return fontColorTag;
    }

    public void setFontColorTag(String fontColorTag) {
        this.fontColorTag = fontColorTag;
    }

    public String getFontEnd() {
        return fontEnd;
    }

    public void setFontEnd(String fontEnd) {
        this.fontEnd = fontEnd;
    }

    public String getSpanColor() {
        return spanColor;
    }

    public void setSpanColor(String spanColor) {
        this.spanColor = spanColor;
    }

    public String getSpanEnd() {
        return spanEnd;
    }

    public void setSpanEnd(String spanEnd) {
        this.spanEnd = spanEnd;
    }

    public String getDivRight() {
        return divRight;
    }

    public void setDivRight(String divRight) {
        this.divRight = divRight;
    }

    public String getDivEnd() {
        return divEnd;
    }

    public void setDivEnd(String divEnd) {
        this.divEnd = divEnd;
    }

    public String getImgBegin() {
        return imgBegin;
    }

    public void setImgBegin(String imgBegin) {
        this.imgBegin = imgBegin;
    }
}
