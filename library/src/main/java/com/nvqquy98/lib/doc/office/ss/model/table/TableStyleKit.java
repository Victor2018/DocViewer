/*
 * 文件名称:          TableStyleKit.java
 *  
 * 编译器:            android2.2
 * 时间:              下午2:06:58
 */
package com.nvqquy98.lib.doc.office.ss.model.table;

import java.util.Map;

import com.nvqquy98.lib.doc.office.constant.SchemeClrConstant;
import com.nvqquy98.lib.doc.office.ss.util.ColorUtil;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2013-4-18
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class TableStyleKit
{/**
     * scheme color name
     */
    private static String[] schemeClrName = {
        SchemeClrConstant.SCHEME_ACCENT1,
        SchemeClrConstant.SCHEME_ACCENT2,
        SchemeClrConstant.SCHEME_ACCENT3,
        SchemeClrConstant.SCHEME_ACCENT4,
        SchemeClrConstant.SCHEME_ACCENT5,
        SchemeClrConstant.SCHEME_ACCENT6
    };
    
    public SSTableStyle getTableStyle(String tableName, Map<String, Integer> schemeColor)
    {
    	try
    	{

//          String regEx="[^0-9]";   
//          Pattern p = Pattern.compile(regEx);   
//          Matcher m = p.matcher(tableName); 
//          tableName = m.replaceAll("").trim();
          
          if(tableName == null || tableName.length() == 0)
          {
              return null;
          }
          
          if(tableName.contains("Light"))
          {
              //Light
              tableName = tableName.substring("TableStyleLight".length()).split(" ")[0];
              int id = Integer.parseInt(tableName);
              switch((id - 1) / 7)
              {
                  case 0: //id = 1,2,3,4,5,6,7
                      return getTableStyleLight1_7(getSchemeColor(schemeColor, id));
                    
                  case 1: //id = 8,9,10,11,12,13,14
                      return getTableStyleLight8_14(getSchemeColor(schemeColor, id));
                      
                  case 2: //id = 15,16,17,18,19,20,21
                      return getTableStyleLight15_21(getSchemeColor(schemeColor, id));                    
              }
          }
          else if(tableName.contains("Medium"))
          {
              //Medium
              tableName = tableName.substring("TableStyleMedium".length()).split(" ")[0];
              int id = Integer.parseInt(tableName);
              switch((id - 1) / 7)
              {
                  case 0: //id = 1,2,3,4,5,6,7
                      return getTableStyleMedium1_7(getSchemeColor(schemeColor, id));
                    
                  case 1: //id = 8,9,10,11,12,13,14
                      return getTableStyleMedium8_14(getSchemeColor(schemeColor, id));
                      
                  case 2: //id = 15,16,17,18,19,20,21
                      return getTableStyleMedium15_21(getSchemeColor(schemeColor, id));
                      
                  case 3: //id = 22,23,24,25,26,27,28
                      return getTableStyleMedium22_28(getSchemeColor(schemeColor, id));  
              }
          }
          else
          {
              //Dark
              tableName = tableName.substring("TableStyleDark".length()).split(" ")[0];
              int id = Integer.parseInt(tableName);
              switch(id)
              {
                  case 1:
                  case 2:
                  case 3:
                  case 4:
                  case 5:
                  case 6:
                  case 7:
                      return getTableStyleDark1_7(getSchemeColor(schemeColor, id));
                  case 8:
                      return getTableStyleDark8();
                      
                  case 9:
                  case 10:
                  case 11:
                      return getTableStyleDark9_11(getSchemeColor(schemeColor, (id - 8) * 2 + 1), 
                          getSchemeColor(schemeColor, (id - 8) * 2));
              }
          }
    	}
    	catch(Exception e)
    	{
    		
    	}
        return null;
    }
    
    /**
     * 
     * @param schemeColor
     * @param id
     * @return
     */
    private int getSchemeColor(Map<String, Integer> schemeColor, int id)
    {
        id %= 7;
        if(id == 1)
        {
            //black
            return 0xFF000000;
        }
        else
        {
            return schemeColor.get(schemeClrName[(id - 2 + 7) % 7]);
        }
    }
    
    /**
     * Table Style Light 1-7
     * @param schemeColor
     * @return
     */
    private SSTableStyle getTableStyleLight1_7(int schemeColor)
    {
        if(tableStyleLight1_7 == null)
        {
            tableStyleLight1_7 = new SSTableStyle();
            //first row, last row
            SSTableCellStyle cellstyle = new SSTableCellStyle(0xFFFFFFFF);
            cellstyle.setFontColor(schemeColor);
            cellstyle.setBorderColor(schemeColor);
            
            tableStyleLight1_7.setFirstRow(cellstyle);
            tableStyleLight1_7.setLastRow(cellstyle);
            
            //band1H, band1V
            int color = ColorUtil.instance().getColorWithTint(schemeColor, 0.8f);
            cellstyle = new SSTableCellStyle(color);
            cellstyle.setFontColor(schemeColor);
            
            tableStyleLight1_7.setBand1H(cellstyle);
            tableStyleLight1_7.setBand1V(cellstyle);
            
            //band2H, band2V
            cellstyle = new SSTableCellStyle(0xFFFFFFFF);
            cellstyle.setFontColor(schemeColor);
            
            tableStyleLight1_7.setBand2H(cellstyle);
            tableStyleLight1_7.setBand2V(cellstyle);
        }
        else
        {
            //first row, last row
            SSTableCellStyle cellstyle = tableStyleLight1_7.getFirstRow();
            cellstyle.setFontColor(schemeColor);
            cellstyle.setBorderColor(schemeColor);
            tableStyleLight1_7.setFirstRow(cellstyle);
            tableStyleLight1_7.setLastRow(cellstyle);
            
            //band1H, band1V
            int color = ColorUtil.instance().getColorWithTint(schemeColor, 0.8f);
            cellstyle = tableStyleLight1_7.getBand1H();
            cellstyle.setFillColor(color);
            cellstyle.setFontColor(schemeColor);
            
            tableStyleLight1_7.setBand1H(cellstyle);
            tableStyleLight1_7.setBand1V(cellstyle);
            
            //band2H, band2V
            cellstyle = tableStyleLight1_7.getBand2H();
            cellstyle.setFontColor(schemeColor);
            
            tableStyleLight1_7.setBand2H(cellstyle);
            tableStyleLight1_7.setBand2V(cellstyle);
        }
        return tableStyleLight1_7;
    }
    
    /**
     * Table Style Light 8-14
     * @param schemeColor
     * @return
     */
    private SSTableStyle getTableStyleLight8_14(int schemeColor)
    {
        if(tableStyleLight8_14 == null)
        {
            tableStyleLight8_14 = new SSTableStyle();
            //first row
            SSTableCellStyle cellstyle = new SSTableCellStyle(schemeColor);
            cellstyle.setFontColor(0xFFFFFFFF);
            cellstyle.setBorderColor(schemeColor);
            
            tableStyleLight8_14.setFirstRow(cellstyle);
            
            //band1H, band1V, last row, band2H, band2V
            cellstyle = new SSTableCellStyle(0xFFFFFFFF);
            cellstyle.setBorderColor(schemeColor);
            
            tableStyleLight8_14.setLastRow(cellstyle);
            tableStyleLight8_14.setBand1H(cellstyle);
            tableStyleLight8_14.setBand1V(cellstyle);
            tableStyleLight8_14.setBand2H(cellstyle);
            tableStyleLight8_14.setBand2V(cellstyle);
        }
        else
        {
            //first row
            SSTableCellStyle cellstyle = tableStyleLight8_14.getFirstRow();
            cellstyle.setFillColor(schemeColor);
            cellstyle.setBorderColor(schemeColor);
            
            tableStyleLight8_14.setFirstRow(cellstyle);
            
            //band1H, band1V, last row, band2H, band2V
            cellstyle = tableStyleLight8_14.getBand1H();
            cellstyle.setBorderColor(schemeColor);
            
            tableStyleLight8_14.setLastRow(cellstyle);
            tableStyleLight8_14.setBand1H(cellstyle);
            tableStyleLight8_14.setBand1V(cellstyle);
            tableStyleLight8_14.setBand2H(cellstyle);
            tableStyleLight8_14.setBand2V(cellstyle);
        }
        return tableStyleLight8_14;
    }
    
    /**
     * Table Style Light 15-21
     * @param schemeColor
     * @return
     */
    private SSTableStyle getTableStyleLight15_21(int schemeColor)
    {
        if(tableStyleLight15_21 == null)
        {
            tableStyleLight15_21 = new SSTableStyle();
            //first row, last row
            SSTableCellStyle cellstyle = new SSTableCellStyle(0xFFFFFFFF);
            cellstyle.setBorderColor(schemeColor);
            
            tableStyleLight15_21.setFirstRow(cellstyle);
            tableStyleLight15_21.setLastRow(cellstyle);
            
            //band1H, band1V
            int color = ColorUtil.instance().getColorWithTint(schemeColor, 0.8f);
            cellstyle = new SSTableCellStyle(color);
            cellstyle.setBorderColor(schemeColor);
            
            tableStyleLight15_21.setBand1H(cellstyle);
            tableStyleLight15_21.setBand1V(cellstyle);
            
            //band2H, band2V
            cellstyle = new SSTableCellStyle(0xFFFFFFFF);
            cellstyle.setBorderColor(schemeColor);
            
            tableStyleLight15_21.setBand2H(cellstyle);
            tableStyleLight15_21.setBand2V(cellstyle);
        }
        else
        {
            //first row, last row
            SSTableCellStyle cellstyle = tableStyleLight15_21.getFirstRow();
            cellstyle.setBorderColor(schemeColor);
            tableStyleLight15_21.setFirstRow(cellstyle);
            tableStyleLight15_21.setLastRow(cellstyle);
            
            //band1H, band1V
            int color = ColorUtil.instance().getColorWithTint(schemeColor, 0.8f);
            cellstyle = tableStyleLight15_21.getBand1H();
            cellstyle.setFillColor(color);
            cellstyle.setBorderColor(schemeColor);
            
            tableStyleLight15_21.setBand1H(cellstyle);
            tableStyleLight15_21.setBand1V(cellstyle);
            
            //band2H, band2V
            cellstyle = tableStyleLight15_21.getBand2H();
            cellstyle.setBorderColor(schemeColor);
            
            tableStyleLight15_21.setBand2H(cellstyle);
            tableStyleLight15_21.setBand2V(cellstyle);
        }
        return tableStyleLight15_21;
    }
    
    /**
     * Table Style Medium 1-7
     * @param schemeColor
     * @return
     */
    private SSTableStyle getTableStyleMedium1_7(int schemeColor)
    {
        if(tableStyleMedium1_7 == null)
        {
            tableStyleMedium1_7 = new SSTableStyle();
            //header row
            SSTableCellStyle cellstyle = new SSTableCellStyle(schemeColor);
            cellstyle.setFontColor(0xFFFFFFFF);
            cellstyle.setBorderColor(schemeColor);
            tableStyleMedium1_7.setFirstRow(cellstyle);
            
            //band1H, band1V
            int color = ColorUtil.instance().getColorWithTint(schemeColor, 0.8f);
            cellstyle = new SSTableCellStyle(color);
            cellstyle.setBorderColor(schemeColor);
            tableStyleMedium1_7.setBand1H(cellstyle);
            tableStyleMedium1_7.setBand1V(cellstyle);
            
            //band12H, band2V
            cellstyle = new SSTableCellStyle(0xFFFFFFFF);
            cellstyle.setBorderColor(schemeColor);        
            tableStyleMedium1_7.setBand2H(cellstyle);
            tableStyleMedium1_7.setBand2V(cellstyle);
        }
        else
        {
            SSTableCellStyle cellstyle = tableStyleMedium1_7.getFirstRow();
            cellstyle.setFillColor(schemeColor);
            cellstyle.setBorderColor(schemeColor);
            cellstyle.setFontColor(0xFFFFFFFF);
            tableStyleMedium1_7.setFirstRow(cellstyle);
            
            //band1H, band1V
            int color = ColorUtil.instance().getColorWithTint(schemeColor, 0.8f);
            cellstyle = tableStyleMedium1_7.getBand1H();
            cellstyle.setFillColor(color);
            cellstyle.setBorderColor(schemeColor);
            
            tableStyleMedium1_7.setBand1H(cellstyle);
            tableStyleMedium1_7.setBand1V(cellstyle);
            
            //band12H, band2V
            cellstyle = tableStyleMedium1_7.getBand2H();;
            cellstyle.setBorderColor(schemeColor);           
            tableStyleMedium1_7.setBand2H(cellstyle);
            tableStyleMedium1_7.setBand2V(cellstyle);
        }
        
        return tableStyleMedium1_7;
    }
    
    /**
     * Table Style Medium 8-14
     * @param schemeColor
     * @return
     */
    private SSTableStyle getTableStyleMedium8_14(int schemeColor)
    {
        if(tableStyleMedium8_14 == null)
        {
            tableStyleMedium8_14 = new SSTableStyle();
            //first row, last row, first col, last col
            SSTableCellStyle cellstyle = new SSTableCellStyle(schemeColor);
            cellstyle.setBorderColor(0xFFFFFFFF);
            cellstyle.setFontColor(0xFFFFFFFF);
            tableStyleMedium8_14.setFirstRow(cellstyle);            
            tableStyleMedium8_14.setFirstCol(cellstyle);
            tableStyleMedium8_14.setLastCol(cellstyle);
            tableStyleMedium8_14.setLastRow(cellstyle);
            
            //band1H, band1V
            int color = ColorUtil.instance().getColorWithTint(schemeColor, 0.6f);
            cellstyle = new SSTableCellStyle(color);
            cellstyle.setBorderColor(0xFFFFFFFF);
            tableStyleMedium8_14.setBand1H(cellstyle);
            tableStyleMedium8_14.setBand1V(cellstyle);
            
            //band12H, band2V
            color = ColorUtil.instance().getColorWithTint(schemeColor, 0.8f);
            cellstyle = new SSTableCellStyle(color);
            cellstyle.setBorderColor(0xFFFFFFFF);
            
            tableStyleMedium8_14.setBand2H(cellstyle);
            tableStyleMedium8_14.setBand2V(cellstyle);
        }
        else
        {
            SSTableCellStyle cellstyle = tableStyleMedium8_14.getFirstRow();
            cellstyle.setFillColor(schemeColor);
            cellstyle.setBorderColor(0xFFFFFFFF);
            cellstyle.setFontColor(0xFFFFFFFF);
            tableStyleMedium8_14.setFirstRow(cellstyle);            
            tableStyleMedium8_14.setFirstCol(cellstyle);
            tableStyleMedium8_14.setLastCol(cellstyle);
            tableStyleMedium8_14.setLastRow(cellstyle);
            
            //band1H, band1V
            int color = ColorUtil.instance().getColorWithTint(schemeColor, 0.6f);
            cellstyle = tableStyleMedium8_14.getBand1H();
            cellstyle.setFillColor(color);
            cellstyle.setBorderColor(0xFFFFFFFF);
            
            tableStyleMedium8_14.setBand1H(cellstyle);
            tableStyleMedium8_14.setBand1V(cellstyle);
            
            //band12H, band2V
            color = ColorUtil.instance().getColorWithTint(schemeColor, 0.8f);
            cellstyle = tableStyleMedium8_14.getBand2H();
            cellstyle.setFillColor(color);
            cellstyle.setBorderColor(0xFFFFFFFF);
            
            tableStyleMedium8_14.setBand2H(cellstyle);
            tableStyleMedium8_14.setBand2V(cellstyle);
        }
        
        return tableStyleMedium8_14;
    }
    
    /**
     * Table Style Medium 15-21
     * @param schemeColor
     * @return
     */
    private SSTableStyle getTableStyleMedium15_21(int schemeColor)
    {
        if(tableStyleMedium15_21 == null)
        {
            tableStyleMedium15_21 = new SSTableStyle();
            //first row, first col, last col
            SSTableCellStyle cellstyle = new SSTableCellStyle(schemeColor);
            cellstyle.setFontColor(0xFFFFFFFF);
            tableStyleMedium15_21.setFirstRow(cellstyle);            
            tableStyleMedium15_21.setFirstCol(cellstyle);
            tableStyleMedium15_21.setLastCol(cellstyle);
            
            //band1H, band1V
            cellstyle = new SSTableCellStyle(0xFFD8D8D8);
            tableStyleMedium15_21.setBand1H(cellstyle);
            tableStyleMedium15_21.setBand1V(cellstyle);
            
            //band12H, band2V
            cellstyle = new SSTableCellStyle(0xFFFFFFFF);
            tableStyleMedium15_21.setBand2H(cellstyle);
            tableStyleMedium15_21.setBand2V(cellstyle);
        }
        else
        {
            SSTableCellStyle cellStyle = tableStyleMedium15_21.getFirstRow();
            cellStyle.setFillColor(schemeColor);
                    
            tableStyleMedium15_21.setFirstCol(cellStyle);
            tableStyleMedium15_21.setLastCol(cellStyle);
        }
        
        return tableStyleMedium15_21;
    }
    
    private SSTableStyle getTableStyleMedium22_28(int schemeColor)
    {
        if(tableStyleMedium22_28 == null)
        {
            tableStyleMedium22_28 = new SSTableStyle();
            // first col, band1H, band1V
            int color = ColorUtil.instance().getColorWithTint(schemeColor, 0.6f);
            SSTableCellStyle cellstyle = new SSTableCellStyle(color);
            cellstyle.setBorderColor(schemeColor);
            tableStyleMedium22_28.setBand1H(cellstyle);
            tableStyleMedium22_28.setBand1V(cellstyle);
            
            //last row, first row, band12H, band2V
            color = ColorUtil.instance().getColorWithTint(schemeColor, 0.8f);
            cellstyle = new SSTableCellStyle(color);
            cellstyle.setBorderColor(schemeColor);
            tableStyleMedium22_28.setFirstRow(cellstyle);
            tableStyleMedium22_28.setLastRow(cellstyle);            
            tableStyleMedium22_28.setBand2H(cellstyle);
            tableStyleMedium22_28.setBand2V(cellstyle);
        }
        else
        {
            
            int color = ColorUtil.instance().getColorWithTint(schemeColor, 0.6f);
            SSTableCellStyle cellstyle = tableStyleMedium22_28.getBand1H();
            cellstyle.setFillColor(color);
            cellstyle.setBorderColor(schemeColor);
            
            tableStyleMedium22_28.setBand1H(cellstyle);
            tableStyleMedium22_28.setBand1V(cellstyle);
            
            //last row, first row, band12H, band2V
            color = ColorUtil.instance().getColorWithTint(schemeColor, 0.8f);
            cellstyle = tableStyleMedium22_28.getFirstRow();
            cellstyle.setFillColor(color);
            cellstyle.setBorderColor(schemeColor);
            
            tableStyleMedium22_28.setFirstRow(cellstyle);
            tableStyleMedium22_28.setLastRow(cellstyle);            
            tableStyleMedium22_28.setBand2H(cellstyle);
            tableStyleMedium22_28.setBand2V(cellstyle);
        }
        
        return tableStyleMedium22_28;
    }
    
    private SSTableStyle getTableStyleDark1_7(int schemeColor)
    {
        if(tableStyleDark1_7 == null)
        {
            tableStyleDark1_7 = new SSTableStyle();
            //first row
            SSTableCellStyle cellstyle = new SSTableCellStyle(0xFF000000);
            cellstyle.setFontColor(0xFFFFFFFF);
            cellstyle.setBorderColor(0xFFFFFFFF);
            tableStyleDark1_7.setFirstRow(cellstyle);
            
            //last row
            int color = 0;
            if((schemeColor & 0xFFFFFF) == 0)
            {
                color = ColorUtil.instance().getColorWithTint(schemeColor, 0.15f);
            }
            else
            {
                color = ColorUtil.instance().getColorWithTint(schemeColor, -0.5f);
            } 
            cellstyle = new SSTableCellStyle(color);
            cellstyle.setFontColor(0xFFFFFFFF);
            cellstyle.setBorderColor(0xFFFFFFFF);
            tableStyleDark1_7.setLastRow(cellstyle);
            
            //band1H, band1V
            if((schemeColor & 0xFFFFFF) == 0)
            {
                color = ColorUtil.instance().getColorWithTint(schemeColor, 0.25f);
            }
            else
            {
                color = ColorUtil.instance().getColorWithTint(schemeColor, -0.25f);
            }
            cellstyle = new SSTableCellStyle(color);
            cellstyle.setFontColor(0xFFFFFFFF);
            tableStyleDark1_7.setBand1H(cellstyle);
            tableStyleDark1_7.setBand1V(cellstyle);
            
            //band2H, band2V
            if((schemeColor & 0xFFFFFF) == 0)
            {
                color = ColorUtil.instance().getColorWithTint(schemeColor, 0.5f);
            }
            else
            {
                color = schemeColor;
            }
            cellstyle = new SSTableCellStyle(color);
            cellstyle.setFontColor(0xFFFFFFFF);
            tableStyleDark1_7.setBand2H(cellstyle);
            tableStyleDark1_7.setBand2V(cellstyle);
        }
        else
        {
            //last row
            int color = 0;
            if((schemeColor & 0xFFFFFF) == 0)
            {
                color = ColorUtil.instance().getColorWithTint(schemeColor, 0.15f);
            }
            else
            {
                color = ColorUtil.instance().getColorWithTint(schemeColor, -0.5f);
            } 
            SSTableCellStyle cellstyle = tableStyleDark1_7.getLastRow();
            cellstyle.setFillColor(color);
            tableStyleDark1_7.setLastRow(cellstyle);
            
            //band1H, band1V
            if((schemeColor & 0xFFFFFF) == 0)
            {
                color = ColorUtil.instance().getColorWithTint(schemeColor, 0.25f);
            }
            else
            {
                color = ColorUtil.instance().getColorWithTint(schemeColor, -0.25f);
            }
            cellstyle = tableStyleDark1_7.getBand1H();
            cellstyle.setFillColor(color);
            
            tableStyleDark1_7.setBand1H(cellstyle);
            tableStyleDark1_7.setBand1V(cellstyle);
            
            //band2H, band2V
            if((schemeColor & 0xFFFFFF) == 0)
            {
                color = ColorUtil.instance().getColorWithTint(schemeColor, 0.5f);
            }
            else
            {
                color = schemeColor;
            }
            cellstyle = tableStyleDark1_7.getBand2H();
            cellstyle.setFillColor(color);
            
            tableStyleDark1_7.setBand2H(cellstyle);
            tableStyleDark1_7.setBand2V(cellstyle);
        }
        
        return tableStyleDark1_7;
    }
    
    /**
     * 
     * @return
     */
    private SSTableStyle getTableStyleDark8()
    {
        if(tableStyleDark8 == null)
        {
            tableStyleDark8 = new SSTableStyle();
            //first row
            SSTableCellStyle cellstyle = new SSTableCellStyle(0xFF000000);
            cellstyle.setFontColor(0xFFFFFFFF);
            tableStyleDark8.setFirstRow(cellstyle);
            
            //band1H, band1V
            cellstyle = new SSTableCellStyle(0xFFA5A5A5);
            tableStyleDark8.setBand1H(cellstyle);
            tableStyleDark8.setBand1V(cellstyle);
            
            //band2H, band2V
            cellstyle = new SSTableCellStyle(0xFFD8D8D8);
            tableStyleDark8.setBand2H(cellstyle);
            tableStyleDark8.setBand2V(cellstyle);
        }        
        
        return tableStyleDark8;
    }
    
    /**
     * Table Style Dark 9-11
     * @param headerColor
     * @param bodyColor
     * @return
     */
    private SSTableStyle getTableStyleDark9_11(int headerColor, int bodyColor)
    {
        if(tableStyleDark9_11 == null)
        {
            tableStyleDark9_11 = new SSTableStyle();
            //first row
            SSTableCellStyle cellstyle = new SSTableCellStyle(headerColor);
            cellstyle.setFontColor(0xFFFFFFFF);
            tableStyleDark9_11.setFirstRow(cellstyle);
            
            //band1H, band1V
            int color = ColorUtil.instance().getColorWithTint(bodyColor, 0.6f);
            cellstyle = new SSTableCellStyle(color);
            tableStyleDark9_11.setBand1H(cellstyle);
            tableStyleDark9_11.setBand1V(cellstyle);
            
            //band2H, band2V
            color = ColorUtil.instance().getColorWithTint(bodyColor, 0.8f);
            cellstyle = new SSTableCellStyle(color);
            tableStyleDark9_11.setBand2H(cellstyle);
            tableStyleDark9_11.setBand2V(cellstyle);
        }
        else
        {
            SSTableCellStyle cellstyle = tableStyleDark9_11.getFirstRow();
            cellstyle.setFillColor(headerColor);
            tableStyleDark9_11.setFirstRow(cellstyle);
            
            //band1H, band1V
            int color = ColorUtil.instance().getColorWithTint(bodyColor, 0.6f);
            cellstyle = tableStyleDark9_11.getBand1H();
            cellstyle.setFillColor(color);
            tableStyleDark9_11.setBand1H(cellstyle);
            tableStyleDark9_11.setBand1V(cellstyle);
            
            //band2H, band2V
            color = ColorUtil.instance().getColorWithTint(bodyColor, 0.8f);
            cellstyle = tableStyleDark9_11.getBand2H();
            cellstyle.setFillColor(color);
            
            tableStyleDark9_11.setBand2H(cellstyle);
            tableStyleDark9_11.setBand2V(cellstyle);
        }
        
        return tableStyleDark9_11;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        
    }
    
    /**
     * light table style
     */
    private SSTableStyle tableStyleLight1_7;
    private SSTableStyle tableStyleLight8_14;
    private SSTableStyle tableStyleLight15_21;
    /**
     * medium table style
     */
    private SSTableStyle tableStyleMedium1_7;
    private SSTableStyle tableStyleMedium8_14;
    private SSTableStyle tableStyleMedium15_21;
    private SSTableStyle tableStyleMedium22_28;
    /**
     * dark table style
     */
    private SSTableStyle tableStyleDark1_7;
    private SSTableStyle tableStyleDark8;
    private SSTableStyle tableStyleDark9_11;
}
