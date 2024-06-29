/*
 * 文件名称:          SocketClient.java
 *  
 * 编译器:            android2.2
 * 时间:              上午11:14:04
 */
package com.nvqquy98.lib.doc.office.system;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * socket client
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-3-7
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class SocketClient
{
    public static final String HOST = "172.25.3.147";
    public static final int LISTENER_PORT = 3000;

    private static SocketClient sc = new SocketClient();
    
    public static SocketClient instance()
    {
        return sc;
    }
    
    /**
     * 
     */
    public SocketClient()
    {
        initConnection();
    }
    
    /**
     * 
     */
    public void initConnection()
    {
        try
        {
            client = new Socket(HOST, LISTENER_PORT);            
        }
        catch(UnknownHostException e)
        {
            System.out.println("Error setting up socket connection: unknown host at " + HOST
                + ":" + LISTENER_PORT);
        }
        catch(IOException e)
        {
            System.out.println("Error setting up socket connection: " + e);
        }
    }

    //============================= 
    public InputStream getFile(String fileName)
    {
        try
        {
            OutputStream output = client.getOutputStream();
            output.write(fileName.getBytes());
            output.flush();
            return client.getInputStream();
        }
        catch(Exception e)
        {
            System.out.println("Error reading from file: " + fileName);
        }
        return null;
    }
    //
    private Socket client;
}
