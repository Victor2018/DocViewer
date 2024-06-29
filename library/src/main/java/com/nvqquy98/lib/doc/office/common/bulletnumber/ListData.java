/*
 * 锟侥硷拷锟斤拷锟斤拷:          ListData.java
 * 锟斤拷权锟斤拷锟斤拷@2001-2014 锟斤拷锟斤拷锟斤拷锟捷ｏ拷锟狡硷拷锟斤拷锟睫癸拷司
 * 锟斤拷锟斤拷锟斤拷:            android2.2
 * 时锟斤拷:              锟斤拷锟斤拷10:49:31
 */
package com.nvqquy98.lib.doc.office.common.bulletnumber;

public class ListData
{
    
    /**
     * @return Returns the listID.
     */
    public int getListID()
    {
        return listID;
    }
    /**
     * @param listID The listID to set.
     */
    public void setListID(int listID)
    {
        this.listID = listID;
    }

    /**
     * @return Returns the simpleList.
     */
    public byte getSimpleList()
    {
        return simpleList;
    }
    /**
     * @param simpleList The simpleList to set.
     */
    public void setSimpleList(byte simpleList)
    {
        this.simpleList = simpleList;
    }

    /**
     * @return Returns the linkStyle.
     */
    public short[] getLinkStyle()
    {
        return linkStyle;
    }
    /**
     * @param linkStyle The linkStyle to set.
     */
    public void setLinkStyle(short[] linkStyle)
    {
        this.linkStyle = linkStyle;
    }

    /**
     * @return Returns the isNumber.
     */
    public boolean isNumber()
    {
        return isNumber;
    }
    /**
     * @param isNumber The isNumber to set.
     */
    public void setNumber(boolean isNumber)
    {
        this.isNumber = isNumber;
    }

    /**
     * @return Returns the levels.
     */
    public ListLevel[] getLevels()
    {
        return levels;
    }
    /**
     * @param levels The levels to set.
     */
    public void setLevels(ListLevel[] levels)
    {
        this.levels = levels;
    }
    
    /**
     * 
     * @param level
     * @return
     */
    public ListLevel getLevel(int level)
    {
        if(level < levels.length)
        {
            return levels[level];
        }
        return null;
    }
    
    /**
     * @return Returns the preParaLevel.
     */
    public byte getPreParaLevel()
    {
        return preParaLevel;
    }
    /**
     * @param preParaLevel The preParaLevel to set.
     */
    public void setPreParaLevel(byte preParaLevel)
    {
        this.preParaLevel = preParaLevel;
    }
    
    /**
     * @return Returns the normalPreParaLevel.
     */
    public byte getNormalPreParaLevel()
    {
        return normalPreParaLevel;
    }
    /**
     * @param normalPreParaLevel The normalPreParaLevel to set.
     */
    public void setNormalPreParaLevel(byte normalPreParaLevel)
    {
        this.normalPreParaLevel = normalPreParaLevel;
    }
    
    /**
     * 
     */
    public void resetForNormalView()
    {
        if (levels != null)
        {
            for(ListLevel level : levels)
            {
                level.setNormalParaCount(0);
            }
        }
    }    

    /**
     * @return Returns the linkStyleID.
     */
    public short getLinkStyleID()
    {
        return linkStyleID;
    }
    /**
     * @param linkStyleID The linkStyleID to set.
     */
    public void setLinkStyleID(short linkStyleID)
    {
        this.linkStyleID = linkStyleID;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        if (levels != null)
        {
            for(ListLevel level : levels)
            {
                level.dispose();
            }
            levels = null;
        }
    }

    // ID
    private int listID;
    // = 0, nine level, = 1, one level
    private byte simpleList;
    // is this level link style
    private short[] linkStyle;    
    //
    private short linkStyleID = -1;
    // is this number
    private boolean isNumber;
    // level 
    private ListLevel[] levels;
    // previous paragraph level of same listID
    private byte preParaLevel;
    //
    private byte normalPreParaLevel;
}
