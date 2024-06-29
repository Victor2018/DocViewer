
package com.nvqquy98.lib.doc.office.common.hyperlink;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HyperlinkManage
{    
    /**
     * 
     */
    public HyperlinkManage()
    {
        hlinks = new ArrayList<Hyperlink>();
        hlinkIndexs = new HashMap<String, Integer>();
    }
   
    /**
     * get hyperlink for hlinkID
     */
    public Hyperlink getHyperlink(int hlinkID)
    {
        if (hlinkID >= 0 && hlinkID < hlinks.size())
        {
            return hlinks.get(hlinkID);
        }
        return null;
    }
    
    /**
     * 
     */
    public Integer getHyperlinkIndex(String key)
    {
        return hlinkIndexs.get(key);
    }
    
    /**
     * add hyperlink
     */
    public int addHyperlink(String address, int linkType)
    {
        Integer index = hlinkIndexs.get(address);
        if (index == null)
        {
            Hyperlink hlink = new Hyperlink();
            hlink.setLinkType(linkType);
            hlink.setAddress(address);
            
            int size = hlinks.size();
            hlinks.add(hlink);
            hlinkIndexs.put(address, size);            
            return size;
        }
        return index;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        if (hlinks != null)
        {
            for (Hyperlink hyperlink : hlinks)
            {
                hyperlink.dispose();
            }
            hlinks.clear();
        }
        if (hlinkIndexs != null)
        {
            hlinkIndexs.clear();
        }
    }
  
    //
    private List<Hyperlink> hlinks;
    //
    private Map<String, Integer> hlinkIndexs;
}
