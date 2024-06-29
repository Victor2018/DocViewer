/*
 * 文件名称:          WPControl.java
 *  
 * 编译器:            android2.2
 * 时间:              下午1:57:55
 */
package com.nvqquy98.lib.doc.office.wp.control;

import java.util.List;
import java.util.Vector;

import com.nvqquy98.lib.doc.office.common.ICustomDialog;
import com.nvqquy98.lib.doc.office.common.IOfficeToPicture;
import com.nvqquy98.lib.doc.office.common.bookmark.Bookmark;
import com.nvqquy98.lib.doc.office.common.hyperlink.Hyperlink;
import com.nvqquy98.lib.doc.office.constant.DialogConstant;
import com.nvqquy98.lib.doc.office.constant.EventConstant;
import com.nvqquy98.lib.doc.office.constant.MainConstant;
import com.nvqquy98.lib.doc.office.constant.wp.WPViewConstant;
import com.nvqquy98.lib.doc.office.simpletext.model.IDocument;
import com.nvqquy98.lib.doc.office.system.AbstractControl;
import com.nvqquy98.lib.doc.office.system.IControl;
import com.nvqquy98.lib.doc.office.system.IFind;
import com.nvqquy98.lib.doc.office.system.IMainFrame;
import com.nvqquy98.lib.doc.office.system.SysKit;
import com.nvqquy98.lib.doc.office.wp.dialog.TXTEncodingDialog;

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
 * 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            梁金晶
 * <p>
 * 日期:            2011-10-27
 * <p>
 * 负责人:          梁金晶
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class WPControl extends AbstractControl
{
    
    /**
     * 
     * @param doc
     * @param filePath
     */
    public WPControl(IControl mainControl, IDocument doc, String filePath)
    {
        this.mainControl = mainControl;
        wpView = new Word(mainControl.getMainFrame().getActivity().getApplicationContext(), doc, filePath, this);
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
        switch (actionID)
        {
            case EventConstant.WP_SHOW_PAGE:
                wpView.showPage((Integer)obj, EventConstant.WP_SHOW_PAGE);
                if (wpView.getCurrentRootType() == WPViewConstant.PRINT_ROOT)
                {
                    return;
                }
                //
                updateStatus();
                // 
                exportImage();
                break;
            case EventConstant.SYS_SET_PROGRESS_BAR_ID:
            	if (wpView.getParent() != null)
            	{
            		wpView.post(new Runnable()
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
            	if (wpView.getParent() != null)
            	{
            		wpView.post(new Runnable()
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
                wpView.init();
                break;
                
            case EventConstant.TEST_REPAINT_ID:
                wpView.postInvalidate();
                break;
                
            case EventConstant.WP_PRINT_MODE:
                if (wpView.getCurrentRootType() == WPViewConstant.PRINT_ROOT)
                {
                    return;
                }
                wpView.switchView(WPViewConstant.PRINT_ROOT);
                updateStatus();
                break;
                
            case EventConstant.WP_SWITCH_VIEW:
                int rootType;
                if (obj != null)
                {
                    rootType = (Integer)obj;
                }
                else
                {
                    rootType = wpView.getCurrentRootType();
                    if (rootType == WPViewConstant.PAGE_ROOT)
                    {
                        rootType = WPViewConstant.NORMAL_ROOT;
                    }
                    else
                    {
                        rootType = WPViewConstant.PAGE_ROOT;
                    }
                }
                wpView.switchView(rootType);
                //
                updateStatus();
                if (rootType != WPViewConstant.PRINT_ROOT)
                {
                    exportImage();
                }
                break;
                
            case EventConstant.APP_ZOOM_ID:
                int[] params = (int[])obj;
                wpView.setZoom(params[0] / (float)MainConstant.STANDARD_RATE, params[1], params[2]);
                
                
                wpView.post(new Runnable()
                {   
                    @ Override
                    public void run()
                    {
                        if (!isDispose)
                        {
                            getMainFrame().changeZoom();
                        }
                    }
                });    
                break;
                
            case EventConstant.SYS_UPDATE_TOOLSBAR_BUTTON_STATUS: // 更新toolsbar button状态
                // 
                updateStatus();
                break;
                
            case EventConstant.WP_SELECT_TEXT_ID:   // 选择文本
                wpView.getStatus().setSelectTextStatus(!wpView.getStatus().isSelectTextStatus());
                break;
                
            case EventConstant.APP_INTERNET_SEARCH_ID: // 联网搜索
                ControlKit.instance().internetSearch(wpView);
                break;
                
            case EventConstant.FILE_COPY_ID:                        //copy
                ClipboardManager clip = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                clip.setText(wpView.getHighlight().getSelectText());
                break;
                
            case EventConstant.SYS_AUTO_TEST_FINISH_ID: // 布局完成 
                wpView.post(new Runnable()
                {
                    /**
                     * 
                     */
                    public void run()
                    {
                        if (!isDispose)
                        {
                            //getMainFrame().getActivity().setProgressBarIndeterminateVisibility(false);
                            mainControl.getMainFrame().showProgressBar(false);
                        }
                    }
                });
                
                if (isAutoTest())
                {
                    getMainFrame().getActivity().onBackPressed();
                }
                break;
                
            case EventConstant.APP_GENERATED_PICTURE_ID:
                exportImage();
                break;
                
            case EventConstant.APP_PAGE_UP_ID:           
                if (wpView.getCurrentRootType() == WPViewConstant.NORMAL_ROOT)
                {
                    if (wpView.getEventManage() != null)
                    {
                        wpView.getEventManage().onScroll(null, null, 0, -wpView.getHeight() + 10);
                    }
                }
                else
                {
                    wpView.showPage(wpView.getCurrentPageNumber() - 2, EventConstant.APP_PAGE_UP_ID);
                }
                if (wpView.getCurrentRootType() == WPViewConstant.PRINT_ROOT)
                {
                    return;
                }
                //
                updateStatus();
                // 
                exportImage();
                break;
                
            case EventConstant.APP_PAGE_DOWN_ID:
                if (wpView.getCurrentRootType() == WPViewConstant.NORMAL_ROOT)
                {
                    if (wpView.getEventManage() != null)
                    {
                        wpView.getEventManage().onScroll(null, null, 0, wpView.getHeight() + 10);
                    }
                }
                else
                {
                    wpView.showPage(wpView.getCurrentPageNumber(), EventConstant.APP_PAGE_DOWN_ID);
                } 
                if (wpView.getCurrentRootType() == WPViewConstant.PRINT_ROOT)
                {
                    return;
                }
                //
                updateStatus();
                // 
                exportImage();
                break;
                
            case EventConstant.APP_HYPERLINK: //hyperlink
                Hyperlink link = ((Hyperlink)obj);
                if(link != null)
                {                    
                    try
                    {
                        if (link.getLinkType() == Hyperlink.LINK_BOOKMARK)
                        {
                            Bookmark bm = getSysKit().getBookmarkManage().getBookmark(link.getAddress());
                            if (bm != null)
                            {
                                ControlKit.instance().gotoOffset(wpView, bm.getStart());
                            }
                        }
                        else
                        {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link.getAddress()));        
                            getMainFrame().getActivity().startActivity(intent);
                        }
                    }
                    catch(Exception e)
                    {
                        
                    }                    
                }                
                break;
                
            case EventConstant.WP_LAYOUT_NORMAL_VIEW:
                if (wpView.getCurrentRootType() == WPViewConstant.NORMAL_ROOT)
                {
                    wpView.setExportImageAfterZoom(true);
                    wpView.layoutNormal();
                }
                break;
            case EventConstant.WP_LAYOUT_COMPLETED:
                if (wpView != null)
                {
                	wpView.updateFieldText();
                	
                    if (wpView.getParent() == null)
                    {
                        getMainFrame().completeLayout();
                        break;
                    }
                    wpView.post(new Runnable()
                    {   
                        @ Override
                        public void run()
                        {
                            getMainFrame().completeLayout();
                        }
                    });
                }
                    
                break;
                
            case EventConstant.APP_SET_FIT_SIZE_ID:
                wpView.setFitSize((Integer)obj);
                break;
                
            case EventConstant.APP_INIT_CALLOUTVIEW_ID:
            	wpView.getPrintWord().getListView().getCurrentPageView().initCalloutView();
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
                return wpView.getZoom();
                
            case EventConstant.WP_SELECT_TEXT_ID:
                return ((Word)wpView).getStatus().isSelectTextStatus();
                
            case EventConstant.APP_FIT_ZOOM_ID:
                return wpView.getFitZoom();
                
            case EventConstant.APP_COUNT_PAGES_ID:
                return wpView.getPageCount();
                
            case EventConstant.APP_CURRENT_PAGE_NUMBER_ID:
                return wpView.getCurrentPageNumber();
                
            case EventConstant.WP_PAGE_TO_IMAGE:
                return wpView.pageToImage((Integer)obj);
                
            case EventConstant.APP_PAGEAREA_TO_IMAGE:
                if(obj instanceof int[])
                {
                    int[] paraArr = (int[])obj;
                    if(paraArr != null && paraArr.length == 7)
                    {
                        return wpView.pageAreaToImage(paraArr[0], paraArr[1], paraArr[2], paraArr[3], paraArr[4], paraArr[5], paraArr[6]);
                    }
                }
                break;
            case EventConstant.APP_THUMBNAIL_ID:
                if(obj instanceof Integer)
                {
                    return wpView.getThumbnail((Integer)obj / (float)MainConstant.STANDARD_RATE);
                }
                break;
                
            case EventConstant.WP_GET_PAGE_SIZE:
                return wpView.getPageSize((Integer)obj - 1);
                
            case EventConstant.WP_GET_VIEW_MODE:
                return wpView.getCurrentRootType();
                
            case EventConstant.APP_GET_FIT_SIZE_STATE_ID:
                if (wpView != null)
                {
                    return wpView.getFitSizeState();
                }
                break;
                
            case EventConstant.APP_GET_SNAPSHOT_ID:
                if (wpView != null)
                {
                    return wpView.getSnapshot((Bitmap)obj);
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
    private void exportImage()
    {
        wpView.post(new Runnable()
        {   
            @ Override
            public void run()
            {
                if (!isDispose)
                {
                    wpView.createPicture();
                }
            }
        });
    }
    
    /**
     * 
     */
    private void updateStatus()
    {
        wpView.post(new Runnable()
        {   
            @ Override
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
     * current view index
     * @return
     */
    public int getCurrentViewIndex()
    {
    	return wpView.getCurrentPageNumber();
    }
    
    /**
     *
     */
    public View getView()
    {
        return wpView;
    }
    
    /**
     * 
     */
    public Dialog getDialog(Activity activity, int id)
    {
        
        switch (id)
        {
            case DialogConstant.ENCODING_DIALOG_ID: // 文本文本编码格式对话框
                Vector<Object> vector = new Vector<Object>();
                vector.add(wpView.getFilePath());
                new TXTEncodingDialog(this, activity, wpView.getDialogAction(), vector, id).show();
                break;

            default:
                break;
        }
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
     */
    public Activity getActivity()
    {
        return getMainFrame().getActivity();
    }
    
    /**
     * 
     */
    public IFind getFind()
    {
        return wpView.getFind();
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
        return  MainConstant.APPLICATION_TYPE_WP;
    }
    
    /**
     * 
     */
    public SysKit getSysKit()
    {
        return mainControl.getSysKit();
    }
    
    /**
     * 
     *
     */
    public void dispose()
    {        
        isDispose = true;
        wpView.dispose();
        wpView = null;
        mainControl = null;
    }
    
    private boolean isDispose;
    //
    private IControl mainControl; 
    //
    private Word wpView;
}
