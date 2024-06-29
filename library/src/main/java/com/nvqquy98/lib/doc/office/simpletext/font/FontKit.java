/*
 * 文件名称:          FontKit.java
 *  
 * 编译器:            android2.2
 * 时间:              下午5:12:46
 */
package com.nvqquy98.lib.doc.office.simpletext.font;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.nvqquy98.lib.doc.office.common.PaintKit;
import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Cell;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Workbook;
import com.nvqquy98.lib.doc.office.ss.model.style.CellStyle;
import com.nvqquy98.lib.doc.office.ss.model.table.SSTableCellStyle;

import android.graphics.Paint;
import android.graphics.Typeface;


/**
 * 布局绘制用到的与字体相关的方法
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-11-8
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class FontKit
{   
    //
    private static FontKit fontKit = new FontKit();
    
    /**
     * 
     * @return
     */
    public static FontKit instance()
    {
        return fontKit;
    }
    
    /**
     * 
     * @param s
     * @param p
     * @return
     */
    public Paint getCellPaint(Cell cell, Workbook wb, SSTableCellStyle tableCellStyle)
    {   
        //Paint paint = new Paint();
        Paint paint = PaintKit.instance().getPaint();            
        paint.setAntiAlias(true);
        CellStyle s = cell.getCellStyle();
        Font font = wb.getFont(s.getFontIndex());
        boolean isbold = font.isBold();//getBoldweight() > HSSFFont.BOLDWEIGHT_NORMAL;
        boolean isitalics = font.isItalic();
        // 精斜体
        if (isbold && isitalics)
        {
            paint.setTextSkewX(-0.2f);
            paint.setFakeBoldText(true);
        }
        // 粗体
        else if (isbold)
        {
            paint.setFakeBoldText(true);
        }
        // 斜体
        else if (isitalics)
        {
            paint.setTextSkewX(-0.2f);
        }
        
        //Strike
        if(font.isStrikeline())
        {
            paint.setStrikeThruText(true);
        }
        
        //underline        
        if(font.getUnderline() != Font.U_NONE)
        {
            paint.setUnderlineText(true);
        }
        
        // 字符样式
        paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL));
        // fontsize
        paint.setTextSize((float)(font.getFontSize() * MainConstant.POINT_TO_PIXEL + 0.5f));
        // color
        int color = wb.getColor(font.getColorIndex());
        if((color & 0xFFFFFF) == 0 && tableCellStyle != null)
        {
            color = tableCellStyle.getFontColor();
        }
        paint.setColor(color);
        
        return paint;
    }
    
    /**
     * 查找换行时的断词点
     * 
     * @param text
     * @param startPos
     * @return
     */
    public synchronized int findBreakOffset(String text, int pos)
    {
        lineBreak.setText(text);
        /*char ch = text.charAt(pos);
        while (ch == 0x20 || ch == 0x3000)
        {
            pos--;
            if (pos >= 0)
            {
                ch = text.charAt(pos);
            }
        }*/
        // 如果一行只有一个单，则返回布局点位置
        //int newPos = wordBreak.following(pos - 1);
        lineBreak.following(pos);
        int newPos = lineBreak.previous();
        return newPos == 0 ? pos : newPos;
    }
    
    public List<String> breakText(String content, int lineWidth, Paint paint)
    {
        String[] words = content.split("\\n");
        List<String> textList = new ArrayList<String>();
        
        int index = 0;
        List<String> item;
        while(index < words.length)
        {
            item = wrapText(words[index], lineWidth, paint);
            Iterator<String> iter = item.iterator();
            while(iter.hasNext())
            {
                textList.add(iter.next());
            }
            index++;
        }
        
        return textList;
    }
    /**
     * ignore char differences between word
     * @param content
     * @param lineWidth
     * @param paint
     * @return
     */
    public List<String> wrapText(String content, int lineWidth, Paint paint)
    {
        String item = "";
        String restContent = content.substring(0);
        String[] words = restContent.split(" ");        
        List<String> textList = new ArrayList<String>();
        
        int wordIndex = 0;
        int charIndex = 0;
        char[] chars;
        while(wordIndex < words.length)
        {
            if(words[wordIndex].length() == 0)
            {
                words[wordIndex] = " ";
            }
            wordIndex++;
        }
        
        wordIndex = 0;        
        while(wordIndex < words.length)
        {           
           //one word width larger than linewidth
           while(paint.measureText(words[wordIndex]) > lineWidth)
           {
               chars = words[wordIndex].toCharArray();
               charIndex = chars.length;
               item = words[wordIndex].substring(0, charIndex);
               while(charIndex > 0 && paint.measureText(item) > lineWidth)
               {
                   charIndex--;
                   item = words[wordIndex].substring(0, charIndex);
               }
               textList.add(item);
               words[wordIndex] = words[wordIndex].substring(charIndex, words[wordIndex].length());
           }
           
           //made one line
           item = "";
           while(wordIndex < words.length && paint.measureText(item + words[wordIndex]) <= lineWidth)
           {
               item += words[wordIndex] + " ";
               wordIndex++;
           }
           textList.add(item.substring(0, item.length() - 1));
        }
       
        disposeString(words);
        return textList;
    } 
    
    /**
     * 
     * @param stringArray
     */
    private void disposeString(String[] stringArray)
    {
        int index = 0;
        while(index < stringArray.length)
        {
            stringArray[index] = null;
            index++;
        }
        stringArray = null;
    }
    
    // 断词、断行算法
    private BreakIterator lineBreak = BreakIterator.getLineInstance();
    
}
