/*
 * 文件名称:          CellView.java
 *  
 * 编译器:            android2.2
 * 时间:              上午10:52:17
 */
package com.nvqquy98.lib.doc.office.ss.view;

import com.nvqquy98.lib.doc.office.common.PaintKit;
import com.nvqquy98.lib.doc.office.common.bg.AShader;
import com.nvqquy98.lib.doc.office.common.bg.BackgroundAndFill;
import com.nvqquy98.lib.doc.office.common.bg.Gradient;
import com.nvqquy98.lib.doc.office.common.bg.LinearGradientShader;
import com.nvqquy98.lib.doc.office.common.bg.PatternShader;
import com.nvqquy98.lib.doc.office.common.bg.TileShader;
import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.constant.SSConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPAttrConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPModelConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPViewConstant;
import com.nvqquy98.lib.doc.office.fc.ppt.attribute.ParaAttr;
import com.nvqquy98.lib.doc.office.fc.ppt.attribute.RunAttr;
import com.nvqquy98.lib.doc.office.fc.xls.Reader.SchemeColorUtil;
import com.nvqquy98.lib.doc.office.simpletext.font.FontKit;
import com.nvqquy98.lib.doc.office.simpletext.model.AttrManage;
import com.nvqquy98.lib.doc.office.simpletext.model.AttributeSetImpl;
import com.nvqquy98.lib.doc.office.simpletext.model.CellLeafElement;
import com.nvqquy98.lib.doc.office.simpletext.model.IAttributeSet;
import com.nvqquy98.lib.doc.office.simpletext.model.IDocument;
import com.nvqquy98.lib.doc.office.simpletext.model.LeafElement;
import com.nvqquy98.lib.doc.office.simpletext.model.ParagraphElement;
import com.nvqquy98.lib.doc.office.simpletext.model.STDocument;
import com.nvqquy98.lib.doc.office.simpletext.model.SectionElement;
import com.nvqquy98.lib.doc.office.simpletext.view.IView;
import com.nvqquy98.lib.doc.office.simpletext.view.STRoot;
import com.nvqquy98.lib.doc.office.ss.model.CellRangeAddress;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Cell;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Row;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Sheet;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Workbook;
import com.nvqquy98.lib.doc.office.ss.model.style.CellStyle;
import com.nvqquy98.lib.doc.office.ss.model.table.SSTable;
import com.nvqquy98.lib.doc.office.ss.model.table.SSTableCellStyle;
import com.nvqquy98.lib.doc.office.ss.model.table.SSTableStyle;
import com.nvqquy98.lib.doc.office.ss.model.table.TableStyleKit;
import com.nvqquy98.lib.doc.office.ss.other.DrawingCell;
import com.nvqquy98.lib.doc.office.ss.other.MergeCell;
import com.nvqquy98.lib.doc.office.ss.other.MergeCellMgr;
import com.nvqquy98.lib.doc.office.ss.util.ModelUtil;
import com.nvqquy98.lib.doc.office.ss.util.format.NumericFormatter;
import com.nvqquy98.lib.doc.office.system.IControl;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
/**
 * draw cells of sheet
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2011-12-2
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class CellView
{
    
    /**
     * 
     * @param sheetView
     */
    public CellView(SheetView sheetView)
    {
        this.sheetView = sheetView;
        cellBorderView = new CellBorderView(sheetView);
        
        cellRect = new RectF();
    }

    private void redrawMergedCellBordersAndBackground(Canvas canvas, CellRangeAddress cr)
    {
        canvas.save();
        canvas.clipRect(left,
            top,
            left + mergedCellSize.getWidth() - mergedCellSize.getNovisibleWidth() - (cellInfor.getWidth() - cellInfor.getVisibleWidth()),
            top + mergedCellSize.getHeight() - mergedCellSize.getNoVisibleHeight() - (cellInfor.getHeight() - cellInfor.getVisibleHeight()));
        
        //fill background
        canvas.drawColor(Color.WHITE);

        //redraw merged cell border
        Paint paint = PaintKit.instance().getPaint();
        //save paint property      
        int oldColor = paint.getColor();    
        Style oldStyle = paint.getStyle();
        
        paint.setColor(SSConstant.GRIDLINE_COLOR);
        paint.setStyle(Style.STROKE);
        canvas.drawRect(left,
            top,
            left + mergedCellSize.getWidth() - mergedCellSize.getNovisibleWidth(),
            top + mergedCellSize.getHeight() - mergedCellSize.getNoVisibleHeight(), paint);
        
        paint.setStyle(oldStyle);
        
        int minRowIndex = sheetView.getMinRowAndColumnInformation().getMinRowIndex();
        int minColumnIndex = sheetView.getMinRowAndColumnInformation().getMinColumnIndex();
        if(mergedCellSize.isFrozenRow())
        {
            minRowIndex = 0;
        }
        if(mergedCellSize.isFrozenColumn())
        {
            minColumnIndex = 0;
        }
        //redraw header border          
        if(minRowIndex >= cr.getFirstRow())
        {
            paint.setColor(SSConstant.HEADER_GRIDLINE_COLOR);
            canvas.drawRect(left,  top, left + mergedCellSize.getWidth() - mergedCellSize.getNovisibleWidth(), top + 1, paint);
        }

        if(minColumnIndex >= cr.getFirstColumn())
        {
            paint.setColor(SSConstant.HEADER_GRIDLINE_COLOR);
            canvas.drawRect(left,  top, left + 1, top + mergedCellSize.getHeight() - mergedCellSize.getNoVisibleHeight(), paint);
        }
        
        paint.setColor(oldColor);
        
        canvas.restore();
    }
    
    /**
     * 
     * @param canvas
     * @param cell
     * @param row
     * @param col
     * @param left
     * @param top
     * @param w
     * @param h
     * @param visibleWidth
     * @param visibleHeight
     */
    public void draw(Canvas canvas, Cell cell, DrawingCell cellInfor)
    {
        if(cell == null || sheetView.getSpreadsheet().isAbortDrawing())
        {
            return;
        }
        
        Sheet sheet = sheetView.getCurrentSheet();       
        {
            this.cellInfor = cellInfor;                
            //init data
            this.left = cellInfor.getLeft();
            this.top =cellInfor.getTop();
            this.mergedCellSize.setWidth(cellInfor.getWidth());
            this.mergedCellSize.setHeight(cellInfor.getHeight());
            mergedCellSize.setNovisibleWidth(0);
            mergedCellSize.setNoVisibleHeight(0);
            
            if (cell.getRangeAddressIndex() >= 0)
            {
                //find the width and height of not show cell which include in merged cell 
                CellRangeAddress cr = sheet.getMergeRange(cell.getRangeAddressIndex());
                
                //
                if (mergedCellMgr.isDrawMergeCell(sheetView, cr, cellInfor.getRowIndex(), cellInfor.getColumnIndex()))
                {
                    mergedCellSize = mergedCellMgr.getMergedCellSize(sheetView, cr,  cellInfor.getRowIndex(), cellInfor.getColumnIndex());            
                    
                    cell = sheet.getRow(cr.getFirstRow()).getCell(cr.getFirstColumn());
                    
                    //redraw merged cell background and borders
                    redrawMergedCellBordersAndBackground(canvas, cr);
                }
                else
                {
                    return;
                } 
            }
        } 
        
        cellRect.set(left,  top, 
            (left + mergedCellSize.getWidth() - mergedCellSize.getNovisibleWidth() - (cellInfor.getWidth() - cellInfor.getVisibleWidth())),
            (top + mergedCellSize.getHeight() - mergedCellSize.getNoVisibleHeight() - (cellInfor.getHeight() - cellInfor.getVisibleHeight()))
            );
        
        SSTable table= cell.getTableInfo();
        SSTableCellStyle tableCellStyle = null;
        if(table != null)
        { 
            tableCellStyle = getTableCellStyle(table, 
                sheetView.getCurrentSheet().getWorkbook(), 
                cell.getRowNumber(), cell.getColNumber());
        }
        
        drawCellBackgroundAndBorder(canvas, cell, tableCellStyle);
        
        drawCellContents(canvas, cell, cellInfor, tableCellStyle);      
    }
    
    /**
     * 
     * @param canvas
     * @param rect
     * @param activeCellType
     */
    public void drawActiveCellBorder(Canvas canvas, RectF rect, short activeCellType)
    {
        cellBorderView.drawActiveCellBorder(canvas, rect, activeCellType);
    }
    /**
     * 
     * @param canvas
     * @param cellInfor
     */
    public void drawCellBackgroundAndBorder(Canvas canvas, Cell cell, SSTableCellStyle tableCellStyle)
    {
        Paint paint = PaintKit.instance().getPaint();        
        //save paint text size
        int color = paint.getColor();
        
        //cell background
        CellStyle style = cell.getCellStyle();
        if(style != null && style.getFillPatternType() == BackgroundAndFill.FILL_SOLID)
        {       
            paint.setColor(style.getFgColor());
            if(Math.abs(cellRect.left - sheetView.getRowHeaderWidth()) < 1)
            {
                canvas.drawRect(cellRect.left + 1, cellRect.top, cellRect.right, cellRect.bottom, paint); 
            }
            else
            {
                canvas.drawRect(cellRect, paint); 
            }
        }
        else if(style != null && 
        		(style.getFillPatternType() == BackgroundAndFill.FILL_SHADE_LINEAR || style.getFillPatternType() == BackgroundAndFill.FILL_SHADE_RADIAL))
        { 
        	drawGradientAndTile(canvas, sheetView.getSpreadsheet().getControl(), sheetView.getSheetIndex(),
        			style.getFillPattern(),  cellRect, sheetView.getZoom(), paint);
        }
        else
        {
            if(tableCellStyle != null && tableCellStyle.getFillColor() != null)
            {
                int bg = tableCellStyle.getFillColor();
                paint.setColor(bg);
                if(Math.abs(cellRect.left - sheetView.getRowHeaderWidth()) < 1)
                {
                    canvas.drawRect(cellRect.left + 1, cellRect.top, cellRect.right, cellRect.bottom, paint); 
                }
                else
                {
                    canvas.drawRect(cellRect, paint); 
                }
            }
        }
        
        paint.setColor(color);
        
        //draw cell borders   
        cellBorderView.draw(canvas, cell, cellRect, tableCellStyle);
    }
    
    private void drawGradientAndTile(Canvas canvas, IControl control, int viewIndex,
			BackgroundAndFill fill, RectF rect, float zoom, Paint paint)
	{
		AShader aShader = fill.getShader();
		 if(aShader != null)
		 {
			 Shader shader = aShader.getShader();
			 if(shader == null)
			 {
				 float r = 1 / zoom;
				 shader = aShader.createShader(control, viewIndex,
						 new Rect(Math.round(rect.left * r),
								 Math.round(rect.top * r), 
										 Math.round(rect.right * r), 
												 Math.round(rect.bottom * r)));
				 if(shader == null)
				 {
					 return;
				 }
			 }         		
			 
			 Matrix m = new Matrix();
		 	 float offX = rect.left;
		 	 float offY = rect.top;
		 	 if(aShader instanceof TileShader)
			 {
		 		 TileShader tileShader = (TileShader)aShader;
				 offX += tileShader.getOffsetX() * zoom;
				 offY += tileShader.getOffsetY() * zoom;
				 
				 m.postScale(zoom, zoom);
			 }
		 	 else if(aShader instanceof PatternShader)
		 	 {
		 		 
		 	 }
		 	 else
		 	 {
		 		 if(aShader instanceof LinearGradientShader)
		 		 {
		 			LinearGradientShader gradient = (LinearGradientShader)aShader;
		     		float focusX = 1f;
		     		float focusY = 1f;
		     		
		     		if(gradient.getAngle() == 90)
		     		{
		     			switch(gradient.getFocus())
		         		{
		             		case 100:
		             			focusX = 0f;
		             			focusY = 0;
		             			break;
		             		case 0:
		             			focusX = 1f;
		             			break;
		             		case -50:
		             			focusX = 0.5f;
		             			focusY = 0.5f;
		             			break;
		             		case 50:
		             			focusX = -0.5f;
		             			focusY = -0.5f;
		             			break;
		         		}
		     		}
		     		else
		     		{
		     			switch(gradient.getFocus())
		         		{
		             		case 100:
		             			focusX = 0f;
		             			focusY = 0;
		             			break;
		             		case 0:
		             			focusX = 1f;
		             			break;        	                 			
		             		case 50:
		             			focusX = 0.5f;
		             			focusY = 0.5f;
		             			break;
		             		case -50:
		             			focusX = -0.5f;
		             			focusY = -0.5f;
		             			break;        	                 		
		         		}
		     		}
		     		
		     		offX += focusX * rect.width();
		     		offY +=  focusY * rect.height();
		 		 }                 		
			 
		 		m.postScale(rect.width() / (float)Gradient.COORDINATE_LENGTH, 
		 				rect.height() / (float)Gradient.COORDINATE_LENGTH);
		 	 }
		 	 m.postTranslate(offX, offY);
		 	 shader.setLocalMatrix(m);
			 paint.setShader(shader);		     
		     canvas.drawRect(rect, paint);
		     paint.setShader(null);
		 }
	} 
    
    public SSTableCellStyle getTableCellStyle(SSTable table, Workbook book, int row, int col)
    {
        CellRangeAddress rangeAddr = table.getTableReference();
        SSTableStyle tableStyle = tableStyleKit.getTableStyle(table.getName(), SchemeColorUtil.getSchemeColor(book));
        if(tableStyle == null)
        {
            return null;
        }
        
        if(table.isHeaderRowShown())
        {
            if(row == rangeAddr.getFirstRow())
            {
                //first row
                return tableStyle.getFirstRow();
            }            
            else if(table.isTotalRowShown() && row == rangeAddr.getLastRow())
            {
                if(tableStyle.getLastRow() != null)
                {
                    return tableStyle.getLastRow();
                }
                else if(table.isShowFirstColumn() && col == rangeAddr.getFirstColumn() && tableStyle.getFirstCol() != null)
                {
                    return tableStyle.getFirstCol();
                }
                else if(table.isShowLastColumn() && col == rangeAddr.getLastColumn() && tableStyle.getLastCol() != null)
                {
                    return tableStyle.getLastCol();
                }
                else
                {
                    return tableStyle.getBand2H();
                }
            }
            else if(table.isShowFirstColumn() && col == rangeAddr.getFirstColumn() && tableStyle.getFirstCol() != null)
            {
                return tableStyle.getFirstCol();
            }
            else if(table.isShowLastColumn() && col == rangeAddr.getLastColumn() && tableStyle.getLastCol() != null)
            {
                return tableStyle.getLastCol();
            }
            else if(table.isShowRowStripes())
            {                
                if((row - rangeAddr.getFirstRow()) % 2 == 1)
                {
                    //band1v
                    return tableStyle.getBand1V();
                }
                else if(table.isShowColumnStripes() && (col - rangeAddr.getFirstColumn()) % 2  == 0)
                {
                    //band1H
                    return tableStyle.getBand1H();
                }
                else
                {
                    return tableStyle.getBand2V();
                }
            }
            else if(table.isShowColumnStripes())
            {
                if((col - rangeAddr.getFirstColumn()) % 2  == 0)
                {
                    //band1H
                    return tableStyle.getBand1H();
                }
                else
                {
                    return tableStyle.getBand2H();
                }
            }

            return tableStyle.getBand2H();
        }
        else
        {
            if(table.isTotalRowShown() && row == rangeAddr.getLastRow())
            {
                if(tableStyle.getLastRow() != null)
                {
                    return tableStyle.getLastRow();
                }
                else if(table.isShowFirstColumn() && col == rangeAddr.getFirstColumn() && tableStyle.getFirstCol() != null)
                {
                    return tableStyle.getFirstCol();
                }
                else if(table.isShowLastColumn() && col == rangeAddr.getLastColumn() && tableStyle.getLastCol() != null)
                {
                    return tableStyle.getLastCol();
                }
                else
                {
                    return tableStyle.getBand2H();
                }                
            }
            else if(table.isShowFirstColumn() && col == rangeAddr.getFirstColumn() && tableStyle.getFirstCol() != null)
            {
                return tableStyle.getFirstCol();
            }
            else if(table.isShowLastColumn() && col == rangeAddr.getLastColumn() && tableStyle.getLastCol() != null)
            {
                return tableStyle.getLastCol();
            }
            else if(table.isShowRowStripes())
            {                
                if((row - rangeAddr.getFirstRow()) % 2 == 0)
                {
                    //band1v
                    return tableStyle.getBand1V();
                }
                else if(table.isShowColumnStripes() && (col - rangeAddr.getFirstColumn()) % 2  == 0)
                {
                    //band1H
                    return tableStyle.getBand1H();
                }
                else
                {
                    return tableStyle.getBand2V();
                }
            }
            else if(table.isShowColumnStripes())
            {
                if((col - rangeAddr.getFirstColumn()) % 2  == 0)
                {
                    //band1H
                    return tableStyle.getBand1H();
                }
                else
                {
                    return tableStyle.getBand2H();
                }
            }

            return tableStyle.getBand2H();
        }
    }
    
    /**
     * canvas rotate, old coordinate system convert to new coordinate system
     * x1 = sina * y0 + cosa * x0
     * y1 = cosa * y0 - sina * x0
     */
    private int getRotatedX(int x, int y, int degree)
    {
        double d = degree / 180f * Math.PI;
        return (int)(Math.sin(d) * y + Math.cos(d) * x);
    }
    
    private int getRotatedY(int x, int y, int degree)
    {
        double d = degree / 180f * Math.PI;
        return (int)(Math.cos(d) * y - Math.sin(d) * x);
    }
    
    /**
     * 
     * @param canvas
     * @param cellInfor
     */
    private void drawCellContents(Canvas canvas, Cell cell, DrawingCell cellInfor, SSTableCellStyle tableCellStyle)
    {
        Sheet sheet = sheetView.getCurrentSheet();
        
        //expanded cell
        if(cell.getExpandedRangeAddressIndex() >= 0)
        {
            
            Row row = sheet.getRow(cellInfor.getRowIndex());            
            CellRangeAddress cr = row.getExpandedRangeAddress(cell.getExpandedRangeAddressIndex()).getRangedAddress();
            if(mergedCellMgr.isDrawMergeCell(sheetView, cr, cellInfor.getRowIndex(), cellInfor.getColumnIndex()))
            {                    
                mergedCellSize = mergedCellMgr.getMergedCellSize(sheetView, cr,  cellInfor.getRowIndex(), cellInfor.getColumnIndex());
                cell = row.getExpandedRangeAddress(cell.getExpandedRangeAddressIndex()).getExpandedCell();                   
                
                cellRect.set(left,  top, 
                    (left + mergedCellSize.getWidth() - mergedCellSize.getNovisibleWidth() - (cellInfor.getWidth() - cellInfor.getVisibleWidth())),
                        (top + mergedCellSize.getHeight() - mergedCellSize.getNoVisibleHeight() - (cellInfor.getHeight() - cellInfor.getVisibleHeight()))
                    );
            }
            else
            {
                return;
            }
        }
        
        String content = ModelUtil.instance().getFormatContents(sheet.getWorkbook(), cell); 
        if(content == null || content.length() == 0)
        { 
            return;
        }
    
        Paint paint = FontKit.instance().getCellPaint(cell, sheet.getWorkbook(), tableCellStyle);
        
        //save paint text size
        float textSize = paint.getTextSize();        
        paint.setTextSize(textSize * sheetView.getZoom());
        
        numericCellAlignRight = false;
        if(cell.getCellType() == Cell.CELL_TYPE_BOOLEAN 
            || (cell.getCellType() == Cell.CELL_TYPE_NUMERIC && cell.getCellNumericType() != Cell.CELL_TYPE_NUMERIC_STRING))
        {
            numericCellAlignRight = true;
        } 
        
        if(numericCellAlignRight)
        {
            drawNumericCell(canvas, cell, content, paint);
        }
        else
        {
            drawNonNumericCell(canvas, cell, content, paint);      
        } 
        //restore paint style
        paint.setTextSize(textSize);
        
        
//      //canvas rotate
//      Paint paint = PaintKit.instance().getPaint();
//      int color = paint.getColor();
//      paint.setColor(Color.RED);        
//      canvas.rotate(45);
//      
//      canvas.drawText("hello world -45", getRotatedX(100, 200, 45), getRotatedY(100, 200, 45),
//              paint);
//      
//      canvas.rotate(-45);        
//      paint.setColor(color);
        
    }
    
    public static boolean isComplexText(Cell cell)
    {
        if(cell.getCellType() == Cell.CELL_TYPE_STRING && cell.getStringCellValueIndex() >= 0)
        {
            Object value = cell.getSheet().getWorkbook().getSharedItem(cell.getStringCellValueIndex());
            return (value instanceof SectionElement);
        }
        
        return false;
    }
    
    /**
     * draw non-numeric cell
     * @param canvas
     * @param currentRow
     * @param currentColumn
     * @param style
     * @param contents
     * @param paint
     */
    private void drawNonNumericCell(Canvas canvas, Cell cell, String contents, Paint paint)
    {     
        if(isComplexText(cell))
        {
            drawComplexTextCell(canvas, cell);
        }
        else if (cell.getCellStyle().isWrapText() 
            && (contents.contains("\n") || paint.measureText(contents) + 2 * SSConstant.SHEET_SPACETOBORDER > mergedCellSize.getWidth()))
        {   
            //more than one lines
            canvas.save();
            canvas.clipRect(cellRect);
            
            //drawWrapText(canvas, style, contents, paint); 
            drawWrapText(canvas, cell, contents);
            
            canvas.restore();
        }
        else
        {
            //only one line(cannot have more than 1024 characters in one line)
            if(contents.length() > 1024)
            {
                contents = contents.substring(0, 1024);
            }
            
           drawNonWrapText(canvas, cell, contents, paint);           
        }
    }
    
    /**
     * draw complex text
     * @param canvas
     * @param cell
     */
    private void drawComplexTextCell(Canvas canvas, Cell cell)
    {
        if(cell.getCellStyle().isWrapText())
        {
            drawWrapComplexTextCell(canvas, cell);
        }
        else
        {
            drawNotWrapComplexTextCell(canvas, cell);
        }
    }
    
    /**
     * 
     * @param canvas
     * @param cell
     */
    private void drawWrapComplexTextCell(Canvas canvas, Cell cell)
    {
        canvas.save();
        canvas.clipRect(cellRect);
        
        STRoot root = cell.getSTRoot();
        if(root == null)
        {
            Rect rect = ModelUtil.instance().getCellAnchor(
                sheetView.getCurrentSheet(), cell.getRowNumber(), cell.getColNumber());
            
            SectionElement elem = (SectionElement)(cell.getSheet().getWorkbook().getSharedItem(cell.getStringCellValueIndex()));
            
            if (elem == null || elem.getEndOffset() - elem.getStartOffset() == 0)
            {
            	canvas.restore();
                return;
            }       
            
            IAttributeSet attr = elem.getAttribute();
            // 宽度
            AttrManage.instance().setPageWidth(attr, Math.round(rect.width() * MainConstant.PIXEL_TO_TWIPS));
            // 高度
            AttrManage.instance().setPageHeight(attr, Math.round(rect.height() * MainConstant.PIXEL_TO_TWIPS));
            
            IDocument doc = new STDocument();
            doc.appendSection(elem);
            root = new STRoot(sheetView.getSpreadsheet().getEditor(), doc);
            root.setWrapLine(true);
            root.doLayout();
            
            cell.setSTRoot(root);
        } 
        
        root.draw(canvas, Math.round(left - mergedCellSize.getNovisibleWidth() - (cellInfor.getWidth() - cellInfor.getVisibleWidth())),
            Math.round(top - mergedCellSize.getNoVisibleHeight() - (cellInfor.getHeight() - cellInfor.getVisibleHeight())),
            sheetView.getZoom());
        
        canvas.restore();
    }
    
    /**
     * 
     * @param canvas
     * @param cell
     */
    private void drawNotWrapComplexTextCell(Canvas canvas, Cell cell)
    {
        canvas.save();
        canvas.clipRect(cellRect);
        CellStyle style = cell.getCellStyle();
        STRoot root = cell.getSTRoot();
        if(root == null)
        {
        	canvas.restore();
            return;
        }
        
        IView view = root.getChildView();
        view = view.getChildView();
        int lineWidth = (int)(view.getLayoutSpan(WPViewConstant.X_AXIS) * sheetView.getZoom());
        int textHeight = (int)(view.getHeight() * sheetView.getZoom());
        
        //horizontal, indent
        int indent = sheetView.getIndentWidthWithZoom(style.getIndent());
        if(indent + 2 * SSConstant.SHEET_SPACETOBORDER >= mergedCellSize.getWidth())
        {
        	canvas.restore();
        	return;
        }
        else
        {
            switch (style.getHorizontalAlign())
            {
                case CellStyle.ALIGN_GENERAL:
                case CellStyle.ALIGN_LEFT:
                case CellStyle.ALIGN_FILL:
                case CellStyle.ALIGN_JUSTIFY:
                case CellStyle.ALIGN_CENTER_SELECTION:
                    left += SSConstant.SHEET_SPACETOBORDER; 
                    mergedCellSize.setWidth(mergedCellSize.getWidth() - indent);                        
                    break;                
                case CellStyle.ALIGN_RIGHT:
                    mergedCellSize.setWidth(mergedCellSize.getWidth() - indent);                        
                    left += mergedCellSize.getWidth() - lineWidth - SSConstant.SHEET_SPACETOBORDER;
                    break;
                case CellStyle.ALIGN_CENTER:
                    left += (mergedCellSize.getWidth() - lineWidth) / 2;
                    
                default:
                    break;
            }

            // 垂直对齐
            switch (style.getVerticalAlign())
            {
                case CellStyle.VERTICAL_TOP:
                    top += SSConstant.SHEET_SPACETOBORDER;
                    break;
                case CellStyle.VERTICAL_CENTER:
                case CellStyle.VERTICAL_JUSTIFY:
                    top += SSConstant.SHEET_SPACETOBORDER/*(mergedCellSize.getHeight() - textHeight) / 2*/;
                    break;
                case CellStyle.VERTICAL_BOTTOM:
                    top += SSConstant.SHEET_SPACETOBORDER/*mergedCellSize.getHeight() - textHeight*/;
                    
                default:
                    break;
            }  
            float x = left - mergedCellSize.getNovisibleWidth() - (cellInfor.getWidth() - cellInfor.getVisibleWidth());
            float y = top - mergedCellSize.getNoVisibleHeight() - (cellInfor.getHeight() - cellInfor.getVisibleHeight());

            if(cell.getExpandedRangeAddressIndex() >= 0)
            {
                /**
                 * not draw content here, it may be covered by next blank cell's background
                 */
                //canvas.drawText(contents, x, y, paint);       
                sheetView.addExtendCell(cell, new RectF(cellRect), x, y, root);
            }
            else
            {                    
                root.draw(canvas, (int)x, (int)y, sheetView.getZoom());
            }
 
            canvas.restore();
        }
    }
    
    /**
     * 
     * @param canvas
     * @param contents
     * @param paint
     */
    private void drawAccountCell(Canvas canvas, Cell cell, String contents, Paint paint)
    {
        FontMetrics fm = paint.getFontMetrics();
        // 文本宽度
        int textWidth = (int)(paint.measureText(contents));
        // 文本高度
        int textHeight = (int)(Math.ceil(fm.descent - fm.ascent)); 
        
        left += SSConstant.SHEET_SPACETOBORDER;
        //indention
        CellStyle style = cell.getCellStyle();
        int indent = (int)(sheetView.getIndentWidthWithZoom(style.getIndent()));
        //
        canvas.save();
        //draw area
        canvas.clipRect(cellRect);         
        
        //accounting cell, align currency symbol
        if(textWidth + indent + SSConstant.SHEET_SPACETOBORDER > mergedCellSize.getWidth())
        {
            //text width large than cell width, show string "#####"
            int charCnt = (int)((mergedCellSize.getWidth() - 2 * SSConstant.SHEET_SPACETOBORDER) / paint.measureText("#"));
            
            strBuilder.delete(0, strBuilder.length());
            for(int i = 0; i< charCnt; i++)
            {
                strBuilder.append('#');
            }
            
            contents  = strBuilder.toString();
            
            textWidth = (int)(paint.measureText(contents));
            
        }
        else
        {             
            switch (style.getHorizontalAlign())
            {
                case CellStyle.ALIGN_GENERAL:
                case CellStyle.ALIGN_LEFT:
                    left += indent; 
                    mergedCellSize.setWidth(mergedCellSize.getWidth() - indent);
                    break;                
                case CellStyle.ALIGN_RIGHT:
                    mergedCellSize.setWidth(mergedCellSize.getWidth() - indent);
                    break;
            }
            
            int index = contents.indexOf("*");
            if(index > -1)
            {
                String first = contents.substring(0, index);
                String end = contents.substring(index + 1, contents.length());
                
                int blackCnt = (int)((mergedCellSize.getWidth() - paint.measureText(first + end)- 2 * SSConstant.SHEET_SPACETOBORDER) / paint.measureText(" "));
                
                strBuilder.delete(0, strBuilder.length());
                strBuilder.append(first);
                for(int i = 0; i< blackCnt; i++)
                {
                    strBuilder.append(' ');
                }
                
                strBuilder.append(end);
                contents = strBuilder.toString();
            }                    
        } 
        
         // 垂直对齐
         switch (cell.getCellStyle().getVerticalAlign())
         {
             case CellStyle.VERTICAL_TOP:
                 top += SSConstant.SHEET_SPACETOBORDER;
                 break;
             case CellStyle.VERTICAL_CENTER:
             case CellStyle.VERTICAL_JUSTIFY:
                 top += (mergedCellSize.getHeight() - textHeight) / 2;
                 break;
             case CellStyle.VERTICAL_BOTTOM:
                 top += mergedCellSize.getHeight() - textHeight;
                 
             default:
                 break;
         }
         
         float x = left - mergedCellSize.getNovisibleWidth() - (cellInfor.getWidth() - cellInfor.getVisibleWidth());
         float y = top - fm.ascent - mergedCellSize.getNoVisibleHeight() - (cellInfor.getHeight() - cellInfor.getVisibleHeight());
         
         canvas.drawText(contents, x, y, paint);
         canvas.restore();    
    }
    
    
    /**
     * contents contain 'E' 
     * @param contents
     * @param paint
     * @return
     */
    private String getScientificGeneralContents(String contents, Paint paint)
    {   
        String firstPart = "";
        String endPart = "";
        char lastChar = 0;
        
        boolean replacedByChar = false;
        
        int index = contents.indexOf('E');        
        
        //first part of contents
        firstPart = contents.substring(0, index);
        //end part of contents
        endPart = contents.substring(index + 1);
        int e = Integer.parseInt(endPart);
        if(e > 0)
        {
            if(e < 10)
            {
                endPart = "E+0".concat(endPart);
            }
            else
            {
                endPart = "E+".concat(endPart);
            }
        }
        else
        {
            if(e > -10)
            {
                endPart = "E-0".concat(String.valueOf(-e));
            }
            else
            {
                endPart = "E".concat(endPart);
            }
        }
        
        
        if(paint.measureText(endPart) + 2 * SSConstant.SHEET_SPACETOBORDER >= mergedCellSize.getWidth())
        {
            replacedByChar = true;
        }
        else
        {
            while((int)paint.measureText(firstPart + endPart) + 2 * SSConstant.SHEET_SPACETOBORDER > mergedCellSize.getWidth() )
            {
                if(firstPart.length() >= 1)
                {
                    lastChar = firstPart.charAt(firstPart.length() - 1);
                    firstPart = firstPart.substring(0, firstPart.length() - 1);
                }
                else
                {
                    replacedByChar = true;
                    break;
                }
            }
        }
        
        if(replacedByChar || firstPart.length() == 0 || firstPart.equals("-"))
        {                        
            contents = "";
            while(paint.measureText(contents + "#") + 2 * SSConstant.SHEET_SPACETOBORDER < mergedCellSize.getWidth())
            {
                contents = contents + "#";
            }
        }
        else if(firstPart.charAt(firstPart.length() - 1) == '.')
        {
            //remove '.'
            contents = firstPart.substring(0, firstPart.length() - 1) + endPart;
        }
        else
        {
            //round value
            if(lastChar <= '9' && lastChar >= '5')
            {
                firstPart = ceilNumeric(firstPart);
            }
            contents = firstPart + endPart;
        }
        
        return contents;
    }
    
    
    private String ceilNumeric(String contents)
    {
        int index = contents.indexOf('.');
        if(index > 0)
        {
            char[] chars = contents.toCharArray();
            int i = chars.length - 1;
            while(i > index && chars[i] == '9')
            {
                i--;
            }
            
            if(i > index)
            {
                chars[i] += 1;
                contents = String.valueOf(chars, 0, i + 1);
            }
            else
            {
                contents = String.valueOf((int)Double.parseDouble(contents) + 1);
            } 
        }
        else if(contents.length() == 1)
        {
            contents = String.valueOf((int)Double.parseDouble(contents) + 1);
        }
        
        return contents;
    }
    /**
     * contents not contain 'E' 
     * @param contents
     * @param paint
     * @return
     */
    private String getNonScientificGeneralContents(String contents, Paint paint)
    {
        // 文本宽度
        int textWidth;
        String firstPart = "";
        String endPart = "";
        double value;
        int step = 1;
        boolean replacedByChar = false;
        
        // 文本宽度
        textWidth = (int)(paint.measureText(contents)); 
        char lastChar = 0;
        
        //accounting cell, align currency symbol
        if(textWidth + 2 * SSConstant.SHEET_SPACETOBORDER > mergedCellSize.getWidth())
        {
        	if(contents.length() == 1)
        	{
        		return "";
        	}
            value = Double.parseDouble(contents);
            //
            if( (int)value == 0 || (int)(paint.measureText(String.valueOf((int)value))) + 2 * SSConstant.SHEET_SPACETOBORDER > mergedCellSize.getWidth() )
            {
                //scientific cell value
                int integerCnt = 0;
                step = 10;
                while((int)Math.abs(value / step) > 0)
                {
                    value = value / step;
                    integerCnt ++;
                }   
                
                //two part of contents
                if(integerCnt > 0)
                {
                    endPart = "E+";
                    if(integerCnt < 10)
                    {
                        endPart = endPart + "0" + String.valueOf(integerCnt);
                    }
                    else
                    {
                        endPart += String.valueOf(integerCnt);
                    } 
                    
                    firstPart = String.valueOf(value);
                }
                else
                {
                    contents = String.valueOf(value);
                    int index = contents.indexOf('E');
                    if(index > 0)
                    {
                        firstPart = contents.substring(0, index);
                        endPart = contents.substring(index);
                    }
                    else
                    {
                        integerCnt = 0;
                        while(Math.abs(value) < 1 && Math.abs(value * Integer.MAX_VALUE) > 0)
                        {
                            value *= 10;
                            integerCnt++;
                        }
                        firstPart = String.valueOf(value);
                        endPart = "E-".concat(String.valueOf(integerCnt));
                    }
                    
                }
                             
                
                if(paint.measureText(endPart) + 2 * SSConstant.SHEET_SPACETOBORDER >= mergedCellSize.getWidth())
                {
                    replacedByChar = true;
                }
                else
                {                    
                    
                    while((int)paint.measureText(firstPart + endPart) + 2 * SSConstant.SHEET_SPACETOBORDER > mergedCellSize.getWidth() )
                    {
                        if(firstPart.length() >= 1)
                        {
                            lastChar = firstPart.charAt(firstPart.length() - 1);
                            firstPart = firstPart.substring(0, firstPart.length() - 1);
                        }
                        else
                        {
                            replacedByChar = true;
                            break;
                        }
                    }
                }
                
                if(replacedByChar || firstPart.length() == 0 || firstPart.equals("-"))
                {          
                    contents = "";
                    while(paint.measureText(contents + "#") + 2 * SSConstant.SHEET_SPACETOBORDER < mergedCellSize.getWidth())
                    {
                        contents = contents + "#";
                    }
                }
                else if(firstPart.charAt(firstPart.length() - 1) == '.')
                {
                    //remove '.'
                    contents = firstPart.substring(0, firstPart.length() - 1) + endPart;
                }
                else 
                {
                    //round value
                    if(lastChar <= '9' && lastChar >= '5')
                    {
                        firstPart = ceilNumeric(firstPart);                        
                    }
                    contents = firstPart + endPart;
                }
            }
            else
            {
                //add decimal part                
                while((int)paint.measureText(contents) + 2 * SSConstant.SHEET_SPACETOBORDER > mergedCellSize.getWidth() )
                {
                    lastChar = contents.charAt(contents.length() - 1);
                    contents = contents.substring(0, contents.length() - 1);                    
                }  
                
                if(contents.charAt(contents.length() - 1) == '.')
                {
                    contents = contents.substring(0, contents.length() - 1);
                }
                else
                {
                    //round value
                    if(lastChar <= '9' && lastChar >= '5')
                    {                        
                        contents = ceilNumeric(contents);
                    }
                }
                
                contents = trimInvalidateZero(contents);
            }
        }
        
        return contents;
    }
    
    private String trimInvalidateZero(String contents)
    {
        if(contents == null || contents.length() == 0)
        {
            return contents;
        }
        int index = contents.indexOf('.');
        if(index > 0)
        {
            char[] chars = contents.toCharArray();
            
            int i = chars.length - 1;
            while(i > index && chars[index] == '0')
            {
                index--;
            }
            if(chars[i] == '.')
            {
                index--;
            }
            
            return String.valueOf(chars, 0, i);
        }
        return contents;
    }
    
    private String adjustGeneralCellContent(String contents, Paint paint)
    {
        //General cell
        contents = scientificToNormal(contents);
        
        int index = contents.indexOf('E');
        if(index > -1)
        {
            return getScientificGeneralContents(contents, paint);
        }
        else
        {
            
            return getNonScientificGeneralContents(contents, paint);
        } 
    }
    
    private boolean isInteger(String str)
    { 
    	try
    	{
    		Integer.parseInt(str);
    		return true;
    	}
    	catch(NumberFormatException e)
    	{
    		return false;
    	}
     } 

    private boolean isDouble(String str)
    {
    	try
    	{
    		Double.parseDouble(str);
    		return true;
    	}
    	catch(NumberFormatException e)
    	{
    		return false;
    	}
    }
    
    private boolean isNumeric(String str)
    {    	  
    	 return isInteger(str) || isDouble(str);    	 
    }

    /**
     * 
     * @param canvas
     * @param style
     * @param contents
     * @param paint
     */
    private void drawGeneralCell(Canvas canvas, Cell cell, String contents, Paint paint)
    {
        
        contents = adjustGeneralCellContent(contents, paint);
        
        FontMetrics fm = paint.getFontMetrics(); 
        // 文本高度
        int textHeight = (int)(Math.ceil(fm.descent - fm.ascent)); 
         //
         canvas.save();
         //draw area
         canvas.clipRect(cellRect);
         
         // 垂直对齐
         CellStyle cellStyle = cell.getCellStyle();
         switch (cellStyle.getVerticalAlign())
         {
             case CellStyle.VERTICAL_TOP:
                 top += SSConstant.SHEET_SPACETOBORDER;
                 break;
             case CellStyle.VERTICAL_CENTER:
             case CellStyle.VERTICAL_JUSTIFY:
                 top += (mergedCellSize.getHeight() - textHeight) / 2;
                 break;
             case CellStyle.VERTICAL_BOTTOM:
                 top += mergedCellSize.getHeight() - textHeight;                 
             default:
                 break;
         }
         
         //indention
         CellStyle style = cell.getCellStyle();
         int indent = (int)(sheetView.getIndentWidthWithZoom(style.getIndent()));
         if(indent + 2 * SSConstant.SHEET_SPACETOBORDER >= mergedCellSize.getWidth() && !isNumeric(contents))
         {        	 
        	 canvas.restore();
         	return;
         }
         
         //
         int textWidth = (int)(paint.measureText(contents));
         if(textWidth + indent + 2 * SSConstant.SHEET_SPACETOBORDER > mergedCellSize.getWidth())
         {
             left += SSConstant.SHEET_SPACETOBORDER;
             //text width large than cell width, show string "##.."
             int charCnt = (int)((mergedCellSize.getWidth() - 2 * SSConstant.SHEET_SPACETOBORDER) / paint.measureText("#"));
            
             strBuilder.delete(0, strBuilder.length());
             for(int i = 0; i< charCnt; i++)
             {
                 strBuilder.append('#');
             }
             
             contents = strBuilder.toString();
             
             textWidth = (int)(paint.measureText(contents));
         }
         else
         {
             switch (style.getHorizontalAlign())
             {                
                 case CellStyle.ALIGN_LEFT:
                 case CellStyle.ALIGN_FILL:
                 case CellStyle.ALIGN_JUSTIFY:
                 case CellStyle.ALIGN_CENTER_SELECTION:
                     left += indent + SSConstant.SHEET_SPACETOBORDER; 
                     mergedCellSize.setWidth(mergedCellSize.getWidth() - indent);
                     break;   
                 case CellStyle.ALIGN_CENTER:                
                     left += (mergedCellSize.getWidth() - textWidth) / 2;
                     break;
                 case CellStyle.ALIGN_GENERAL:
                 case CellStyle.ALIGN_RIGHT:
                     mergedCellSize.setWidth(mergedCellSize.getWidth() - indent);
                     left += mergedCellSize.getWidth() - (int)(paint.measureText(contents)) - SSConstant.SHEET_SPACETOBORDER;
                     break;
             }
         }
         float x = left - mergedCellSize.getNovisibleWidth() - (cellInfor.getWidth() - cellInfor.getVisibleWidth());
         float y = top - fm.ascent - mergedCellSize.getNoVisibleHeight() - (cellInfor.getHeight() - cellInfor.getVisibleHeight());
        
         canvas.drawText(contents, x, y, paint);
         canvas.restore();    
    }
    
    /**
     * 
     * @param contents
     * @return
     */
    private String scientificToNormal(String contents)
    {
        int index = contents.indexOf('E');
        if(index < 0)
        {
            return contents;
        }
        
        //first part of contents
        String firstPart = contents.substring(0, index);
        //end part of contents
        int endPart = Integer.parseInt(contents.substring(index + 1));
        boolean negative = Double.parseDouble(firstPart) < 0;
        if(Math.abs(endPart) > 10)
        {
            return contents;
        }
        else if(endPart < 0)
        {
            //remove char '0' in the end, eg"3.20"
            if(firstPart.charAt(firstPart.length() - 1) == '0')
            {
                firstPart = firstPart.substring(0, firstPart.length() - 1);
            }
            firstPart = firstPart.replace(".", "");
            
            //cons '0' string
            strBuilder.delete(0, strBuilder.length());
            while(++endPart < 0)
            {
                strBuilder.append("0");
            }
            //cat
            if(!negative)
            {                
                firstPart = "0." + strBuilder.toString() + firstPart;
            }
            else
            {
                firstPart = "-0." + strBuilder.toString() + firstPart.replace("-", "");
            }
        }
        else if(endPart <= 10)
        {
            index = firstPart.indexOf('.');
            
            int decimalPartLen = firstPart.length() - 2;
            if(negative)
            {
                decimalPartLen = firstPart.length() - 3;
            }
            if(decimalPartLen <= endPart)
            {
                firstPart = firstPart.replace(".", "");
                endPart = endPart - decimalPartLen;
                while(endPart > 0)
                {
                    firstPart += "0";
                    endPart--;
                }
            }
            else
            {
                char[] chars = firstPart.toCharArray();
                int i = index;
                index += endPart;
                while(i < index)
                {
                    chars[i] = chars[i + 1];
                    i++;
                }
                chars[i] = '.';
                firstPart = String.valueOf(chars);
            }
        }
        
        return firstPart;
    }
    
    /**
     * 
     * @param canvas
     * @param style
     * @param value
     * @param paint
     */
    private void drawNumericCell(Canvas canvas, Cell cell, String content, Paint paint)
    {
        int oldColor = paint.getColor();
        if(content.length() > 0 && cell.getNumberValue() < 0)
        {
            //color of Negative number            
            paint.setColor(NumericFormatter.getNegativeColor(cell));
        }
        
        
        if(cell.getCellNumericType() == Cell.CELL_TYPE_NUMERIC_ACCOUNTING)
        {
            //accounting cell
            drawAccountCell(canvas, cell, content, paint);
            return;
        }       
        else if(cell.getCellNumericType() == Cell.CELL_TYPE_NUMERIC_GENERAL)
        {            
            drawGeneralCell(canvas, cell, content, paint);
            return;
        }
        
        //the rest cell
        FontMetrics fm = paint.getFontMetrics();        
        // 文本高度
        int textHeight = (int)(Math.ceil(fm.descent - fm.ascent));  
        // 文本宽度
        int textWidth = (int)(paint.measureText(content));
           
        //
        canvas.save();
        //draw area
        canvas.clipRect(cellRect);         
        
        // 垂直对齐
        switch (cell.getCellStyle().getVerticalAlign())
        {
            case CellStyle.VERTICAL_TOP:
                top += SSConstant.SHEET_SPACETOBORDER;
                break;
            case CellStyle.VERTICAL_CENTER:
            case CellStyle.VERTICAL_JUSTIFY:
                top += (mergedCellSize.getHeight() - textHeight) / 2;
                break;
            case CellStyle.VERTICAL_BOTTOM:
                top += mergedCellSize.getHeight() - textHeight;
                
            default:
                break;
        }
        
        //
        int indent = (int)(sheetView.getIndentWidthWithZoom(cell.getCellStyle().getIndent()));
        
        if(textWidth + indent + 2 * SSConstant.SHEET_SPACETOBORDER > mergedCellSize.getWidth())
        {
            //text width large than cell width, show string "##.."
            int charCnt = (int)((mergedCellSize.getWidth() - 2 * SSConstant.SHEET_SPACETOBORDER) / paint.measureText("#"));
           
            strBuilder.delete(0, strBuilder.length());
            for(int i = 0; i< charCnt; i++)
            {
                strBuilder.append('#');
            }
            
            content = strBuilder.toString();
            
            textWidth = (int)(paint.measureText(content));
            left += SSConstant.SHEET_SPACETOBORDER;
        }
        else
        { 
        	short hori = cell.getCellStyle().getHorizontalAlign();
        	if(cell.getCellType() == Cell.CELL_TYPE_BOOLEAN && hori == CellStyle.ALIGN_GENERAL)
        	{
        		hori = CellStyle.ALIGN_CENTER;
        	}
            switch (hori)
            {
                case CellStyle.ALIGN_LEFT:
                case CellStyle.ALIGN_FILL:
                case CellStyle.ALIGN_JUSTIFY:
                case CellStyle.ALIGN_CENTER_SELECTION:
                    left += indent + SSConstant.SHEET_SPACETOBORDER;
                    mergedCellSize.setWidth(mergedCellSize.getWidth() - indent);
                    break;                        
                case CellStyle.ALIGN_RIGHT:
                case CellStyle.ALIGN_GENERAL:
                    mergedCellSize.setWidth(mergedCellSize.getWidth() - indent);
                    left += mergedCellSize.getWidth() - textWidth - SSConstant.SHEET_SPACETOBORDER;
                    break;
                case CellStyle.ALIGN_CENTER:
                    left += (mergedCellSize.getWidth() - textWidth) / 2;
                    
                default:
                    break;
            }      
        }
        
        float x = left - mergedCellSize.getNovisibleWidth() - (cellInfor.getWidth() - cellInfor.getVisibleWidth());
        float y = top - fm.ascent - mergedCellSize.getNoVisibleHeight() - (cellInfor.getHeight() - cellInfor.getVisibleHeight());
        
        if(mergedCellSize.isFrozenColumn())
        {
            x = left - (cellInfor.getWidth() - cellInfor.getVisibleWidth());
        }
        if(mergedCellSize.isFrozenRow())
        {
            y = top - fm.ascent - (cellInfor.getHeight() - cellInfor.getVisibleHeight());
        }
        
        canvas.drawText(content, x, y, paint);
        canvas.restore();
        //
        paint.setColor(oldColor);
    }    
    
    private void initPageProp(IAttributeSet attr, CellStyle style, int width, int height)
    {
        byte verAlign = WPAttrConstant.PAGE_V_TOP;
        switch(style.getVerticalAlign())
        {
            case CellStyle.VERTICAL_TOP:
                verAlign = WPAttrConstant.PAGE_V_TOP;
                break;
            case CellStyle.VERTICAL_CENTER:
            case CellStyle.VERTICAL_JUSTIFY:
                verAlign = WPAttrConstant.PAGE_V_CENTER;
                break;
            case CellStyle.VERTICAL_BOTTOM:
                verAlign = WPAttrConstant.PAGE_V_BOTTOM;
                break;
        }        
        
        //vertical alignment
        AttrManage.instance().setPageVerticalAlign(attr, verAlign);
        // 宽度
        AttrManage.instance().setPageWidth(attr, Math.round(width * MainConstant.PIXEL_TO_TWIPS));
        // 高度
        AttrManage.instance().setPageHeight(attr, Math.round(height * MainConstant.PIXEL_TO_TWIPS));
        // 左边距
        AttrManage.instance().setPageMarginLeft(attr, Math.round(SSConstant.SHEET_SPACETOBORDER * MainConstant.PIXEL_TO_TWIPS));
        // 右边距
        AttrManage.instance().setPageMarginRight(attr, Math.round(SSConstant.SHEET_SPACETOBORDER * MainConstant.PIXEL_TO_TWIPS));
        // 上边距
        AttrManage.instance().setPageMarginTop(attr, 0);
        // 下边框
        AttrManage.instance().setPageMarginBottom(attr, 0);
        
        
    }
    private SectionElement convertToSectionElement(Cell cell, String content, int width, int height)
    {
        // ======== 处理文本 ========
        // 建立章节
        SectionElement secElem = new SectionElement();
        // 开始Offset
        secElem.setStartOffset(0);
        // 属性
        IAttributeSet attr = secElem.getAttribute();
        
        //init page property
        initPageProp(attr, cell.getCellStyle(), width, height);
        
        int pos = processParagraph(secElem, cell, content);
        secElem.setEndOffset(pos);
        
        return secElem;
    }
    
    private int processParagraph( SectionElement secElem, Cell cell, String text)
    {
        int offset = 0;
        String[] ps = text.split("\n");
        for(String p : ps)
        {
            ParagraphElement paraElem = new ParagraphElement();
            paraElem.setStartOffset(offset);
            IAttributeSet attrLayout =  new AttributeSetImpl();            
            ParaAttr.instance().setParaAttribute(cell.getCellStyle(), paraElem.getAttribute(), attrLayout);            
            
            offset = processRun(paraElem, cell, p, offset, attrLayout);
            paraElem.setEndOffset(offset);
            secElem.appendParagraph(paraElem, WPModelConstant.MAIN);
            
        }
        
        
        
        return offset;    
    } 
    
    private int processRun(ParagraphElement paraElem, Cell cell, String p,
        int offset, IAttributeSet attrLayout)
    {
        
        LeafElement leaf = null;
        // 只有一个回车符的段落
        if (p.length() == 0)
        {
            leaf = new CellLeafElement(cell, 0, 0);   
            ((CellLeafElement)leaf).appendNewlineFlag();
            // 属性
            RunAttr.instance().setRunAttribute(sheetView.getCurrentSheet(), cell, leaf.getAttribute(), attrLayout);
            // 开始 offset
            leaf.setStartOffset(offset);
            offset++;
            leaf.setEndOffset(offset);
            paraElem.appendLeaf(leaf);
            return offset;
        }
        
        int len = p.length();
        if (len > 0)
        {
            String content = cell.getSheet().getWorkbook().getSharedString(cell.getStringCellValueIndex());
            if(content == null)
            {
                //string number
                leaf = new LeafElement(p);             
            }
            else
            {
                int start = content.indexOf(p);
                leaf = new CellLeafElement(cell, start, start + p.length());
                
            }
            
            // 属性
            RunAttr.instance().setRunAttribute(sheetView.getCurrentSheet(), cell, leaf.getAttribute(), attrLayout);
            // 开始 offset
            leaf.setStartOffset(offset);
            offset += len;
            // 结束 offset
            leaf.setEndOffset(offset);
            paraElem.appendLeaf(leaf);
        }
        
        if (leaf != null)
        {
            if(leaf instanceof CellLeafElement)
            {
                ((CellLeafElement)leaf).appendNewlineFlag();
                offset++;
                leaf.setEndOffset(offset);
            }
            else
            {
                leaf.setText(leaf.getText(null) + "\n");
            }
            
        }
        return offset;
    }
    
    private void drawWrapText(Canvas canvas, Cell cell, String content)
    {        
        STRoot root = cell.getSTRoot();
        if(root == null)
        {
            Rect rect = ModelUtil.instance().getCellAnchor(
                sheetView.getCurrentSheet(), cell.getRowNumber(), cell.getColNumber());
            
            SectionElement elem = convertToSectionElement(cell, content, rect.width(), rect.height());
            
            if (elem.getEndOffset() - elem.getStartOffset() == 0)
            {
                return;
            }       
            
            IDocument doc = new STDocument();
            doc.appendSection(elem);
            root = new STRoot(sheetView.getSpreadsheet().getEditor(), doc);
            root.setWrapLine(true);
            root.doLayout();
            
            cell.setSTRoot(root);
        }
        
        root.draw(canvas, 
            Math.round(left - mergedCellSize.getNovisibleWidth() - (cellInfor.getWidth() - cellInfor.getVisibleWidth())),
            Math.round(top - mergedCellSize.getNoVisibleHeight() - (cellInfor.getHeight() - cellInfor.getVisibleHeight())),
            sheetView.getZoom());
    }    

    /**
     * 
     * @param canvas
     * @param row
     * @param col
     * @param style
     * @param contents
     * @param paint
     */
    private void drawNonWrapText(Canvas canvas, Cell cell, String contents, Paint paint)
    {        
        FontMetrics fm = paint.getFontMetrics();
        // 文本宽度
        int textWidth = (int)(paint.measureText(contents));
        // 文本高度
        int textHeight = (int)(Math.ceil(fm.descent - fm.ascent)); 
        
        CellStyle style = cell.getCellStyle();
        //horizontal, indent
        int indent = (int)(sheetView.getIndentWidthWithZoom(style.getIndent()));
        if(indent + 2 * SSConstant.SHEET_SPACETOBORDER >= mergedCellSize.getWidth())
        {
        	canvas.restore();
        	return;
        }
        else
        {
        	if(cell.getCellNumericType() == Cell.CELL_TYPE_NUMERIC_SIMPLEDATE 
        			&& textWidth + indent + 2 * SSConstant.SHEET_SPACETOBORDER > mergedCellSize.getWidth())
            {
        		//date&time text width large than cell width, show string "##.."
                int charCnt = (int)((mergedCellSize.getWidth() - 2 * SSConstant.SHEET_SPACETOBORDER) / paint.measureText("#"));
               
                strBuilder.delete(0, strBuilder.length());
                for(int i = 0; i< charCnt; i++)
                {
                    strBuilder.append('#');
                }
                
                contents = strBuilder.toString();
                
                textWidth = (int)(paint.measureText(contents));
                indent = 0;
            }
        	
            canvas.save();            
            //draw area            
            canvas.clipRect(cellRect);
            
            short horiAlign = style.getHorizontalAlign();
            if(horiAlign == CellStyle.ALIGN_GENERAL 
            		&& cell.getCellNumericType() == Cell.CELL_TYPE_NUMERIC_SIMPLEDATE)
            {
            	horiAlign = CellStyle.ALIGN_RIGHT;
            }
            
            switch (horiAlign)
            {
                case CellStyle.ALIGN_GENERAL:
                case CellStyle.ALIGN_LEFT:
                case CellStyle.ALIGN_FILL:
                case CellStyle.ALIGN_JUSTIFY:
                case CellStyle.ALIGN_CENTER_SELECTION:
                    left += indent + SSConstant.SHEET_SPACETOBORDER; 
                    mergedCellSize.setWidth(mergedCellSize.getWidth() - indent);
                    
                    if(textWidth +  SSConstant.SHEET_SPACETOBORDER > mergedCellSize.getWidth())
                    {
                        float[] widths = new float[contents.length()];                        
                        paint.getTextWidths(contents, 0, contents.length(), widths);
                        int s = 0;
                        float sum = widths[0];
                        while(sum < mergedCellSize.getWidth() - SSConstant.SHEET_SPACETOBORDER)
                        {
                            sum += widths[++s];
                        }                       
                        
                        contents = contents.substring(0, s);
                    }
                    break;                
                case CellStyle.ALIGN_RIGHT:
                    mergedCellSize.setWidth(mergedCellSize.getWidth() - indent);
                    if(textWidth + SSConstant.SHEET_SPACETOBORDER > mergedCellSize.getWidth())
                    {
                        float[] widths = new float[contents.length()];                        
                        paint.getTextWidths(contents, 0, contents.length(), widths);
                        
                        
                        int s = contents.length();
                        float sum = 0;
                        while(sum < mergedCellSize.getWidth() - SSConstant.SHEET_SPACETOBORDER)
                        {
                            sum += widths[--s];
                        }
                        
                        contents = contents.substring(s + 1, contents.length());
                        textWidth = (int)(paint.measureText(contents));
                    }
                    left += mergedCellSize.getWidth() - textWidth - SSConstant.SHEET_SPACETOBORDER;
                    break;
                case CellStyle.ALIGN_CENTER:
                    left += (mergedCellSize.getWidth() - textWidth) / 2;
                    
                default:
                    break;
            }

            // 垂直对齐
            switch (style.getVerticalAlign())
            {
                case CellStyle.VERTICAL_TOP:
                    top += SSConstant.SHEET_SPACETOBORDER;
                    break;
                case CellStyle.VERTICAL_CENTER:
                case CellStyle.VERTICAL_JUSTIFY:
                    top += (mergedCellSize.getHeight() - textHeight) / 2;
                    break;
                case CellStyle.VERTICAL_BOTTOM:
                    top += mergedCellSize.getHeight() - textHeight;
                    
                default:
                    break;
            }  
            float x = left - mergedCellSize.getNovisibleWidth() - (cellInfor.getWidth() - cellInfor.getVisibleWidth());
            float y = top - fm.ascent - mergedCellSize.getNoVisibleHeight() - (cellInfor.getHeight() - cellInfor.getVisibleHeight());

            if(cell.getExpandedRangeAddressIndex() >= 0)
            {
                /**
                 * not draw content here, it may be covered by next blank cell's background
                 */
                //canvas.drawText(contents, x, y, paint);       
                sheetView.addExtendCell(cell, new RectF(cellRect), x, y, contents);
            }
            else
            {
                
                canvas.drawText(contents, x, y, paint);
            }
 
            canvas.restore();
        }  
    }
    
    /**
     * 
     */
    public void dispose()
    {
        sheetView = null;
        
        if(cellBorderView != null)
        {
            cellBorderView.dispose();
            cellBorderView = null;
        }
        
        cellRect = null;
        
        if(mergedCellSize != null)
        {
            mergedCellSize.dispose();
            mergedCellSize = null;
        }
        
        if(mergedCellMgr !=  null)
        {
            mergedCellMgr.dispose();
            mergedCellMgr = null;
        }
        
        cellInfor = null;
        
        strBuilder = null;
        if(tableStyleKit != null)
        {
            tableStyleKit.dispose();
            tableStyleKit = null;
        }
    }
    
    //
    private SheetView sheetView = null;
    
    //border
    private CellBorderView cellBorderView;  
    
    private float left;
    private float top;   
    
    //merged cell
    private MergeCell mergedCellSize = new MergeCell();
    private MergeCellMgr mergedCellMgr = new MergeCellMgr();
    
    private DrawingCell cellInfor;
    
    private RectF cellRect;
    private boolean numericCellAlignRight;
    
    ////////////////////////temp
    private StringBuilder strBuilder = new StringBuilder();
    private TableStyleKit tableStyleKit = new TableStyleKit();
}
