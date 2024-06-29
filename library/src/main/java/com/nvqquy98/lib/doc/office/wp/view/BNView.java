/*
 * 文件名称:          BNView.java
 *  
 * 编译器:            android2.2
 * 时间:              上午9:33:36
 */
package com.nvqquy98.lib.doc.office.wp.view;

import com.nvqquy98.lib.doc.office.common.bulletnumber.ListData;
import com.nvqquy98.lib.doc.office.common.bulletnumber.ListKit;
import com.nvqquy98.lib.doc.office.common.bulletnumber.ListLevel;
import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPViewConstant;
import com.nvqquy98.lib.doc.office.simpletext.model.AttrManage;
import com.nvqquy98.lib.doc.office.simpletext.model.IDocument;
import com.nvqquy98.lib.doc.office.simpletext.model.IElement;
import com.nvqquy98.lib.doc.office.simpletext.model.Style;
import com.nvqquy98.lib.doc.office.simpletext.model.StyleManage;
import com.nvqquy98.lib.doc.office.simpletext.view.AbstractView;
import com.nvqquy98.lib.doc.office.simpletext.view.CharAttr;
import com.nvqquy98.lib.doc.office.simpletext.view.DocAttr;
import com.nvqquy98.lib.doc.office.simpletext.view.PageAttr;
import com.nvqquy98.lib.doc.office.simpletext.view.ParaAttr;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

/**
 * bullet and number view
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-6-20
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class BNView extends AbstractView/* implements IMemObj*/
{

    /**
     * 
     */
    public BNView()
    {
        charAttr = new CharAttr();
        paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
    }
    
    /**
     * 
     */
    public short getType()
    {
        return WPViewConstant.BN_VIEW;
    }
    
    /**
     * 
     * @param doc
     * @param docAttr
     * @param pageAttr
     * @param paraAttr
     * @param para
     * @param startOffset
     * @param x
     * @param y
     * @param w
     * @param h
     * @param flag
     */
    public synchronized int doLayout(IDocument doc, DocAttr docAttr, PageAttr pageAttr, ParaAttr paraAttr,
        ParagraphView para, int x, int y, int w, int h, int flag)
    {
        setLocation(paraAttr.listAlignIndent + x, y);
        int breakType = WPViewConstant.BREAK_NO;
        IElement paraElem = para.getElement();
        IElement leafElem = null;
        
        String text = "";
        if (paraAttr.listID >= 0)
        {
            ListData listData = para.getControl().getSysKit().getListManage().getListData(paraAttr.listID);
            if (listData == null)
            {
                return breakType;
            }
            if (listData.getLinkStyleID() >= 0)
            {
                Style style = StyleManage.instance().getStyle(listData.getLinkStyleID());
                if (style != null)
                {
                    int listID = AttrManage.instance().getParaListID(style.getAttrbuteSet());
                    listData = para.getControl().getSysKit().getListManage().getListData(listID);
                    if (listData == null || listData.getLevels().length == 0)
                    {
                        return breakType;
                    }
                }
            }
            leafElem =  doc.getLeaf(paraElem.getEndOffset() - 1);
            ListLevel listLevel = listData.getLevel(paraAttr.listLevel);        
            text = ListKit.instance().getBulletText(listData, listLevel, docAttr, paraAttr.listLevel);
            int preParaLevel = docAttr.rootType == WPViewConstant.NORMAL_ROOT ? 
                listData.getNormalPreParaLevel() : listData.getPreParaLevel(); 
            //
            if (paraAttr.listLevel < preParaLevel)
            {
                // 大于当前级别的listLevel的paraCount 置 0 
                for (int i = paraAttr.listLevel + 1; i < 9; i++)
                {
                    if (docAttr.rootType == WPViewConstant.NORMAL_ROOT)
                    {
                        listData.getLevel(i).setNormalParaCount(0);
                    }
                    else
                    {
                        listData.getLevel(i).setParaCount(0);
                    }
                }
            }
            else if(paraAttr.listLevel > preParaLevel)
            {
                // 在当前级别与前一个级别之间的 paraCount 也需要加 1
                for (int i = preParaLevel + 1; i < paraAttr.listLevel; i++)
                {
                    ListLevel temp = listData.getLevel(i);
                    if (docAttr.rootType == WPViewConstant.NORMAL_ROOT)
                    {
                        temp.setNormalParaCount(temp.getNormalParaCount() + 1);
                    }
                    else
                    {
                        temp.setParaCount(temp.getParaCount() + 1);
                    }
                }
            }
            // set previous paragraph count
            //listData.setPreParaLevel(paraAttr.listLevel);
            //listLevel.setParaCount(listLevel.getParaCount() + 1);
            if (docAttr.rootType == WPViewConstant.NORMAL_ROOT)
            {
                listLevel.setNormalParaCount(listLevel.getNormalParaCount() + 1);
                listData.setNormalPreParaLevel(paraAttr.listLevel);
            }
            else
            {
                listLevel.setParaCount(listLevel.getParaCount() + 1);
                listData.setPreParaLevel(paraAttr.listLevel);
            }
            currLevel = listLevel;
        }
        // PowerPoint bullet and number
        else if (paraAttr.pgBulletID >= 0)
        {
            leafElem = doc.getLeaf(paraElem.getStartOffset());
            text =  para.getControl().getSysKit().getPGBulletText().getBulletText(paraAttr.pgBulletID);
            if (text == null)
            {
                text = "";
            }
        }
        
        AttrManage.instance().fillCharAttr(charAttr, paraElem.getAttribute(), leafElem.getAttribute());
        // 粗斜体
        if (charAttr.isBold && charAttr.isItalic)
        {
            paint.setTextSkewX(-0.2f);
            paint.setFakeBoldText(true);
        }
        // 粗体
        else if (charAttr.isBold)
        {
            paint.setFakeBoldText(true);
        }
        // 斜体
        else if (charAttr.isItalic)
        {
            paint.setTextSkewX(-0.25f);
        }
        // 字体没有什么好改变的，用统一的吧
        paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.NORMAL));
        // 字号
        paint.setTextSize(charAttr.fontSize * (charAttr.fontScale / 100.f) * MainConstant.POINT_TO_PIXEL);
        // 颜色 
        paint.setColor(charAttr.fontColor);
        //paint.setColor(Color.BLACK);
        
        float[] widths = new float[text.length()];
        paint.getTextWidths(text, widths);
        float tW = 0;
        for (int i = 0; i < widths.length; i++)
        {
            tW += widths[i];
        }
        int ex = (int)((tW + getX()) % MainConstant.DEFAULT_TAB_WIDTH_PIXEL);
        if (ex > 0)
        {
            tW += (MainConstant.DEFAULT_TAB_WIDTH_PIXEL - ex);
        }
        //
        setSize((int)tW, (int)Math.ceil((paint.descent() - paint.ascent())));
        //
        content = text;
        return breakType;
    }
    /**
     * 
     * @param canvas
     * @param x
     * @param y
     * @param zoom
     */
    public void draw(Canvas canvas, int originX, int originY, float zoom)
    {
        int dX = (int)(x * zoom) + originX;
        int dY = (int)(y * zoom) + originY;
        
        if (content != null && content instanceof String)
        {
            String text = (String)content;
            float oldFontSize = paint.getTextSize();
            // 如果是上下标，则字号 / 2
            paint.setTextSize((charAttr.subSuperScriptType > 0 ? oldFontSize / 2 :  oldFontSize ) * zoom);
            canvas.drawText(text, 0, text.length(), dX, dY - paint.ascent(), paint);
            
            paint.setTextSize(oldFontSize);
        }
    }
    
    /**
     * 得到基线
     */
    public int getBaseline()
    {        
        return (int)-paint.ascent();
    }
    
    /**
     * 
     *
     * /
    public IMemObj getCopy()
    {
        return new BNView();
    }
    
    /**
     * 
     */
    public void free()
    {
        //ViewFactory.bnView.free(this);
    }
    
    /**
     * 
     */
    public synchronized void dispose()
    {
        content = null;
        paint = null;
        charAttr = null;
        if (currLevel != null)
        {
            currLevel.setParaCount(currLevel.getParaCount() - 1);
        }
    }
    
    // 显示文本
    private Object content;
    //
    private Paint paint;
    //
    private CharAttr charAttr;
    //
    private ListLevel currLevel;
}
