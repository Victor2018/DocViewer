/*
 * 文件名称:          DOCReader.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:57:34
 */

package com.nvqquy98.lib.doc.office.fc.doc;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nvqquy98.lib.doc.bean.DocSourceType;
import com.nvqquy98.lib.doc.office.common.PaintKit;
import com.nvqquy98.lib.doc.office.common.autoshape.ExtendPath;
import com.nvqquy98.lib.doc.office.common.autoshape.pathbuilder.ArrowPathAndTail;
import com.nvqquy98.lib.doc.office.common.bg.BackgroundAndFill;
import com.nvqquy98.lib.doc.office.common.bg.Gradient;
import com.nvqquy98.lib.doc.office.common.bg.LinearGradientShader;
import com.nvqquy98.lib.doc.office.common.bg.RadialGradientShader;
import com.nvqquy98.lib.doc.office.common.bg.TileShader;
import com.nvqquy98.lib.doc.office.common.bookmark.Bookmark;
import com.nvqquy98.lib.doc.office.common.borders.Border;
import com.nvqquy98.lib.doc.office.common.borders.Borders;
import com.nvqquy98.lib.doc.office.common.borders.Line;
import com.nvqquy98.lib.doc.office.common.bulletnumber.ListData;
import com.nvqquy98.lib.doc.office.common.bulletnumber.ListLevel;
import com.nvqquy98.lib.doc.office.common.hyperlink.Hyperlink;
import com.nvqquy98.lib.doc.office.common.picture.Picture;
import com.nvqquy98.lib.doc.office.common.pictureefftect.PictureEffectInfoFactory;
import com.nvqquy98.lib.doc.office.common.shape.AbstractShape;
import com.nvqquy98.lib.doc.office.common.shape.Arrow;
import com.nvqquy98.lib.doc.office.common.shape.GroupShape;
import com.nvqquy98.lib.doc.office.common.shape.IShape;
import com.nvqquy98.lib.doc.office.common.shape.LineShape;
import com.nvqquy98.lib.doc.office.common.shape.PictureShape;
import com.nvqquy98.lib.doc.office.common.shape.ShapeTypes;
import com.nvqquy98.lib.doc.office.common.shape.WPAbstractShape;
import com.nvqquy98.lib.doc.office.common.shape.WPAutoShape;
import com.nvqquy98.lib.doc.office.common.shape.WPGroupShape;
import com.nvqquy98.lib.doc.office.common.shape.WPPictureShape;
import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPAttrConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPModelConstant;
import com.nvqquy98.lib.doc.office.fc.FCKit;
import com.nvqquy98.lib.doc.office.fc.ShapeKit;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherContainerRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherOptRecord;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherProperties;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherSimpleProperty;
import com.nvqquy98.lib.doc.office.fc.ddf.EscherTextboxRecord;
import com.nvqquy98.lib.doc.office.fc.hwpf.HWPFDocument;
import com.nvqquy98.lib.doc.office.fc.hwpf.model.FieldsDocumentPart;
import com.nvqquy98.lib.doc.office.fc.hwpf.model.ListFormatOverride;
import com.nvqquy98.lib.doc.office.fc.hwpf.model.ListTables;
import com.nvqquy98.lib.doc.office.fc.hwpf.model.POIListData;
import com.nvqquy98.lib.doc.office.fc.hwpf.model.POIListLevel;
import com.nvqquy98.lib.doc.office.fc.hwpf.model.PicturesTable;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.Bookmarks;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.BorderCode;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.CharacterRun;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.Field;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.HWPFAutoShape;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.HWPFShape;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.HWPFShapeGroup;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.HeaderStories;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.InlineWordArt;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.LineSpacingDescriptor;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.OfficeDrawing;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.OfficeDrawings;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.POIBookmark;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.Paragraph;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.PictureType;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.Range;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.Section;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.Table;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.TableCell;
import com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.TableRow;
import com.nvqquy98.lib.doc.office.fc.util.LittleEndian;
import com.nvqquy98.lib.doc.office.java.awt.Rectangle;
import com.nvqquy98.lib.doc.office.java.util.Arrays;
import com.nvqquy98.lib.doc.office.simpletext.font.FontTypefaceManage;
import com.nvqquy98.lib.doc.office.simpletext.model.AttrManage;
import com.nvqquy98.lib.doc.office.simpletext.model.IAttributeSet;
import com.nvqquy98.lib.doc.office.simpletext.model.IElement;
import com.nvqquy98.lib.doc.office.simpletext.model.LeafElement;
import com.nvqquy98.lib.doc.office.simpletext.model.ParagraphElement;
import com.nvqquy98.lib.doc.office.simpletext.model.SectionElement;
import com.nvqquy98.lib.doc.office.simpletext.view.PageAttr;
import com.nvqquy98.lib.doc.office.ss.util.ModelUtil;
import com.nvqquy98.lib.doc.office.system.AbstractReader;
import com.nvqquy98.lib.doc.office.system.IControl;
import com.nvqquy98.lib.doc.office.wp.model.CellElement;
import com.nvqquy98.lib.doc.office.wp.model.HFElement;
import com.nvqquy98.lib.doc.office.wp.model.RowElement;
import com.nvqquy98.lib.doc.office.wp.model.TableElement;
import com.nvqquy98.lib.doc.office.wp.model.WPDocument;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Path;
import android.graphics.PointF;
import android.net.Uri;

/**
 * 处理doc文档
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-12-31
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class DOCReader extends AbstractReader
{
    public DOCReader(IControl control, String filePath,int docSourceType)
    {
        this.control = control;
        this.filePath = filePath;
        this.docSourceType = docSourceType;
        //controlForReader = control;
    }

    /**
     * 
     */
    public Object getModel() throws Exception
    {
        if (wpdoc != null)
        {
            return wpdoc;
        }
        wpdoc = new WPDocument();
        processDoc();
        return wpdoc;
    }

    /**
     * 处理doc文档
     */
    private void processDoc() throws Exception
    {
        InputStream is = null;
        switch (docSourceType) {
            case DocSourceType.URL:
                URL url = new URL(filePath);
                is = url.openStream();
                break;
            case DocSourceType.URI:
                Uri uri = Uri.parse(filePath);
                is = control.getActivity().getContentResolver().openInputStream(uri);
                break;
            case DocSourceType.PATH:
                is = new FileInputStream(filePath);
                break;
            case DocSourceType.ASSETS:
                is = control.getActivity().getAssets().open(filePath);
                break;
        }

        poiDoc = new HWPFDocument(is);
        
        //
        processBulletNumber();
        //
        processBookmark();
        //
        offset = WPModelConstant.MAIN;
        docRealOffset = WPModelConstant.MAIN;
        Range range = poiDoc.getRange();
        int numSection = range.numSections();
        for (int i = 0; i < numSection && !abortReader; i++)
        {
            // poi的section，后期需要修改
            processSection(range.getSection(i));
            if (isBreakChar)
            {
                IElement elem = wpdoc.getLeaf(offset - 1);
                if (elem != null && elem instanceof LeafElement)
                {
                    String s = elem.getText(wpdoc);
                    if (s != null && s.length() == 1
                        && s.charAt(0) == '\f')
                    {
                        ((LeafElement)elem).setText(String.valueOf('\n'));
                    }
                }
            }
        }
        //
        processHeaderFooter();
    }
    
    /**
     * 
     */
    private void processBookmark()
    {
        Bookmarks bks = poiDoc.getBookmarks();
        if (bks != null)
        {
            for (int i = 0; i < bks.getBookmarksCount(); i++)
            {
                POIBookmark poiBM = bks.getBookmark(i);
                Bookmark bm = new Bookmark(poiBM.getName(), poiBM.getStart(), poiBM.getEnd());
                control.getSysKit().getBookmarkManage().addBookmark(bm);
                bms.add(bm);
            }
        }
    }
    
    /**
     * 
     */
    private void processBulletNumber()
    {
        ListTables listTables = poiDoc.getListTables();
        if (listTables == null)
        {
            return;
        }
        int size = listTables.getOverrideCount();
        for (int i = 0; i < size; i++)
        {
            ListData listData = new ListData();
            
            POIListData poiData = listTables.getListData(listTables.getOverride(i + 1).getLsid());
            if(poiData == null)
            {
            	continue;
            }
            
            // list ID
            listData.setListID(poiData.getLsid());
            // list level
            POIListLevel[] levels = poiData.getLevels();
            int len = levels.length;
            ListLevel[] listLevels =  new ListLevel[len];
            for (int j = 0; j < len; j++)
            {
                listLevels[j] = new ListLevel();
                processListLevel(levels[j], listLevels[j]);
            }
            listData.setLevels(listLevels);
            // simpleLevel
            listData.setSimpleList((byte)len);
            
            control.getSysKit().getListManage().putListData(listData.getListID(), listData);
        }
    }
    
    /**
     * 
     */
    private void processListLevel(POIListLevel level, ListLevel listLevel)
    {
        // start at
        listLevel.setStartAt(level.getStartAt());
        // horizontal alignment;
        listLevel.setAlign((byte)level.getAlignment());
        // follow char
        listLevel.setFollowChar(level.getTypeOfCharFollowingTheNumber());
        // number format
        listLevel.setNumberFormat(level.getNumberFormat());
        // number text
        listLevel.setNumberText(converterNumberChar(level.getNumberChar()));
        // special indent, default 21 POINT
        listLevel.setSpecialIndent(level.getSpecialIndnet());
        // left text indent, default 21 point * level
        listLevel.setTextIndent(level.getTextIndent());
    }
    
    /**
     * 
     */
    private char[] converterNumberChar(char[] numChar)
    {
        if (numChar == null)
        {
            return null;
        }
        for (int i = 0; i < numChar.length; i++)
        {
            if (numChar[i] == 0xF06C)
            {
                numChar[i] = 0x25CF;
            }
            else if (numChar[i] == 0xF06E)
            {
                numChar[i] = 0x25A0;
            } 
            else if (numChar[i] == 0xF075)
            {
                numChar[i] = 0x25C6;
            }  
            else if (numChar[i] == 0xF0FC)
            {
                numChar[i] = 0x221A;
            }
            else if (numChar[i] == 0xF0D8)
            {
                numChar[i] = 0x2605;
            }
            else if (numChar[i] == 0xF0B2)
            {
                numChar[i] = 0x2606;
            }
            else if (numChar[i] >= 0xF060)
            {
                numChar[i] = 0x25CF;
            }
        }
        return numChar;
    }
    
    /**
     * 
     * @param section
     */
    private void processSection(Section section)
    {
        // 建立章节
        SectionElement secElem = new SectionElement();
        // 属性
        IAttributeSet attr = secElem.getAttribute();
        // 宽度 default a4 paper
        AttrManage.instance().setPageWidth(attr, section.getPageWidth());//11906); 
        // 高度 default a4 paper
        AttrManage.instance().setPageHeight(attr, section.getPageHeight());//16838);
        // 左边距 default a4 paper
        AttrManage.instance().setPageMarginLeft(attr, section.getMarginLeft());//1800);
        // 右边距 default a4 paper
        AttrManage.instance().setPageMarginRight(attr, section.getMarginRight());//1800);
        // 上边距 default a4 paper
        AttrManage.instance().setPageMarginTop(attr, section.getMarginTop());//1440)
        // 下边距 default a4 paper
        AttrManage.instance().setPageMarginBottom(attr, section.getMarginBottom());//1440);
        // 页眉高度
        AttrManage.instance().setPageHeaderMargin(attr, section.getMarginHeader());//850);
        // 页脚高度
        AttrManage.instance().setPageFooterMargin(attr, section.getMarginFooter());
        //网格类型，高度
        if(section.getGridType() != PageAttr.GRIDTYPE_NONE)
        {
        	AttrManage.instance().setPageLinePitch(attr, section.getLinePitch());
        }
        processSectionBorder(secElem, section);
        // 开始Offset
        secElem.setStartOffset(offset);
        //
        int paraCount = section.numParagraphs();
        for (int i = 0; i < paraCount && !abortReader; i++)
        {
            Paragraph para = section.getParagraph(i);
            if (para.isInTable())
            {
                Table table = section.getTable(para);
                processTable(table);
                i += table.numParagraphs() - 1;
                continue;
            }
            processParagraph(section.getParagraph(i));
        }
        // 结束Offset
        secElem.setEndOffset(offset);
        wpdoc.appendSection(secElem);
    }
    
    /**
     * 
     */
    private void processSectionBorder(SectionElement secElem, Section section)
    {
        BorderCode top = section.getTopBorder();
        BorderCode bottom = section.getBottomBorder();
        BorderCode left = section.getLeftBorder();
        BorderCode right = section.getRightBorder();
        if (top != null || bottom != null
            || left != null || right != null)
        {
            byte pageBorderInfo = (byte)section.getPageBorderInfo();
            byte offsetFrom = (byte)(pageBorderInfo >> 5 & 0x07);
            //int depth = pageBorderInfo >> 3 & 0x03;
            //int applyTo = pageBorderInfo >> 5 & 0x07;
            Borders borders = new Borders();
            borders.setOnType((byte)offsetFrom);
            Border border;
            // top
            if (top != null)
            {
                border = new Border();
                border.setColor(top.getColor() == 0 ? Color.BLACK : converterColorForIndex(top.getColor()));
                border.setSpace((short)(top.getSpace() * MainConstant.POINT_TO_PIXEL));
                borders.setTopBorder(border);
            }
            // bottom
            if (bottom != null)
            {
                border = new Border();
                border.setColor(bottom.getColor() == 0 ? Color.BLACK : converterColorForIndex(bottom.getColor()));
                border.setSpace((short)(bottom.getSpace() * MainConstant.POINT_TO_PIXEL));
                borders.setBottomBorder(border);
            }
            // left
            if (left != null)
            {
                border = new Border();
                border.setColor(left.getColor() == 0 ? Color.BLACK : converterColorForIndex(left.getColor()));
                border.setSpace((short)(left.getSpace() * MainConstant.POINT_TO_PIXEL));
                borders.setLeftBorder(border);
            }
            // right
            if (right != null)
            {
                border = new Border();
                border.setColor(right.getColor() == 0 ? Color.BLACK : converterColorForIndex(right.getColor()));
                border.setSpace((short)(right.getSpace() * MainConstant.POINT_TO_PIXEL));
                borders.setRightBorder(border);
            }
            AttrManage.instance().setPageBorder(secElem.getAttribute(), control.getSysKit().getBordersManage().addBorders(borders));
        }
        
    }
    
    /**
     * 
     */
    private void processHeaderFooter()
    {
        HeaderStories hs = new HeaderStories(poiDoc);
        // header
        offset = WPModelConstant.HEADER;
        docRealOffset = WPModelConstant.HEADER;
        // first page
        Range range;
        /* range = hs.getFirstHeaderSubrange();
        if (range != null)
        {
            processHeaderFooterPara(range, WPModelConstant.HEADER_ELEMENT, WPModelConstant.HF_FIRST);
        }*/
        // odd page
        range = hs.getOddHeaderSubrange();
        if (range != null)
        {
            processHeaderFooterPara(range, WPModelConstant.HEADER_ELEMENT, WPModelConstant.HF_ODD);
        }
        // even page
        /*range = hs.getEvenHeaderSubrange();
        if (range != null)
        {
            processHeaderFooterPara(range, WPModelConstant.HEADER_ELEMENT, WPModelConstant.HF_ODD);
        }*/
        
        // footer
        offset = WPModelConstant.FOOTER;
        docRealOffset = WPModelConstant.FOOTER;
        // first page 
        /*range = hs.getFirstFooterSubrange();
        if (range != null)
        {
            processHeaderFooterPara(range, WPModelConstant.FOOTER_ELEMENT, WPModelConstant.HF_FIRST);
        }*/
        // odd page
        range = hs.getOddFooterSubrange();
        if (range != null)
        {
            processHeaderFooterPara(range, WPModelConstant.FOOTER_ELEMENT, WPModelConstant.HF_ODD);
        }
        // even page
        /*range = hs.getEvenFooterSubrange();
        if (range != null)
        {
            processHeaderFooterPara(range, WPModelConstant.FOOTER_ELEMENT, WPModelConstant.HF_ODD);
        }*/
    }
    
    /**
     * 
     */
    private void processHeaderFooterPara(Range range, short elemType, byte hfType)
    {
        IElement elem = new HFElement(elemType, hfType);
        elem.setStartOffset(offset);
        int paraCount = range.numParagraphs();
        for (int i = 0; i < paraCount && !abortReader; i++)
        {   
            Paragraph para = range.getParagraph(i);
            if (para.isInTable())
            {
                Table table = range.getTable(para);
                processTable(table);
                i += table.numParagraphs() - 1;
                continue;
            }
            processParagraph(para);
        }
        // 结束Offset
        elem.setEndOffset(offset);
        //
        wpdoc.appendElement(elem, offset);
    }
    
    /**
     * 处理 表格
     * @param table
     */
    private void processTable(Table table)
    {
        TableElement tableElem = new TableElement();
        tableElem.setStartOffset(offset);
        Vector<Integer> dxs = new Vector<Integer>();
        // row
        int rowNum = table.numRows();        
        for (int i = 0; i < rowNum; i++)
        {
            TableRow tableRow = table.getRow(i);
            // table attributes
            if (i == 0)
            {
                processTableAttribute(tableRow, tableElem.getAttribute());
            }
            RowElement rowElem = new RowElement();
            rowElem.setStartOffset(offset);
            // row attribute 
            processRowAttribute(tableRow, rowElem.getAttribute());            
            // cell
            int numCell = tableRow.numCells();
            int dx = 0;
            for (int j = 0; j < numCell; j++)
            {
                TableCell cell = tableRow.getCell(j);
                cell.isBackward();
                CellElement cellElem = new CellElement();                
                cellElem.setStartOffset(offset);
                // cell attribute
                processCellAttribute(cell, cellElem.getAttribute());
                // paragraph
                int numPara = cell.numParagraphs();
                for (int k = 0; k < numPara; k++)
                {
                    processParagraph(cell.getParagraph(k));
                }
                cellElem.setEndOffset(offset);
                if(offset > cellElem.getStartOffset())
                {
                	rowElem.appendCell(cellElem);
                }
                
                dx += cell.getWidth();
                //
                if (!dxs.contains(dx))
                {
                    dxs.add(dx);
                }
            }
            rowElem.setEndOffset(offset);
            if(offset > rowElem.getStartOffset())
            {
            	tableElem.appendRow(rowElem);
            }
        }
        tableElem.setEndOffset(offset);
        if(offset > tableElem.getStartOffset())
        {
        	wpdoc.appendParagraph(tableElem, offset);
        	
        	// 计算水平合并单元的跨度        
            int size = dxs.size();
            int[] maxDx = new int[size];
            for (int i = 0; i < size; i++)
            {
                maxDx[i] = dxs.get(i);
            }
            Arrays.sort(maxDx);
            
            int rowIndex = 0;
            RowElement row = (RowElement)tableElem.getElementForIndex(rowIndex++);
            while (row != null)
            {
                int cellIndex = 0;
                IElement cell = row.getElementForIndex(cellIndex);
                int i = 0;
                int dx = 0;
                while (cell != null)
                {
                    dx += AttrManage.instance().getTableCellWidth(cell.getAttribute());
                    for (; i < size; i++)
                    {
                        if (dx > maxDx[i])
                        {                            
                            row.insertElementForIndex(new CellElement(), cellIndex + 1);                            
                            cellIndex++;
                        }
                        else
                        {
                            i++;
                            break;
                        }
                    }
                    
                    cellIndex++;
                    cell = row.getElementForIndex(cellIndex);
                }
                row = (RowElement)tableElem.getElementForIndex(rowIndex++);
            }
        }
    }
    
    
    /**
     * process table attribute
     */
    private void processTableAttribute(TableRow row, IAttributeSet attr)
    {
        // table horizontal alignment
        if (row.getRowJustification() != WPAttrConstant.PARA_HOR_ALIGN_LEFT)
        {
            AttrManage.instance().setParaHorizontalAlign(attr, row.getRowJustification());
        }
        if (row.getTableIndent() != 0)
        {
            AttrManage.instance().setParaIndentLeft(attr, row.getTableIndent());
        }
    }
    
    /**
     * process table row attribute 
     */
    private void processRowAttribute(TableRow row, IAttributeSet attr)
    {
        // row height
        if (row.getRowHeight() != 0)
        {
            AttrManage.instance().setTableRowHeight(attr, row.getRowHeight());
        }
        // table header
        if (row.isTableHeader())
        {
            AttrManage.instance().setTableHeaderRow(attr, true);
        }
        // table split
        if (row.cantSplit())
        {
            AttrManage.instance().setTableRowSplit(attr, true);
        }
        // top border
        /*BorderCode border = row.getTopBorder();
        if (border != null)
        {
            AttrManage.instance().setTableTopBorder(attr, 1);
            AttrManage.instance().setTableTopBorderColor(attr, 
                converterColorForIndex(border.getColor()));
        }
        // bottom border
        border = row.getBottomBorder();
        if (border != null)
        {
            AttrManage.instance().setTableBottomBorder(attr, 1);
            AttrManage.instance().setTableBottomBorderColor(attr, 
                converterColorForIndex(border.getColor()));
        }
        // left border
        border = row.getLeftBorder();
        if (border != null)
        {
            AttrManage.instance().setTableLeftBorder(attr, 1);
            AttrManage.instance().setTableLeftBorderColor(attr, 
                converterColorForIndex(border.getColor()));
        }
        // right border
        border = row.getRightBorder();
        if (border != null)
        {
            AttrManage.instance().setTableRightBorder(attr, 1);
            AttrManage.instance().setTableRightBorderColor(attr, 
                converterColorForIndex(border.getColor()));
        }*/
    }
    
    /**
     * 
     */
    private void processCellAttribute(TableCell cell, IAttributeSet attr)
    {
        // first horizontal merged
        if (cell.isFirstMerged())
        {
            AttrManage.instance().setTableHorFirstMerged(attr, true);
        }
        // horizontal merged
        if (cell.isMerged())
        {
            AttrManage.instance().setTableHorMerged(attr, true);
        }
        // first vertical merged
        if (cell.isFirstVerticallyMerged())
        {
            AttrManage.instance().setTableVerFirstMerged(attr, true);
        }
        // vertical merged
        if (cell.isVerticallyMerged())
        {
            AttrManage.instance().setTableVerMerged(attr, true);
        }
        // vertical alignment
        AttrManage.instance().setTableCellVerAlign(attr, cell.getVertAlign());
        // cell width
        AttrManage.instance().setTableCellWidth(attr, cell.getWidth());
        
        /*// 上边距
        int margin = cell.getDescriptor().getWCellPaddingTop();
        if (margin != 0)
        {
            AttrManage.instance().setTableTopMargin(attr, margin);
        }
        // 下边距
        margin = cell.getDescriptor().getWCellPaddingBottom();
        if (margin != 0)
        {
            AttrManage.instance().setTableBottomMargin(attr, margin);
        }
        
        // 左边距
        margin = cell.getDescriptor().getWCellPaddingLeft();
        if (margin != 0)
        {
            AttrManage.instance().setTableLeftMargin(attr, margin);
        }
        
        // 右边距
        margin = cell.getDescriptor().getWCellPaddingRight();
        if (margin != 0)
        {
            AttrManage.instance().setTableRightMargin(attr, margin);
        }*/
        
        // top border
        /*BorderCode border = cell.getBrcTop();
        if (border != null)
        {
            AttrManage.instance().setTableTopBorder(attr, 1);
            AttrManage.instance().setTableTopBorderColor(attr, 
                converterColorForIndex(border.getColor()));
        }
        // bottom border
        border = cell.getBrcBottom();
        if (border != null)
        {
            AttrManage.instance().setTableBottomBorder(attr, 1);
            AttrManage.instance().setTableBottomBorderColor(attr, 
                converterColorForIndex(border.getColor()));
        }
        // left border
        border = cell.getBrcLeft();
        if (border != null)
        {
            AttrManage.instance().setTableLeftBorder(attr, 1);
            AttrManage.instance().setTableLeftBorderColor(attr, 
                converterColorForIndex(border.getColor()));
        }
        // right border
        border = cell.getBrcRight();
        if (border != null)
        {
            AttrManage.instance().setTableRightBorder(attr, 1);
            AttrManage.instance().setTableRightBorderColor(attr, 
                converterColorForIndex(border.getColor()));
        }*/
    }
    
    /**
     * 
     */
    private void processParagraph(Paragraph para)
    {
        ParagraphElement paraElem = new ParagraphElement();
        // 属性
        IAttributeSet attr = paraElem.getAttribute();
        // 段前
        AttrManage.instance().setParaBefore(attr, para.getSpacingBefore());
        // 段后
        AttrManage.instance().setParaAfter(attr, para.getSpacingAfter());
        // 左缩进
        AttrManage.instance().setParaIndentLeft(attr, para.getIndentFromLeft());
        // 右缩进
        AttrManage.instance().setParaIndentRight(attr, para.getIndentFromRight());
        // 水平对齐
        AttrManage.instance().setParaHorizontalAlign(attr, converterParaHorAlign(para.getJustification()));
        // 垂直对齐
        AttrManage.instance().setParaVerticalAlign(attr, para.getFontAlignment());
        // 特殊缩进
        converterSpecialIndent(attr, para.getFirstLineIndent());
        // 行距
        converterLineSpace(para.getLineSpacing(), attr);
        // list level
        if (para.getIlfo() > 0)
        {
            ListTables listTables = poiDoc.getListTables();
            if (listTables != null)
            {
                // list ID
            	ListFormatOverride listFormatOverride = listTables.getOverride(para.getIlfo());
            	if(listFormatOverride != null)
            	{
            		AttrManage.instance().setParaListID(attr, listFormatOverride.getLsid());
            	}
                
                // list level
                AttrManage.instance().setParaListLevel(attr, para.getIlvl());
            }
        }
        if (para.isInTable())
        {
            AttrManage.instance().setParaLevel(attr, para.getTableLevel());
        }
        // 开始 offset
        paraElem.setStartOffset(offset);
        
        int runCount = para.numCharacterRuns();
        boolean isFieldCode = false;
        boolean isFieldText = false;
        Field field = null;
        CharacterRun preRun;
        CharacterRun run = null;
        String fieldCode ="";
        String fieldText ="";
        long before = docRealOffset;
        for (int i = 0; i < runCount && !abortReader; i++)
        {
            preRun = run;
            run = para.getCharacterRun(i);
            String text = run.text();
            if (text.length() == 0 || run.isMarkedDeleted())
            {
                continue;
            }
            docRealOffset += text.length();
            char ch = text.charAt(0);
            char lastch = text.charAt(text.length() - 1);
            if ((ch == '\t' && text.length() == 1)
                || ch == 0x05 // 批注框
                /*|| ch == 0x01*/) // 嵌入对象占位符
            {
                continue;
            }
            else if (ch == 0x13 || lastch == 0x13) // 域开始符 
            {
            	if(ch != 0x15 || lastch != 0x13)
            	{
            		long area = offset & WPModelConstant.AREA_MASK;
                    FieldsDocumentPart fieldsPart = area == WPModelConstant.HEADER || area == WPModelConstant.FOOTER
                        ? fieldsPart = FieldsDocumentPart.HEADER : FieldsDocumentPart.MAIN;
                    field = poiDoc.getFields().getFieldByStartOffset(fieldsPart, (int)run.getStartOffset());
                    isFieldCode = true;
            	}                
                continue;
            }
            else if (ch == 0x14 || lastch == 0x14) // 域分隔符
            {
                isFieldCode = false;
                isFieldText = true;
                continue;
            }
            else if (ch == 0x15 || lastch == 0x15) // 域结束符
            {
                if (preRun != null && fieldText != null
                    && field != null && field.getType() == Field.EMBED)
                {
                	//EMBED object
                	if (fieldText.indexOf("EQ") >= 0 &&
                            fieldText.indexOf("jc") >= 0)
                	{
                		processRun(preRun, para, field, paraElem, fieldCode, fieldText);
                	}
                	else
                	{
                		if(lastch == 0x15)
                		{
                			fieldText += text.substring(0, text.length() - 1);
                		}
                		processRun(run, para, field, paraElem, fieldCode, fieldText);
                	}
                }
                else if (isPageNumber(field, fieldCode))
                {
                	processRun(run, para, field, paraElem, fieldCode, fieldText);
                }
                
                isFieldText = false;
                isFieldCode = false;
                field = null;                
                hyperlinkAddress = null;
                fieldCode = "";
                fieldText = "";
                continue;
            }
            if (isFieldCode)
            {
            	fieldCode += run.text();
                continue;
            }
            
            if(isFieldText && isPageNumber(field, fieldCode))
            {
            	fieldText += run.text();
            	continue;
            }
            
            processRun(run, para, field, paraElem, null, null);
            //fieldText = "";
        }
        if (para.getTabClearPosition() > 0)
        {
            AttrManage.instance().setParaTabsClearPostion(attr, para.getTabClearPosition());
        }
        
        // 结束 offset        
        if(offset == paraElem.getStartOffset())
        {
        	//this paragraph is not Marked Deleted
        	paraElem.dispose();
        	paraElem = null;
        	
        	return;
        }

        paraElem.setEndOffset(offset);
        wpdoc.appendParagraph(paraElem, offset);
        //
        adjustBookmarkOffset(before, docRealOffset);
    }
    
    private boolean isPageNumber(Field field, String fieldCode)
    {
    	if(field != null && (field.getType() ==  Field.PAGE || field.getType() == Field.NUMPAGES))
    	{
    		return true;
    	}
    	else if( fieldCode != null && (fieldCode.contains("NUMPAGES") || fieldCode.contains("PAGE")))
    	{
    		return true;
        }
    	
    	return false;
    }
    
    /**
     * 先后顺序：
     * 1、如果出现了sprmPDxcLeft，则取该数据作为左缩进字符单位，数据格式为浮点数乘以100；
     * 2、如果没有出现sprmPDxcLeft记录，则取sprmPDxaleft数据作为左缩进，数据格式为浮点数乘以20；
     * 3、如果前两条记录都没出现，则取sprmPDxaLeft80作为左缩进，数据格式为浮点数乘以20；
     * 其他缩进方式参照这个执行
     */
    private void converterSpecialIndent(IAttributeSet attr, int firstLintIndent)
    {
        /*
         * 特殊缩进
         * 规则：firstLineIndent > 0，表示首行缩进
         *       firstLineIndent < 0，表示悬挂缩进
         */
        AttrManage.instance().setParaSpecialIndent(attr, firstLintIndent);
        //  悬挂缩进值也设置到左缩进，左缩进需要减去悬挂缩进
        if (firstLintIndent< 0)
        {
            AttrManage.instance().setParaIndentLeft(attr, 
                AttrManage.instance().getParaIndentLeft(attr) + firstLintIndent);
        }
        
    }
    
    /**
     * 行距规则
     * 如果 _fMultiLinespace = 1，表示为倍数，用_dyaLine / 240就是设置倍数
     *      _fMultiLinespace = 0，表示为非倍数，
     *      如果 _dyaLine > 0，表示为最小值，用_dyaLine / 20 就是设置的最小值，单位磅
     *           _dyaLine < 0，表示为固定值，用_dyaLine / 20 就是设置的固定值，单位磅
     */
    private void converterLineSpace(LineSpacingDescriptor sd, IAttributeSet attr)
    { 
        int lineSpaceType = WPAttrConstant.LINE_SPACE_SINGLE;
        float lineSpaceValue = 1;
        
        // 倍数
        if (sd.getMultiLinespace() == 1)
        {
            float t = sd.getDyaLine() / 240.0f; 
            if (t == 1)
            {
                lineSpaceType = WPAttrConstant.LINE_SPACE_SINGLE;
                lineSpaceValue = 1;
            }
            else if (t == 1.5)
            {
                lineSpaceType = WPAttrConstant.LINE_SPACE_ONE_HALF;
                lineSpaceValue = 1.5f;
            }
            else if (t == 2)
            {
                lineSpaceType = WPAttrConstant.LINE_SPACE_DOUBLE;
                lineSpaceValue = 2f;
            }
            else
            {
                lineSpaceType = WPAttrConstant.LINE_SPACE_DOUBLE;
                lineSpaceValue = t;
            }
        }
        // 设定值
        else
        {
            float t = sd.getDyaLine();
            if (t >= 0)
            {
                lineSpaceType = WPAttrConstant.LINE_SAPCE_LEAST;
                lineSpaceValue = t;
            }
            else
            {
                lineSpaceType = WPAttrConstant.LINE_SPACE_EXACTLY;
                lineSpaceValue = -t;
            }
        }
        // 行距
        AttrManage.instance().setParaLineSpace(attr, lineSpaceValue);
        // 行距类型
        AttrManage.instance().setParaLineSpaceType(attr, lineSpaceType);
    }
    
    /**
     * 转换段落对齐试
     * @param js
     * @return
     */
    private byte converterParaHorAlign(int js)
    {
        switch (js)
        {
            case 0:
            case 3:
            case 4:
            case 6:
            case 9:
            case 7:
                return WPAttrConstant.PARA_HOR_ALIGN_LEFT;
            case 1:
            case 5:
                return WPAttrConstant.PARA_HOR_ALIGN_CENTER;
            case 2:
            case 8:
                return WPAttrConstant.PARA_HOR_ALIGN_RIGHT;
            default:
                return WPAttrConstant.PARA_HOR_ALIGN_LEFT;
        }
    }

    /**
     * 
     */
    private void processRun(CharacterRun run, Range parentRange, Field field, ParagraphElement paraElem, String fieldCode, String fieldText)
    {
        String text = run.text();
        if (fieldText != null)
        {
        	text = fieldText;
        }
        
        if(text != null && text.length() > 0)
        {
        	// 嵌入对象占位符
            char ch = text.charAt(0);
            isBreakChar = ch == '\f';
            if (ch == '\b' || ch == 0x01)
            {
                for (int i = 0; i < text.length() && !run.isVanished(); i++)
                {
                    ch = text.charAt(i);
                    if (ch == '\b' || ch == 0x01)
                    {
                        LeafElement leaf = new LeafElement(String.valueOf(ch));
                        // process shape error
                        if (!processShape(run, leaf, ch == '\b', i))
                        {
                            return ;
                        }
                        // 开始 offset
                        leaf.setStartOffset(offset);
                        offset += 1;
                        // 结束 offset
                        leaf.setEndOffset(offset);
                        paraElem.appendLeaf(leaf);
                    }
                }
                return;
            }
        }        
        
        LeafElement leaf = new LeafElement(text);
        // 属性
        IAttributeSet attr = leaf.getAttribute();
        // 字号
        AttrManage.instance().setFontSize(attr, (int)(run.getFontSize() / 2.f + 0.5));
        // 字体
        int index =  FontTypefaceManage.instance().addFontName(run.getFontName());
        if (index >= 0)
        {
            AttrManage.instance().setFontName(attr, index);    
        }
        // 字符颜色
        AttrManage.instance().setFontColor(attr, FCKit.BGRtoRGB(run.getIco24()));
        // 粗体
        AttrManage.instance().setFontBold(attr, run.isBold());
        // 斜体
        AttrManage.instance().setFontItalic(attr, run.isItalic());
        // 删除线
        AttrManage.instance().setFontStrike(attr, run.isStrikeThrough());
        // 双删除线
        AttrManage.instance().setFontDoubleStrike(attr, run.isDoubleStrikeThrough());
        // 下划线
        AttrManage.instance().setFontUnderline(attr, run.getUnderlineCode());
        // 下划线颜色
        AttrManage.instance().setFontUnderlineColr(attr, FCKit.BGRtoRGB(run.getUnderlineColor()));
        // 上下标
        AttrManage.instance().setFontScript(attr, run.getSubSuperScriptIndex());
        // 高亮
        AttrManage.instance().setFontHighLight(attr, converterColorForIndex(run.getHighlightedColor()));

        if (field != null && field.getType() ==  Field.HYPERLINK)
        {
            // hyperlink
        	 if (hyperlinkAddress == null)
             {
                 Range firstSubrange = field.firstSubrange(parentRange);
                 if (firstSubrange != null)
                 {
                     String formula = firstSubrange.text();
                     Matcher matcher = hyperlinkPattern.matcher(formula);                        
                     if (matcher.find())
                     {
                         hyperlinkAddress = matcher.group(1);
                     }
                 }
             }
             if (hyperlinkAddress != null)
             {
                 index = control.getSysKit().getHyperlinkManage().addHyperlink(hyperlinkAddress, Hyperlink.LINK_URL);                    
                 if (index >= 0)
                 {
                     AttrManage.instance().setFontColor(attr, Color.BLUE);
                     AttrManage.instance().setFontUnderline(attr, 1);
                     AttrManage.instance().setFontUnderlineColr(attr, Color.BLUE);
                     AttrManage.instance().setHyperlinkID(attr, index);
                 }
             }
        }
        else if(fieldCode != null)
        {
        	if (fieldCode.indexOf("HYPERLINK") > 0)
            {
                index = fieldCode.indexOf("_Toc");
                if (index > 0)
                {
                    int endIndex = fieldCode.lastIndexOf('"');
                    if (endIndex > 0 && endIndex > index)
                    {
                        String bmName = fieldCode.substring(index, endIndex);
                        index = control.getSysKit().getHyperlinkManage().addHyperlink(bmName, Hyperlink.LINK_BOOKMARK);                    
                        if (index >= 0)
                        {
                            AttrManage.instance().setFontColor(attr, Color.BLUE);
                            AttrManage.instance().setFontUnderline(attr, 1);
                            AttrManage.instance().setFontUnderlineColr(attr, Color.BLUE);
                            AttrManage.instance().setHyperlinkID(attr, index);
                        }
                    }
                }
            }
        	else
        	{
        		long area = offset & WPModelConstant.AREA_MASK;
                if(area == WPModelConstant.HEADER || area == WPModelConstant.FOOTER)
                {
                	byte pageNumberType = -1;
                    if(fieldCode != null)
                    {
                    	if(fieldCode.contains("NUMPAGES"))
                        {
                        	pageNumberType = WPModelConstant.PN_TOTAL_PAGES;
                        }
                        else if(fieldCode.contains("PAGE"))
                        {
                        	pageNumberType = WPModelConstant.PN_PAGE_NUMBER;
                        }
                    }
                    
                    if(pageNumberType > 0)
                    {
                    	AttrManage.instance().setFontPageNumberType(leaf.getAttribute(), pageNumberType);
                    }                	
                }
        	}
        }
        
        // 开始 offset
        leaf.setStartOffset(offset);
        offset += text.length();
        // 结束 offset
        leaf.setEndOffset(offset);
        paraElem.appendLeaf(leaf);
    }

    
    /**
     * 
     */
    private int converterColorForIndex(short color)
    {    
        switch (color)
        {
            case 1:
                return Color.BLACK;
            case 2:
                return Color.BLUE;
            case 3:
                return Color.CYAN;
            case 4:
                return Color.GREEN;
            case 5:
                return Color.MAGENTA;
            case 6:
                return Color.RED;
            case 7:
                return Color.YELLOW;
            case 8:
                return Color.WHITE;
            case 9:
                return Color.BLUE;
            case 10:
                return Color.DKGRAY;
            case 11:
                return Color.GREEN;
            case 12:
                return Color.MAGENTA;
            case 13:
                return Color.RED;
            case 14:
                return Color.YELLOW;
            case 15:
                return Color.GRAY;
            case 16:
                return Color.LTGRAY;
            default:
                return -1;
        }
    }
    
    private BackgroundAndFill converFill(HWPFAutoShape shape, OfficeDrawing drawing, int shapeType)
    {
        if (shapeType == ShapeTypes.Line || shapeType == ShapeTypes.StraightConnector1
            || shapeType == ShapeTypes.BentConnector2 || shapeType == ShapeTypes.BentConnector3 
            || shapeType == ShapeTypes.CurvedConnector3)
        {
            return null;
        }
        
        BackgroundAndFill bgFill = null;
        if (shape != null)
        {
            int type = shape.getFillType();
            // 填充类型
            if (type == BackgroundAndFill.FILL_SOLID || type == BackgroundAndFill.FILL_BACKGROUND)
            {
                if (shape.getForegroundColor() != null)
                {
                    bgFill = new BackgroundAndFill();
                    bgFill.setFillType(BackgroundAndFill.FILL_SOLID);
                    // 前景颜色
                    bgFill.setForegroundColor(shape.getForegroundColor().getRGB());
                }
            }
            else if(type == BackgroundAndFill.FILL_SHADE_LINEAR || type == BackgroundAndFill.FILL_SHADE_RADIAL 
            		|| type == BackgroundAndFill.FILL_SHADE_RECT || type == BackgroundAndFill.FILL_SHADE_SHAPE)
            {
            	bgFill = new BackgroundAndFill();
            	int angle = shape.getFillAngle();
            	switch(angle)
        		{
        			case -90:
        			case 0:
        				angle += 90;
        				break;
        			case -45:
        				angle = 135;
        				break;
        			case -135:
        				angle = 45;
        				break;
        		}
            	int focus = shape.getFillFocus();
            	com.nvqquy98.lib.doc.office.java.awt.Color fillColor = shape.getForegroundColor();
            	com.nvqquy98.lib.doc.office.java.awt.Color fillbackColor = shape.getFillbackColor();
            	
            	int[] colors = null;
            	float[] positions = null; 
            	if(shape.isShaderPreset())
            	{
            		colors = shape.getShaderColors();
            		positions = shape.getShaderPositions();
            	}
            	
            	if(colors == null)
            	{
            		colors = new int[]{fillColor == null ? 0xFFFFFFFF : fillColor.getRGB(), 
                			fillbackColor == null ? 0xFFFFFFFF : fillbackColor.getRGB()};
            	}
            	if(positions == null)
            	{
            		positions = new float[]{0f, 1f};
            	}
            	
            	Gradient gradient = null; 
            	if(type == BackgroundAndFill.FILL_SHADE_LINEAR)
            	{
            		gradient = new LinearGradientShader(angle, colors, positions);
            	}
            	else if(type == BackgroundAndFill.FILL_SHADE_RADIAL 
                		|| type == BackgroundAndFill.FILL_SHADE_RECT 
                        || type == BackgroundAndFill.FILL_SHADE_SHAPE )
                {
        			gradient = 
        					new RadialGradientShader(shape.getRadialGradientPositionType(), colors, positions);
                }
            	
            	if(gradient != null)
            	{
            		gradient.setFocus(focus);
            	}
            	
            	bgFill.setFillType((byte)type);
            	bgFill.setShader(gradient);
            }
            else if(type == BackgroundAndFill.FILL_SHADE_TILE)
            {
            	// 背景为图片
                byte[] data = drawing.getPictureData(control, shape.getBackgroundPictureIdx());
                if (data != null && isSupportPicture(PictureType.findMatchingType(data)))
                {
                	bgFill = new BackgroundAndFill();
                    bgFill.setFillType(BackgroundAndFill.FILL_SHADE_TILE);
                    // 图片数据
                    int index = control.getSysKit().getPictureManage().getPictureIndex(drawing.getTempFilePath(control));
                    if (index < 0)
                    {
                        Picture picture = new Picture();
                        // 图片数据
                        picture.setTempFilePath(drawing.getTempFilePath(control));
                        // 图片类型
                        picture.setPictureType(PictureType.findMatchingType(data).getExtension());
                        index = control.getSysKit().getPictureManage().addPicture(picture);
                        bgFill.setShader(
                				new TileShader(control.getSysKit().getPictureManage().getPicture(index), 
                						TileShader.Flip_None, 1f, 1.0f));
                    }
                }            	
            }
            else if (type == BackgroundAndFill.FILL_PICTURE)
            {
                // 背景为图片
                byte[] data = drawing.getPictureData(control, shape.getBackgroundPictureIdx());
                if (data != null && isSupportPicture(PictureType.findMatchingType(data)))
                {
                    bgFill = new BackgroundAndFill();
                    bgFill.setFillType(BackgroundAndFill.FILL_PICTURE);
                    // 图片数据
                    int index = control.getSysKit().getPictureManage().getPictureIndex(drawing.getTempFilePath(control));
                    if (index < 0)
                    {
                        Picture picture = new Picture();
                        // 图片数据
                        picture.setTempFilePath(drawing.getTempFilePath(control));
                        // 图片类型
                        picture.setPictureType(PictureType.findMatchingType(data).getExtension());
                        index = control.getSysKit().getPictureManage().addPicture(picture);
                    }
                    bgFill.setPictureIndex(index);
                }
            }
            else if (type == BackgroundAndFill.FILL_PATTERN)
            {
   /*         	byte[] data = drawing.getBGPictureData(shape.getBackgroundPictureIdx());
                if (data != null  && isSupportPicture(PictureType.findMatchingType(data)))
                {
                	bgFill = new BackgroundAndFill();
                    bgFill.setFillType(BackgroundAndFill.FILL_PATTERN);
                    // 图片数据
                    int index = control.getSysKit().getPictureManage().getPictureIndex(drawing.getTempFilePath());
                    if (index < 0)
                    {
                    	control.getSysKit().getPictureManage().writeTempFile(data);
                    	
                        Picture picture = new Picture();
                        // 图片数据
                        picture.setTempFilePath(drawing.getTempFilePath());
                        // 图片类型
                        picture.setPictureType(PictureType.findMatchingType(data).getExtension());
                        index = control.getSysKit().getPictureManage().addPicture(picture);
                        bgFill.setShader(
                				new PatternShader(control.getSysKit().getPictureManage().getPicture(index), 
                						shape.getBackgroundColor().getRGB(), shape.getForegroundColor().getRGB()));
                    }
                }
                else*/ if (shape.getFillbackColor() != null)
                {
                    bgFill = new BackgroundAndFill();
                    bgFill.setFillType(BackgroundAndFill.FILL_SOLID);
                    // 前景颜色
                    bgFill.setForegroundColor(shape.getFillbackColor().getRGB());
                }
            }
        }
        return bgFill;
    }
    
    private void processRotation(HWPFAutoShape shape, IShape autoShape)
    {
        float angle = shape.getRotation();
        if (shape.getFlipHorizontal())
        {
            autoShape.setFlipHorizontal(true);
            angle = -angle;
        }
        if (shape.getFlipVertical())
        {
            autoShape.setFlipVertical(true);
            angle = -angle;
        }
        
        if(autoShape instanceof LineShape)
        {
            if((angle == 45 || angle == 135 || angle == 225)
                && !autoShape.getFlipHorizontal()
                && !autoShape.getFlipVertical())
            {
                angle -= 90;
            }
        }
        autoShape.setRotation(angle);
    }
    
    /**
     * 重新计算group shape中的child shape的位置
     * @param grpRect
     * @param offsetRect
     * @param rect
     * @return
     */
    private Rectangle processGrpSpRect(GroupShape parent, Rectangle rect)
    {
        if (parent != null)
        {
            rect.x += parent.getOffX();
            rect.y += parent.getOffY();
        }
        return rect;
    }
    
    /**
     * 
     * @param shape
     * @param autoShape
     */
    private void processAutoshapePosition(HWPFAutoShape shape, WPAutoShape autoShape)
    {
    	//horizontal alignment
    	switch(shape.getPosition_H())
    	{
	    	case HWPFShape.POSH_ABS:
	    		autoShape.setHorPositionType(WPAbstractShape.POSITIONTYPE_ABSOLUTE);
	    		break;
	    	case HWPFShape.POSH_LEFT:
	    		autoShape.setHorizontalAlignment(WPAbstractShape.ALIGNMENT_LEFT);
	    		break;
	    	case HWPFShape.POSH_CENTER:
	    		autoShape.setHorizontalAlignment(WPAbstractShape.ALIGNMENT_CENTER);
	    		break;
	    	case HWPFShape.POSH_RIGHT:
	    		autoShape.setHorizontalAlignment(WPAbstractShape.ALIGNMENT_RIGHT);
	    		break;
	    	case HWPFShape.POSH_INSIDE:
	    		autoShape.setHorizontalAlignment(WPAbstractShape.ALIGNMENT_INSIDE);
	    		break;
	    	case HWPFShape.POSH_OUTSIDE:
	    		autoShape.setHorizontalAlignment(WPAbstractShape.ALIGNMENT_OUTSIDE);
	    		break;
    	}
    	
    	//relative to in horizontal
    	switch(shape.getPositionRelTo_H())
    	{
	    	case HWPFShape.POSRELH_MARGIN:
	    		autoShape.setHorizontalRelativeTo(WPAbstractShape.RELATIVE_MARGIN);
	    		break;
	    	case HWPFShape.POSRELH_PAGE:
	    		autoShape.setHorizontalRelativeTo(WPAbstractShape.RELATIVE_PAGE);
	    		break;
	    	case HWPFShape.POSRELH_COLUMN:
	    		autoShape.setHorizontalRelativeTo(WPAbstractShape.RELATIVE_COLUMN);
	    		break;
	    	case HWPFShape.POSRELH_CHAR:
	    		autoShape.setHorizontalRelativeTo(WPAbstractShape.RELATIVE_CHARACTER);
	    		break;
    	}
    	
    	//alignment in vertical
    	switch(shape.getPosition_V())
    	{
	    	case HWPFShape.POSV_ABS:
	    		autoShape.setVerPositionType(WPAbstractShape.POSITIONTYPE_ABSOLUTE);
	    		break;
	    	case HWPFShape.POSV_TOP:
	    		autoShape.setVerticalAlignment(WPAbstractShape.ALIGNMENT_TOP);
	    		break;
	    	case HWPFShape.POSV_CENTER:
	    		autoShape.setVerticalAlignment(WPAbstractShape.ALIGNMENT_CENTER);
	    		break;
	    	case HWPFShape.POSV_BOTTOM:
	    		autoShape.setVerticalAlignment(WPAbstractShape.ALIGNMENT_BOTTOM);
	    		break;
	    	case HWPFShape.POSV_INSIDE:
	    		autoShape.setVerticalAlignment(WPAbstractShape.ALIGNMENT_INSIDE);
	    		break;
	    	case HWPFShape.POSV_OUTSIDE:
	    		autoShape.setVerticalAlignment(WPAbstractShape.ALIGNMENT_OUTSIDE);
	    		break;
    	}
    	
    	//relative to in vertical
    	switch(shape.getPositionRelTo_V())
    	{
	    	case HWPFShape.POSRELV_MARGIN:
	    		autoShape.setVerticalRelativeTo(WPAbstractShape.RELATIVE_MARGIN);
	    		break;
	    	case HWPFShape.POSRELV_PAGE:
	    		autoShape.setVerticalRelativeTo(WPAbstractShape.RELATIVE_PAGE);
	    		break;
	    	case HWPFShape.POSRELV_TEXT:
	    		autoShape.setVerticalRelativeTo(WPAbstractShape.RELATIVE_PARAGRAPH);
	    		break;
	    	case HWPFShape.POSRELV_LINE:
	    		autoShape.setVerticalRelativeTo(WPAbstractShape.RELATIVE_LINE);
	    		break;
    	}
    }
    
    private byte[] getPictureframeData(OfficeDrawing darwing, HWPFShape poiShape)
    {
    	EscherOptRecord escherOptRecord = 
    			poiShape.getSpContainer().getChildById(EscherOptRecord.RECORD_ID);
            if (escherOptRecord == null)
                return null;

            EscherSimpleProperty escherProperty = escherOptRecord
                .lookup(EscherProperties.BLIP__BLIPTODISPLAY);
            if (escherProperty == null)
                return null;

            int bitmapIndex = escherProperty.getPropertyValue();
            return darwing.getPictureData(control, bitmapIndex);
    }
    
    private boolean convertShape(IElement leaf, OfficeDrawing drawing, GroupShape parent, 
                    HWPFShape poiShape, Rectangle rect, float zoomX, float zoomY)
    {
        if (rect == null)
        {
            return false;
        }
        
        
        if (poiShape instanceof HWPFAutoShape)
        {
            HWPFAutoShape shape = (HWPFAutoShape)poiShape;
            int shapeType = shape.getShapeType();
            BackgroundAndFill fill = converFill(shape, drawing, shapeType);
            Line line = poiShape.getLine(shapeType == ShapeTypes.Line);
            if (line != null || fill != null || shapeType == ShapeTypes.TextBox || shapeType == ShapeTypes.PictureFrame)
            {
                rect = processGrpSpRect(parent, rect);
                
                WPAutoShape autoShape = null;
                if(shapeType == ShapeTypes.PictureFrame)
                {
                	autoShape = new WPPictureShape();
                }
                else
                {
                	autoShape = new WPAutoShape();
                }
                
                autoShape.setShapeType(shapeType);
                autoShape.setAuotShape07(false);               
                
                float angle = Math.abs(shape.getRotation());
                autoShape.setBounds(ModelUtil.processRect(rect, angle));
                
                autoShape.setBackgroundAndFill(fill);
                if (line != null)
                {
                    autoShape.setLine(line);
                }
                Float[] adj = shape.getAdjustmentValue();
                autoShape.setAdjustData(adj);
                processRotation(shape, autoShape);
                
                processAutoshapePosition(shape, autoShape);
                
                boolean isLineShape = false;
                if(shapeType == ShapeTypes.PictureFrame)
                {
                	byte[] b = getPictureframeData(drawing, shape);
                	if (b != null)
                    {
                        if (isSupportPicture(PictureType.findMatchingType(b)))
                        {
                            PictureShape picShape = new PictureShape();                
                            
                            int index = control.getSysKit().getPictureManage().getPictureIndex(drawing.getTempFilePath(control));
                            if (index < 0)
                            {
                                Picture picture = new Picture();
                                // 图片数据
                                picture.setTempFilePath(drawing.getTempFilePath(control));
                                // 图片类型
                                picture.setPictureType(PictureType.findMatchingType(b).getExtension());
                                index = control.getSysKit().getPictureManage().addPicture(picture);
                            }                
                            picShape.setPictureIndex(index);    
                            picShape.setBounds(rect);  
                            picShape.setZoomX((short)1000);
                            picShape.setZoomY((short)1000);
                            picShape.setPictureEffectInfor(drawing.getPictureEffectInfor());                            
                            ((WPPictureShape)autoShape).setPictureShape(picShape);
                        }
                    }
                }
                else if (shapeType == ShapeTypes.Line || shapeType == ShapeTypes.StraightConnector1
                    || shapeType == ShapeTypes.BentConnector2 || shapeType == ShapeTypes.BentConnector3 
                    || shapeType == ShapeTypes.CurvedConnector3)
                {
                	isLineShape = true;
                    if(autoShape.getShapeType() == ShapeTypes.BentConnector2 && adj == null)
                    {
                        autoShape.setAdjustData(new Float[]{1.0f});
                    }
                    
                    int type = shape.getStartArrowType();
                    if (type > 0)
                    {                        
                        autoShape.createStartArrow((byte)type,
                            shape.getStartArrowWidth(), 
                            shape.getStartArrowLength());
                    }
                    
                    type = shape.getEndArrowType();
                    if (type > 0)
                    {
                        autoShape.createEndArrow((byte)type,
                            shape.getEndArrowWidth(), 
                            shape.getEndArrowLength());
                    }                    
                }
                else if(shapeType == ShapeTypes.NotPrimitive || shapeType == ShapeTypes.NotchedCircularArrow)
                {
                	isLineShape = true;
                    autoShape.setShapeType(ShapeTypes.ArbitraryPolygon);
                    
                    PointF startArrowTailCenter = null;
                    PointF endArrowTailCenter = null;
                    
                    int startArrowType = shape.getStartArrowType();
                    if (startArrowType > 0)
                    {
                    	ArrowPathAndTail arrowPathAndTail = ((HWPFAutoShape)shape).getStartArrowPath(rect);
                    	 if(arrowPathAndTail != null && arrowPathAndTail.getArrowPath() != null)
                         {
                         	startArrowTailCenter = arrowPathAndTail.getArrowTailCenter();
                             ExtendPath pathExtend = new ExtendPath();
                             pathExtend.setPath(arrowPathAndTail.getArrowPath());
                             pathExtend.setArrowFlag(true);
                             if(startArrowType != Arrow.Arrow_Arrow)
                             {
                                 if((line == null || line.getBackgroundAndFill() == null)  && poiShape.getLineColor() != null)
                                 {
                                 	BackgroundAndFill arrowFill = new BackgroundAndFill();
                                 	arrowFill.setFillType(BackgroundAndFill.FILL_SOLID);
                                 	arrowFill.setForegroundColor(poiShape.getLineColor().getRGB());
                                     pathExtend.setBackgroundAndFill(arrowFill);
                                 }
                                 else
                                 {                            	
                                 	pathExtend.setBackgroundAndFill(line.getBackgroundAndFill());
                                 }
                             }
                             else
                             {
                             	pathExtend.setLine(line);
                             }
                             
                             autoShape.appendPath(pathExtend);
                         }
                        
                    }
                    
                    int endArrowType = shape.getEndArrowType();
                    if (endArrowType > 0)
                    {
                    	ArrowPathAndTail arrowPathAndTail = ((HWPFAutoShape)shape).getEndArrowPath(rect);
                    	if(arrowPathAndTail != null && arrowPathAndTail.getArrowPath() != null)
                        {
                        	endArrowTailCenter = arrowPathAndTail.getArrowTailCenter();
                            ExtendPath pathExtend = new ExtendPath();
                            pathExtend.setPath(arrowPathAndTail.getArrowPath());
                        	
                            pathExtend.setArrowFlag(true);
                            if(endArrowType != Arrow.Arrow_Arrow)
                            {
                            	if((line == null || line.getBackgroundAndFill() == null)  && poiShape.getLineColor() != null)
                                {
                                	BackgroundAndFill arrowFill = new BackgroundAndFill();
                                	arrowFill.setFillType(BackgroundAndFill.FILL_SOLID);
                                	arrowFill.setForegroundColor(poiShape.getLineColor().getRGB());
                                    pathExtend.setBackgroundAndFill(arrowFill);
                                }
                                else
                                {                            	
                                	pathExtend.setBackgroundAndFill(line.getBackgroundAndFill());
                                }
                            }
                            else
                            {
                            	pathExtend.setLine(line);
                            }
                            
                            autoShape.appendPath(pathExtend);
                        }
                        
                    }
                    
                    Path[] paths = shape.getFreeformPath(rect, 
                    		startArrowTailCenter, (byte)startArrowType, endArrowTailCenter, (byte)endArrowType);
                    for (int i = 0; i < paths.length; i++)
                    {
                        ExtendPath pathExtend = new ExtendPath();
                        pathExtend.setPath(paths[i]);
                        if (line != null)
                        {
                        	pathExtend.setLine(line);
                        }
                        if (fill != null)
                        {
                            pathExtend.setBackgroundAndFill(fill);
                        }
                        autoShape.appendPath(pathExtend);
                    }
                }
                else
                {
                	processTextbox(shape.getSpContainer(),
            				autoShape, 
                			poiDoc.getMainTextboxRange().getSection(0));
                }
                
                if (parent == null)
                {
                    // wrap
                    if (drawing.getWrap() == 3 && !drawing.isAnchorLock())
                    {
                        if(drawing.isBelowText())
                        {
                            autoShape.setWrap(WPAutoShape.WRAP_BOTTOM);
                        }
                        else
                        {
                            autoShape.setWrap(WPAutoShape.WRAP_TOP);
                            fill = autoShape.getBackgroundAndFill();
                        }                        
                    }
                    else
                    {
                        autoShape.setWrap(WPAutoShape.WRAP_OLE);
                    }
                    AttrManage.instance().setShapeID(leaf.getAttribute(), control.getSysKit().getWPShapeManage().addShape(autoShape));
                    return true;
                }
                else
                {
                    parent.appendShapes(autoShape);
                    return false;
                }
            }
        }
        else if (poiShape instanceof HWPFShapeGroup)
        {
            HWPFShapeGroup poiGroup = (HWPFShapeGroup)poiShape;
            
            AbstractShape shape = null;
            WPGroupShape groupShape = new WPGroupShape();
            if (parent == null)
            {
                shape = new WPAutoShape();
                ((WPAutoShape)shape).addGroupShape(groupShape);
            }
            else
            {
                shape = groupShape;
            }
            
            float zoom[] = poiGroup.getShapeAnchorFit(rect, zoomX, zoomY);    
            rect = processGrpSpRect(parent, rect);
            Rectangle childRect = poiGroup.getCoordinates(zoom[0] * zoomX, zoom[1] * zoomY);
            groupShape.setOffPostion(rect.x - childRect.x, rect.y - childRect.y);
            
            groupShape.setBounds(rect);
            groupShape.setParent(parent);
            groupShape.setRotation(poiGroup.getGroupRotation());
            groupShape.setFlipHorizontal(poiGroup.getFlipHorizontal());
            groupShape.setFlipVertical(poiGroup.getFlipVertical());
            
            HWPFShape[] shapes = poiGroup.getShapes();
            if (shapes != null)
            {
                for (int i = 0; i < shapes.length; i++)
                {
                    convertShape(leaf, drawing, groupShape, shapes[i], shapes[i].getAnchor(rect, zoom[0] * zoomX, zoom[1] * zoomY), zoom[0] * zoomX, zoom[1] * zoomY);
                }
            }
            
            if (parent == null)
            {
                // wrap
                if (drawing.getWrap() == 3 && !drawing.isAnchorLock())
                {
                    ((WPAutoShape)shape).setWrap(WPAutoShape.WRAP_TOP);
                }
                else
                {
                    ((WPAutoShape)shape).setWrap(WPAutoShape.WRAP_OLE);
                }
                AttrManage.instance().setShapeID(leaf.getAttribute(), control.getSysKit().getWPShapeManage().addShape(shape));
            }
            else
            {
                shape.setParent(parent);
                parent.appendShapes(shape);
            }
            return true;
        }
        return false;
    }
    
    /**
     * textbox id based on 1
     * @return
     */
    private short getTextboxId(EscherContainerRecord escherContainer)
    {
    	 EscherTextboxRecord escherTextboxRecord = escherContainer
                 .getChildById(EscherTextboxRecord.RECORD_ID);
    	 if(escherTextboxRecord != null)
    	 {
    		 byte[] data = escherTextboxRecord.getData();
    		 if(data != null && data.length == 4)
    		 {
    			 return LittleEndian.getShort(data, 2);
    		 }
    	 }
    	 
    	 return -1;
    }
    
    /**
     * 
     * @param autoShape
     * @param section
     * @param startPara start with startPara-th paragraph
     * @param endPara not contain end para
     */
    private void processTextbox(EscherContainerRecord escherContainer, WPAutoShape autoShape, Section section)
    {
    	if(section ==  null)
    	{
    		return;
    	}
    	
    	//based on 1
    	int txbx = getTextboxId(escherContainer) - 1;
    	if(txbx >= 0)
    	{
    		processSimpleTextBox(escherContainer, autoShape, section);
    	}
    	else
    	{
    		processWordArtTextbox(escherContainer, autoShape);
    	}
    }
    
    private void processSimpleTextBox(EscherContainerRecord escherContainer, WPAutoShape autoShape, Section section)
    {
    	int txbx = getTextboxId(escherContainer) - 1;
    	
    	int startCP = poiDoc.getTextboxStart(txbx);
    	int endCP = poiDoc.getTextboxEnd(txbx);
		
    	long oldOffset = offset;
        offset = WPModelConstant.TEXTBOX + (textboxIndex << 32);
        autoShape.setElementIndex((int)textboxIndex);
        SectionElement textboxElement = new SectionElement();
        textboxElement.setStartOffset(offset);
        wpdoc.appendElement(textboxElement, offset);
        
        // 属性
        IAttributeSet attr = textboxElement.getAttribute();
        // 宽度 default a4 paper
        AttrManage.instance().setPageWidth(attr, (int)(autoShape.getBounds().width * MainConstant.PIXEL_TO_TWIPS));
        // 高度 default a4 paper
        AttrManage.instance().setPageHeight(attr, (int)(autoShape.getBounds().height * MainConstant.PIXEL_TO_TWIPS));

        //网格类型，高度
        if(section.getGridType() != PageAttr.GRIDTYPE_NONE)
        {
        	AttrManage.instance().setPageLinePitch(attr, section.getLinePitch());
        }
    	
    	// 上边距
        AttrManage.instance().setPageMarginTop(attr, (int)(ShapeKit.getTextboxMarginTop(escherContainer) * MainConstant.PIXEL_TO_TWIPS));
        // 下边距
        AttrManage.instance().setPageMarginBottom(attr, (int)(ShapeKit.getTextboxMarginBottom(escherContainer) * MainConstant.PIXEL_TO_TWIPS));
        // 左边距
        AttrManage.instance().setPageMarginLeft(attr, (int)(ShapeKit.getTextboxMarginLeft(escherContainer) * MainConstant.PIXEL_TO_TWIPS));
        // 右边距
        AttrManage.instance().setPageMarginRight(attr, (int)(ShapeKit.getTextboxMarginRight(escherContainer) * MainConstant.PIXEL_TO_TWIPS));
        
        AttrManage.instance().setPageVerticalAlign(attr, WPAttrConstant.PAGE_V_TOP);
        
        autoShape.setTextWrapLine(ShapeKit.isTextboxWrapLine(escherContainer));
        
        // 开始Offset
        textboxElement.setStartOffset(offset);
        //
        int paraCount = section.numParagraphs();
        int charOffset = 0;
        for (int i = 0; i < paraCount && !abortReader; i++)
        {   
            Paragraph para = section.getParagraph(i);
            charOffset += para.text().length();
            if(charOffset > startCP && charOffset <= endCP)
            {
            	if (para.isInTable())
                {
                    Table table = section.getTable(para);
                    processTable(table);
                    i += table.numParagraphs() - 1;
                    continue;
                }
                processParagraph(section.getParagraph(i));
            }
        }
        autoShape.setElementIndex((int)textboxIndex);
        
        // 结束Offset        
        textboxElement.setEndOffset(offset);
        textboxIndex++;
        offset = oldOffset;	
    }
    
    private void processWordArtTextbox(EscherContainerRecord escherContainer, WPAutoShape autoShape)
    {
    	String text = ShapeKit.getUnicodeGeoText(escherContainer);
		if(text != null && text.length() > 0)
		{
        	long oldOffset = offset;
            offset = WPModelConstant.TEXTBOX + (textboxIndex << 32);
            autoShape.setElementIndex((int)textboxIndex);
            SectionElement textboxElement = new SectionElement();
            textboxElement.setStartOffset(offset);
            wpdoc.appendElement(textboxElement, offset);
            
            // 属性
            IAttributeSet attr = textboxElement.getAttribute();
            // 宽度 default a4 paper
            AttrManage.instance().setPageWidth(attr, (int)(autoShape.getBounds().width * MainConstant.PIXEL_TO_TWIPS));
            // 高度 default a4 paper
            AttrManage.instance().setPageHeight(attr, (int)(autoShape.getBounds().height * MainConstant.PIXEL_TO_TWIPS));
        	
        	// 上边距
            AttrManage.instance().setPageMarginTop(attr, (int)(ShapeKit.getTextboxMarginTop(escherContainer) * MainConstant.PIXEL_TO_TWIPS));
            // 下边距
            AttrManage.instance().setPageMarginBottom(attr, (int)(ShapeKit.getTextboxMarginBottom(escherContainer) * MainConstant.PIXEL_TO_TWIPS));
            // 左边距
            AttrManage.instance().setPageMarginLeft(attr, (int)(ShapeKit.getTextboxMarginLeft(escherContainer) * MainConstant.PIXEL_TO_TWIPS));
            // 右边距
            AttrManage.instance().setPageMarginRight(attr, (int)(ShapeKit.getTextboxMarginRight(escherContainer) * MainConstant.PIXEL_TO_TWIPS));
            
            AttrManage.instance().setPageVerticalAlign(attr, WPAttrConstant.PAGE_V_TOP);
            
            autoShape.setTextWrapLine(ShapeKit.isTextboxWrapLine(escherContainer));
            
            int width = (int)(autoShape.getBounds().width - ShapeKit.getTextboxMarginLeft(escherContainer) - ShapeKit.getTextboxMarginRight(escherContainer));
            int height = (int)(autoShape.getBounds().height - ShapeKit.getTextboxMarginTop(escherContainer) - ShapeKit.getTextboxMarginBottom(escherContainer));
            int fontsize = 12;
        	Paint paint = PaintKit.instance().getPaint();
        	paint.setTextSize(fontsize);
        	FontMetrics fm = paint.getFontMetrics();
        	while((int)paint.measureText(text) < width && (int)(Math.ceil(fm.descent - fm.ascent)) < height)
        	{	        		
        		paint.setTextSize(++fontsize);
        		fm = paint.getFontMetrics();
        	}
        	
            // 开始Offset
            textboxElement.setStartOffset(offset);
            //
            ParagraphElement paraElem = new ParagraphElement();                
            // 开始 offset
            paraElem.setStartOffset(offset);
            long before = docRealOffset;

            LeafElement leaf = new LeafElement(text);
            // 属性
            IAttributeSet leafAttr = leaf.getAttribute();
            // 字号
            AttrManage.instance().setFontSize(leafAttr, (int)((fontsize - 1) * MainConstant.PIXEL_TO_POINT));            
            // 字符颜色
            com.nvqquy98.lib.doc.office.java.awt.Color color = ShapeKit.getForegroundColor(escherContainer, null, MainConstant.APPLICATION_TYPE_PPT);
            if(color != null)
            {
            	AttrManage.instance().setFontColor(leafAttr, color.getRGB());
            }
            
            
            // 开始 offset
            leaf.setStartOffset(offset);
            offset += text.length();
            // 结束 offset
            leaf.setEndOffset(offset);
            paraElem.appendLeaf(leaf);

            paraElem.setEndOffset(offset);
            wpdoc.appendParagraph(paraElem, offset);
            //
            adjustBookmarkOffset(before, docRealOffset);

            autoShape.setElementIndex((int)textboxIndex);
            
            // 结束Offset        
            textboxElement.setEndOffset(offset);
            textboxIndex++;
            offset = oldOffset;		
		} 
    }
    
    private void processPicturePosition(OfficeDrawing drawing, WPAutoShape autoShape)
    {
    	//horizontal alignment
    	switch(drawing.getHorizontalPositioning())
    	{
	    	case HWPFShape.POSH_ABS:
	    		autoShape.setHorPositionType(WPAbstractShape.POSITIONTYPE_ABSOLUTE);
	    		break;
	    	case HWPFShape.POSH_LEFT:
	    		autoShape.setHorizontalAlignment(WPAbstractShape.ALIGNMENT_LEFT);
	    		break;
	    	case HWPFShape.POSH_CENTER:
	    		autoShape.setHorizontalAlignment(WPAbstractShape.ALIGNMENT_CENTER);
	    		break;
	    	case HWPFShape.POSH_RIGHT:
	    		autoShape.setHorizontalAlignment(WPAbstractShape.ALIGNMENT_RIGHT);
	    		break;
	    	case HWPFShape.POSH_INSIDE:
	    		autoShape.setHorizontalAlignment(WPAbstractShape.ALIGNMENT_INSIDE);
	    		break;
	    	case HWPFShape.POSH_OUTSIDE:
	    		autoShape.setHorizontalAlignment(WPAbstractShape.ALIGNMENT_OUTSIDE);
	    		break;
    	}
    	
    	//relative to in horizontal
    	switch(drawing.getHorizontalRelative())
    	{
	    	case HWPFShape.POSRELH_MARGIN:
	    		autoShape.setHorizontalRelativeTo(WPAbstractShape.RELATIVE_MARGIN);
	    		break;
	    	case HWPFShape.POSRELH_PAGE:
	    		autoShape.setHorizontalRelativeTo(WPAbstractShape.RELATIVE_PAGE);
	    		break;
	    	case HWPFShape.POSRELH_COLUMN:
	    		autoShape.setHorizontalRelativeTo(WPAbstractShape.RELATIVE_COLUMN);
	    		break;
	    	case HWPFShape.POSRELH_CHAR:
	    		autoShape.setHorizontalRelativeTo(WPAbstractShape.RELATIVE_CHARACTER);
	    		break;
    	}
    	
    	//alignment in vertical
    	switch(drawing.getVerticalPositioning())
    	{
	    	case HWPFShape.POSV_ABS:
	    		autoShape.setVerPositionType(WPAbstractShape.POSITIONTYPE_ABSOLUTE);
	    		break;
	    	case HWPFShape.POSV_TOP:
	    		autoShape.setVerticalAlignment(WPAbstractShape.ALIGNMENT_TOP);
	    		break;
	    	case HWPFShape.POSV_CENTER:
	    		autoShape.setVerticalAlignment(WPAbstractShape.ALIGNMENT_CENTER);
	    		break;
	    	case HWPFShape.POSV_BOTTOM:
	    		autoShape.setVerticalAlignment(WPAbstractShape.ALIGNMENT_BOTTOM);
	    		break;
	    	case HWPFShape.POSV_INSIDE:
	    		autoShape.setVerticalAlignment(WPAbstractShape.ALIGNMENT_INSIDE);
	    		break;
	    	case HWPFShape.POSV_OUTSIDE:
	    		autoShape.setVerticalAlignment(WPAbstractShape.ALIGNMENT_OUTSIDE);
	    		break;
    	}
    	
    	//relative to in vertical
    	switch(drawing.getVerticalRelativeElement())
    	{
	    	case HWPFShape.POSRELV_MARGIN:
	    		autoShape.setVerticalRelativeTo(WPAbstractShape.RELATIVE_MARGIN);
	    		break;
	    	case HWPFShape.POSRELV_PAGE:
	    		autoShape.setVerticalRelativeTo(WPAbstractShape.RELATIVE_PAGE);
	    		break;
	    	case HWPFShape.POSRELV_TEXT:
	    		autoShape.setVerticalRelativeTo(WPAbstractShape.RELATIVE_PARAGRAPH);
	    		break;
	    	case HWPFShape.POSRELV_LINE:
	    		autoShape.setVerticalRelativeTo(WPAbstractShape.RELATIVE_LINE);
	    		break;
    	}
    }
    /**
     * 
     */
    private boolean processShape(CharacterRun run, IElement leaf, boolean isWrap, int runIndex)
    {
        if (isWrap)
        {
            OfficeDrawings drawings = poiDoc.getOfficeDrawingsMain();
            OfficeDrawing drawing = drawings.getOfficeDrawingAt((int)run.getStartOffset() + runIndex);
            if (drawing == null)
            {
                return false;
            }
            
            Rectangle rect = new Rectangle();
            rect.x = (int)(drawing.getRectangleLeft() * MainConstant.TWIPS_TO_PIXEL);
            rect.y = (int)(drawing.getRectangleTop() * MainConstant.TWIPS_TO_PIXEL);
            rect.width = (int)((drawing.getRectangleRight() - drawing.getRectangleLeft()) * MainConstant.TWIPS_TO_PIXEL);
            rect.height = (int)((drawing.getRectangleBottom() - drawing.getRectangleTop()) * MainConstant.TWIPS_TO_PIXEL);
            
            byte[] b = drawing.getPictureData(control);
            if (b != null)
            {
                if (isSupportPicture(PictureType.findMatchingType(b)))
                {
                    PictureShape picShape = new PictureShape();                
                    
                    int index = control.getSysKit().getPictureManage().getPictureIndex(drawing.getTempFilePath(control));
                    if (index < 0)
                    {
                        Picture picture = new Picture();
                        // 图片数据
                        picture.setTempFilePath(drawing.getTempFilePath(control));
                        // 图片类型
                        picture.setPictureType(PictureType.findMatchingType(b).getExtension());
                        index = control.getSysKit().getPictureManage().addPicture(picture);
                    }                
                    picShape.setPictureIndex(index);    
                    picShape.setBounds(rect);  
                    picShape.setZoomX((short)1000);
                    picShape.setZoomY((short)1000);
                    picShape.setPictureEffectInfor(drawing.getPictureEffectInfor());
                    WPPictureShape wpPictureShape = new WPPictureShape();
                    wpPictureShape.setPictureShape(picShape);
                    
                    // wrap
                    if (drawing.getWrap() == 3 && !drawing.isAnchorLock())
                    {
                        if(drawing.isBelowText())
                        {
                        	wpPictureShape.setWrap(WPAutoShape.WRAP_BOTTOM);
                        }
                        else
                        {
                        	wpPictureShape.setWrap(WPAutoShape.WRAP_TOP);
                        } 
                        
                        processPicturePosition(drawing, wpPictureShape);
                    }
                    else
                    {
                    	wpPictureShape.setWrap(WPAutoShape.WRAP_OLE);
                    }
                    
                    AttrManage.instance().setShapeID(leaf.getAttribute(), control.getSysKit().getWPShapeManage().addShape(wpPictureShape));
                    return true;
                }
            }
            else
            {
                HWPFShape poiShape = drawing.getAutoShape();
                if (poiShape != null)
                {
                    return convertShape(leaf, drawing, null, poiShape, rect, 1.0f, 1.0f);
                }
            }
        }
        else
        {
            // Picture
            PicturesTable pictureTable = poiDoc.getPicturesTable();
            com.nvqquy98.lib.doc.office.fc.hwpf.usermodel.Picture pic = pictureTable.extractPicture(control.getSysKit().getPictureManage().getPicTempPath(),
                run, false);

            if (pic != null && isSupportPicture(pic.suggestPictureType()))
            {
                PictureShape picShape = new PictureShape();
                
                int index =control.getSysKit().getPictureManage().getPictureIndex(pic.getTempFilePath());
                if (index < 0)
                {
                    Picture picture = new Picture();
                    // 图片数据
                    picture.setTempFilePath(pic.getTempFilePath());
                    // 图片类型
                    picture.setPictureType(pic.suggestPictureType().getExtension());
                    index = control.getSysKit().getPictureManage().addPicture(picture);
                }                
                picShape.setPictureIndex(index);
                
                Rectangle rect = new Rectangle();
                rect.width = (int)(pic.getDxaGoal() * MainConstant.TWIPS_TO_PIXEL * pic.getHorizontalScalingFactor() / 1000f);
                rect.height = (int)(pic.getDyaGoal() * MainConstant.TWIPS_TO_PIXEL * pic.getVerticalScalingFactor() / 1000f);
                picShape.setBounds(rect);
                
                picShape.setZoomX(pic.getZoomX());
                picShape.setZoomY(pic.getZoomY());
                picShape.setPictureEffectInfor(PictureEffectInfoFactory.getPictureEffectInfor(pic));
                
                WPPictureShape wpPictureShape = new WPPictureShape();
                wpPictureShape.setPictureShape(picShape);
                
                wpPictureShape.setWrap(WPAutoShape.WRAP_OLE);
                
                AttrManage.instance().setShapeID(leaf.getAttribute(), control.getSysKit().getWPShapeManage().addShape(wpPictureShape));
                return true;
            }
            else
            {
            	//inline word art
            	InlineWordArt inlineShape = pictureTable.extracInlineWordArt(run);
            	if(inlineShape != null && inlineShape.getInlineWordArt() != null)
            	{
            		WPAutoShape autoShape = new WPAutoShape();
            		
            		Rectangle rect = new Rectangle();
                    rect.width = (int)(inlineShape.getDxaGoal() * MainConstant.TWIPS_TO_PIXEL * inlineShape.getHorizontalScalingFactor() / 1000f);
                    rect.height = (int)(inlineShape.getDyaGoal() * MainConstant.TWIPS_TO_PIXEL * inlineShape.getVerticalScalingFactor() / 1000f);
                    autoShape.setBounds(rect);
                    autoShape.setWrap(WPAutoShape.WRAP_OLE);
                    
                    processWordArtTextbox(inlineShape.getInlineWordArt().getSpContainer(), autoShape);
                    
                    AttrManage.instance().setShapeID(leaf.getAttribute(), control.getSysKit().getWPShapeManage().addShape(autoShape));
                    
                    return true;
            	}
            }
        }
        return false;
    }
    
    /**
     * 
     */
    private boolean isSupportPicture(PictureType picType)
    {
        String mineType = picType.getExtension();
        return mineType.equalsIgnoreCase("gif")
            || mineType.equalsIgnoreCase("jpeg")
            || mineType.equalsIgnoreCase("jpg")
            || mineType.equalsIgnoreCase("bmp")
            || mineType.equalsIgnoreCase("png")
            || mineType.equalsIgnoreCase("wmf")
            || mineType.equalsIgnoreCase("emf");
    }

    /**
     * 
     * @param file
     * @param key
     * @return
     */
    public boolean searchContent(File file, String key) throws Exception
    {
        boolean isContain = false;
        HWPFDocument poiDoc = new HWPFDocument(new FileInputStream(file));
        Range range = poiDoc.getRange();
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < range.numSections(); i++)
        {
            Section section = range.getSection(i);
            for (int j = 0; j < section.numParagraphs(); j++)
            {
                Paragraph para = section.getParagraph(j);
                for (int k = 0; k < para.numCharacterRuns(); k++)
                {     
                    sb.append(para.getCharacterRun(k).text());
                }
                if (sb.indexOf(key) >= 0)
                {
                    isContain = true;
                    break;
                }
                sb.delete(0, sb.length());
            }
        }
        return isContain;
    }
    
    /**
     * 
     */
    private void adjustBookmarkOffset(long before, long after)
    {
        for (Bookmark bm : bms)
        {
            if (bm.getStart() >= before && bm.getStart() <= after)
            {
                bm.setStart(offset);
            }
        }
    }
    
    /**
     * 
     */
    public void dispose()
    {
        if (isReaderFinish())
        {
            wpdoc = null;
            filePath = null;
            poiDoc = null;
            control =  null;
            hyperlinkAddress = null;
            //controlForReader = null;
            if (bms != null)
            {
                bms.clear();
                bms = null;
            }
        }
        
    }
    //
    private boolean isBreakChar;
    //
    private long offset;
    //
    private long textboxIndex;
    //
    private long docRealOffset;
    //
    private String filePath;
    private int docSourceType;
    //
    private WPDocument wpdoc;
    //
    private HWPFDocument poiDoc;
    //
    private Pattern hyperlinkPattern = Pattern.compile("[ \\t\\r\\n]*HYPERLINK \"(.*)\"[ \\t\\r\\n]*");
    //
    private String hyperlinkAddress;
    //
    private List<Bookmark> bms = new ArrayList<Bookmark>();;
}
