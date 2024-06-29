/*
 * 文件名称:          SSControl.java
 *  
 * 编译器:            android2.2
 * 时间:              下午3:49:55
 */
package com.nvqquy98.lib.doc.office.ss.control;

import java.util.List;
import java.util.Vector;

import com.nvqquy98.lib.doc.office.common.ICustomDialog;
import com.nvqquy98.lib.doc.office.common.IOfficeToPicture;
import com.nvqquy98.lib.doc.office.common.hyperlink.Hyperlink;
import com.nvqquy98.lib.doc.office.constant.EventConstant;
import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Sheet;
import com.nvqquy98.lib.doc.office.ss.model.baseModel.Workbook;
import com.nvqquy98.lib.doc.office.ss.util.ReferenceUtil;
import com.nvqquy98.lib.doc.office.system.AbstractControl;
import com.nvqquy98.lib.doc.office.system.IControl;
import com.nvqquy98.lib.doc.office.system.IFind;
import com.nvqquy98.lib.doc.office.system.IMainFrame;
import com.nvqquy98.lib.doc.office.system.SysKit;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.ClipboardManager;
import android.view.View;

import org.jetbrains.annotations.Nullable;

/**
 * excel 应用控制
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-11-3
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class SSControl extends AbstractControl
{
    /**
     * 
     * @param
     * @param filepath
     */
    public SSControl(IControl mainControl, Workbook book, String filepath)
    {
        this.mainControl = mainControl;
        //this.spreadSheet = new Spreadsheet(getMainFrame().getActivity(), filepath, book, this);
        this.excelView = new ExcelView(getMainFrame().getActivity(), filepath, book, this);
        this.spreadSheet = excelView.getSpreadsheet();
    }

    /**
     * 布局视图
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public void layoutView(int x, int y, int w, int h)
    {
    }
    
    /**
     * action派发
     *  @param actionID 动作ID
     * @param obj 动作ID的Value
     */
    public void actionEvent(int actionID, final @Nullable Object obj)
    {
        Intent intent;
        switch (actionID)
        {
            case EventConstant.SYS_SET_PROGRESS_BAR_ID:
            	if (spreadSheet.getParent() != null)
            	{
            		spreadSheet.post(new Runnable()
                    {
                        /**
                         *
                         */
                        public void run()
                        {
                            if (!isDispose)
                            {
                                //getActivity().setProgressBarIndeterminateVisibility((Boolean)obj);
                                mainControl.getMainFrame().showProgressBar((Boolean)obj);
                            }
                        }
                    });
            	}                
                break;
                
            case EventConstant.SYS_VECTORGRAPH_PROGRESS:
            	if (spreadSheet.getParent() != null)
            	{
            		spreadSheet.post(new Runnable()
                    {
                        /**
                         *
                         */
                        public void run()
                        {
                        	if (!isDispose)
                            {
                        		mainControl.getMainFrame().updateViewImages((List<Integer>)obj);
                            }
                        }
                    });
            	}
            	else
            	{
            		new Thread()
            				{
            					/**
                                *
                                */
                               public void run()
                               {
                               	if (!isDispose)
                                   {
                               		mainControl.getMainFrame().updateViewImages((List<Integer>)obj);
                                   }
                               }
            				}.start();
            	}
            	break;
            	
            case EventConstant.SYS_INIT_ID:
                excelView.init();
                break;
                
            case EventConstant.SS_SHOW_SHEET:
                excelView.showSheet((Integer)obj);
                break;
                
            case EventConstant.TEST_REPAINT_ID:
                spreadSheet.postInvalidate();
                break;
                
            case EventConstant.SS_SHEET_CHANGE:
//                getMainFrame().getActivity().setTitle((String)obj);
                break;
                
            case EventConstant.APP_ZOOM_ID:
                int[] params = (int[])obj;
                spreadSheet.setZoom(params[0] / (float)MainConstant.STANDARD_RATE); //zoom
                spreadSheet.post(new Runnable()
                {   
                    @ Override
                    public void run()
                    {
                        if (!isDispose)
                        {
                            getMainFrame().changeZoom();
                            updateStatus();
                        }
                    }
                });
                break;
                
            case EventConstant.APP_CONTENT_SELECTED:
                updateStatus();
                break;
                
            case EventConstant.FILE_COPY_ID:                        //copy
                ClipboardManager clip = (ClipboardManager)getMainFrame().getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                clip.setText(spreadSheet.getActiveCellContent());
                break;
                
            case EventConstant.APP_HYPERLINK:                       //hyperlink
                Hyperlink hyperlink = spreadSheet.getActiveCellHyperlink();
                if(hyperlink != null)
                {
                    try
                    {
                        if(hyperlink.getLinkType() == Hyperlink.LINK_DOCUMENT)
                        {
                            String addr = hyperlink.getAddress();
                            int index = addr.indexOf("!");
                            String sheetName = addr.substring(0, index).replace("'", "");
                            String ref = addr.substring(index + 1, addr.length());
                            
                            int rowIndex = ReferenceUtil.instance().getRowIndex(ref);
                            int columnIndex = ReferenceUtil.instance().getColumnIndex(ref);
                            
                            Sheet sheet = spreadSheet.getWorkbook().getSheet(sheetName);
                            sheet.setActiveCellRowCol(rowIndex, columnIndex);
                            excelView.showSheet(sheetName);
                            
                            rowIndex -= 1;
                            columnIndex -= 1;                            
                            
                            spreadSheet.getSheetView().goToCell(rowIndex >= 0 ? rowIndex : 0, columnIndex >= 0 ? columnIndex : 0);
                            
                            getMainFrame().doActionEvent(EventConstant.SYS_UPDATE_TOOLSBAR_BUTTON_STATUS, null);
                            
                            spreadSheet.postInvalidate();
                            
                        }
                        else if(hyperlink.getLinkType() == Hyperlink.LINK_EMAIL
                            || hyperlink.getLinkType() == Hyperlink.LINK_URL)
                        {
                            intent = new Intent(Intent.ACTION_VIEW, 
                                Uri.parse(hyperlink.getAddress()));        
                            getMainFrame().getActivity().startActivity(intent);
                        }
                        else
                        {
                            //file hyperlink
                            mainControl.actionEvent(EventConstant.SYS_SHOW_TOOLTIP, "not supported hyperlink!");
                        }
                        
                    }
                    catch(Exception e)
                    {
                        
                    }                    
                }                
                break;
                
            case EventConstant.APP_INTERNET_SEARCH_ID:              //internet search
                getSysKit().internetSearch(spreadSheet.getActiveCellContent(), getMainFrame().getActivity());
                break;         
                
            case EventConstant.SYS_AUTO_TEST_FINISH_ID: // 布局完成 
                if (mainControl.isAutoTest())
                {
                    getMainFrame().getActivity().onBackPressed();
                }
                break;                
                
            case EventConstant.APP_GENERATED_PICTURE_ID:
                exportImage();
                break;
                
            case EventConstant.APP_ABORTREADING:
                if (mainControl.getReader() != null)
                {
                    mainControl.getReader().abortReader();
                }
                break;
                
            case EventConstant.APP_PAGE_UP_ID:           
                if (spreadSheet.getEventManage() != null)
                {
                    spreadSheet.getEventManage().onScroll(null, null, 0, -spreadSheet.getHeight() + 10);
                    exportImage();
                    
                    spreadSheet.post(new Runnable()
                    {   
                        @ Override
                        public void run()
                        {
                            if (!isDispose)
                            {
                                updateStatus();
                            }
                        }
                    });
                }
                break;
                
            case EventConstant.APP_PAGE_DOWN_ID:
                if (spreadSheet.getEventManage() != null)
                {
                    spreadSheet.getEventManage().onScroll(null, null, 0, spreadSheet.getHeight() - 10);
                    exportImage();
                    
                    spreadSheet.post(new Runnable()
                    {   
                        @ Override
                        public void run()
                        {
                            if (!isDispose)
                            {
                                updateStatus();
                            }
                        }
                    });
                }                
                break;
                
            case EventConstant.SS_REMOVE_SHEET_BAR:
                //spreadSheet.removeSheetBar();
                excelView.removeSheetBar();
                break;
                
            case EventConstant.APP_INIT_CALLOUTVIEW_ID:
            	spreadSheet.initCalloutView();
            	break;
                
            default:
                break;
        }
    }
    
    /**
     * 得到action的状态
     * 
     * @return obj
     */
    public @Nullable Object getActionValue(int actionID, @Nullable Object obj)
    {
        switch (actionID)
        {
            case EventConstant.APP_ZOOM_ID:
                return spreadSheet.getZoom();
                
            case EventConstant.APP_FIT_ZOOM_ID:
                return spreadSheet.getFitZoom();
                
            case EventConstant.APP_COUNT_PAGES_ID:
                return spreadSheet.getSheetCount();
                
            case EventConstant.APP_CURRENT_PAGE_NUMBER_ID:
                return spreadSheet.getCurrentSheetNumber();
                
            case EventConstant.SS_GET_ALL_SHEET_NAME:
                Vector<String> vec = new Vector<String>();
                Workbook  book = spreadSheet.getWorkbook();
                int cnt = book.getSheetCount();
                for (int i = 0; i < cnt; i++)
                {
                    vec.add(book.getSheet(i).getSheetName());
                }
                return vec;
                
            case EventConstant.SS_GET_SHEET_NAME:
                 int number = (Integer)obj;
                 if (number == -1)
                 {
                     return null;
                 }
                 Sheet sheet = spreadSheet.getWorkbook().getSheet(number - 1);
                 if (sheet != null)
                 {
                     return sheet.getSheetName();
                 }
                 return null;
             
            case EventConstant.APP_THUMBNAIL_ID:
                if(obj instanceof int[])
                {
                    int[] paraArr = (int[])obj;
                    if(paraArr != null && paraArr.length == 3)
                    {
                        return spreadSheet.getThumbnail(paraArr[0], paraArr[1], paraArr[2] / (float)MainConstant.STANDARD_RATE);
                    }
                } 
                
            case EventConstant.APP_GET_SNAPSHOT_ID:
                if (spreadSheet != null)
                {
                    return spreadSheet.getSnapshot((Bitmap)obj);
                }
                break;
                
            default:
                break;
        }
        return null;
    }

    
    /**
     * 
     */
    private void updateStatus()
    {
        spreadSheet.post(new Runnable()
        {
            public void run()
            {
                if (!isDispose)
                {
                    getMainFrame().updateToolsbarStatus();
                }
            }
        });
    }
    
    
    /**
     * 
     */
    private void exportImage()
    {
        spreadSheet.post(new Runnable()
        {
            
            @ Override
            public void run()
            {
                if (!isDispose)
                {
                    spreadSheet.createPicture();
                }
            }
        });
    }
    
    /**
     * current view index
     * @return
     */
    public int getCurrentViewIndex()
    {
    	return excelView.getCurrentViewIndex();
    }
    
    /**
     * 获取应用组件
     */
    public View getView()
    {
        return excelView;
    }

    /**
     * 
     */
    public Dialog getDialog(Activity activity, int id)
    {
        return null;
    }
    
    /**
     * 
     */
    public IMainFrame getMainFrame()
    {
        return mainControl.getMainFrame();
    }
    
    /**
     * 
     *(non-Javadoc)
     * @see com.nvqquy98.lib.doc.office.system.AbstractControl#getActivity()
     *
     */
    public Activity getActivity()
    {
        return mainControl.getMainFrame().getActivity();
    }
    
    /**
     * 
     */
    public IFind getFind()
    {
        return spreadSheet;
    }
    
    /**
     * 
     */
    public boolean isAutoTest()
    {
        return mainControl.isAutoTest();
    }    
    
    /**
     *
     */
    public IOfficeToPicture getOfficeToPicture()
    {
        return mainControl.getOfficeToPicture();
    }
    
    /**
     * 
     */
    public ICustomDialog getCustomDialog()
    {
        return mainControl.getCustomDialog();
    }
    
    /**
     * 
     *
     */
    public int getApplicationType()
    {
        return  MainConstant.APPLICATION_TYPE_SS;
    }    
    
    /**
     */
    public SysKit getSysKit()
    {
        return mainControl.getSysKit();
    }
    /**
     * 释放内存
     */
    public void dispose()
    {
        isDispose = true;
        mainControl = null;
        
        spreadSheet = null;
        /*if(spreadSheet != null)
        {
            spreadSheet.dispose();
            spreadSheet = null;
        }*/
        if (excelView == null)
        {
            excelView.dispose();
            excelView = null;
        }
    }
    

    //
    private IControl mainControl;
    //
    private boolean isDispose;
    // 
    private Spreadsheet spreadSheet;
    //
    private ExcelView excelView;
}
