/*
 * 文件名称:          PDFPageAdapter.java
 *  
 * 编译器:            android2.2
 * 时间:              下午9:31:01
 */
package com.nvqquy98.lib.doc.office.system.beans.pagelist;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * page list view component adapter
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-9-19
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class APageListAdapter extends BaseAdapter
{
    /**
     * construct
     * 
     * @param pdfListView
     */
    public APageListAdapter(APageListView view)
    {
        this.listView = view;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     * 
     * @return Count of items.
     */
    public int getCount()
    {
        return listView.getPageCount();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     * 
     * @param position Position of the item whose data we want within the adapter's 
     * data set.
     * @return The data at the specified position.
     */
    public Object getItem(int position)
    {
        return null;
    }

    /**
     * Indicated whether the item ids are stable across changes to the
     * underlying data.
     * 
     * @return True if the same id always refers to the same object.
     */
    public long getItemId(int position)
    {
        return 0;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link android.view.LayoutInflater#inflate(int, android.view.ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     * 
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     * @param parent The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    public View getView(int position, View convertView, ViewGroup parent)
    {
        APageListItem pageItem = (APageListItem)convertView;
        Rect pageSize = listView.getPageListViewListener().getPageSize(position);
        if (convertView == null)
        {
            pageItem = listView.getPageListItem(position, convertView, parent);
            if (pageItem == null)
            {
                pageSize.right = 794;
                pageSize.bottom = 1124;
                pageItem = new APageListItem(listView, pageSize.width(), pageSize.height());
            }
        }
        pageItem.setPageItemRawData(position, pageSize.width(), pageSize.height());
        return pageItem;
    }
    
    /**
     * 
     */
    public void dispose()
    {
        listView = null;
    }
    // 
    private APageListView listView;
}
