/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package com.nvqquy98.lib.doc.office.fc.dom4j.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import org.xmlpull.v1.XmlPullParserException;

import com.nvqquy98.lib.doc.office.fc.dom4j.DocumentException;
import com.nvqquy98.lib.doc.office.fc.dom4j.DocumentFactory;
import com.nvqquy98.lib.doc.office.fc.dom4j.ElementHandler;


/*import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;*/

/**
 * <p>
 * <code>XPP3Reader</code> is a Reader of DOM4J documents that uses the fast
 * <a href="http://www.extreme.indiana.edu/soap/xpp/">XML Pull Parser 3.x </a>.
 * It is very fast for use in SOAP style environments.
 * </p>
 * 
 * @author <a href="mailto:pelle@neubia.com">Pelle Braendgaard </a>
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @version $Revision: 1.3 $
 */
public class XPP3Reader
{
    /** <code>DocumentFactory</code> used to create new document objects */
    private DocumentFactory factory;

    /** <code>XmlPullParser</code> used to parse XML */
    //private XmlPullParser xppParser;

    /** <code>XmlPullParser</code> used to parse XML */
    // private XmlPullParserFactory xppFactory;

    /** DispatchHandler to call when each <code>Element</code> is encountered */
    private DispatchHandler dispatchHandler;

    public XPP3Reader()
    {
    }

    public XPP3Reader(DocumentFactory factory)
    {
        this.factory = factory;
    }

    /**
     * <p>
     * Reads a Document from the given <code>File</code>
     * </p>
     * 
     * @param file
     *            is the <code>File</code> to read from.
     * 
     * @return the newly created Document instance
     * 
     * @throws DocumentException
     *             if an error occurs during parsing.
     * @throws IOException
     *             if a URL could not be made for the given File
     * @throws XmlPullParserException
     *             DOCUMENT ME!
     * /
    public Document read(File file) throws DocumentException, IOException, XmlPullParserException
    {
        String systemID = file.getAbsolutePath();

        return read(new BufferedReader(new FileReader(file)), systemID);
    }

    /**
     * <p>
     * Reads a Document from the given <code>URL</code>
     * </p>
     * 
     * @param url
     *            <code>URL</code> to read from.
     * 
     * @return the newly created Document instance
     * 
     * @throws DocumentException
     *             if an error occurs during parsing.
     * @throws IOException
     *             DOCUMENT ME!
     * @throws XmlPullParserException
     *             DOCUMENT ME!
     * /
    public Document read(URL url) throws DocumentException, IOException, XmlPullParserException
    {
        String systemID = url.toExternalForm();

        return read(createReader(url.openStream()), systemID);
    }

    /**
     * <p>
     * Reads a Document from the given URL or filename.
     * </p>
     * 
     * <p>
     * If the systemID contains a <code>':'</code> character then it is
     * assumed to be a URL otherwise its assumed to be a file name. If you want
     * finer grained control over this mechansim then please explicitly pass in
     * either a {@link URL}or a {@link File}instance instead of a {@link
     * String} to denote the source of the document.
     * </p>
     * 
     * @param systemID
     *            is a URL for a document or a file name.
     * 
     * @return the newly created Document instance
     * 
     * @throws DocumentException
     *             if an error occurs during parsing.
     * @throws IOException
     *             if a URL could not be made for the given File
     * @throws XmlPullParserException
     *             DOCUMENT ME!
     * /
    public Document read(String systemID) throws DocumentException, IOException,
        XmlPullParserException
    {
        if (systemID.indexOf(':') >= 0)
        {
            // lets assume its a URL
            return read(new URL(systemID));
        }
        else
        {
            // lets assume that we are given a file name
            return read(new File(systemID));
        }
    }

    /**
     * <p>
     * Reads a Document from the given stream
     * </p>
     * 
     * @param in
     *            <code>InputStream</code> to read from.
     * 
     * @return the newly created Document instance
     * 
     * @throws DocumentException
     *             if an error occurs during parsing.
     * @throws IOException
     *             DOCUMENT ME!
     * @throws XmlPullParserException
     *             DOCUMENT ME!
     * /
    public Document read(InputStream in) throws DocumentException, IOException,
        XmlPullParserException
    {
        return read(createReader(in));
    }

    /**
     * <p>
     * Reads a Document from the given <code>Reader</code>
     * </p>
     * 
     * @param reader
     *            is the reader for the input
     * 
     * @return the newly created Document instance
     * 
     * @throws DocumentException
     *             if an error occurs during parsing.
     * @throws IOException
     *             DOCUMENT ME!
     * @throws XmlPullParserException
     *             DOCUMENT ME!
     * /
    public Document read(Reader reader) throws DocumentException, IOException,
        XmlPullParserException
    {
        getXPPParser().setInput(reader);

        return parseDocument();
    }

    /**
     * <p>
     * Reads a Document from the given array of characters
     * </p>
     * 
     * @param text
     *            is the text to parse
     * 
     * @return the newly created Document instance
     * 
     * @throws DocumentException
     *             if an error occurs during parsing.
     * @throws IOException
     *             DOCUMENT ME!
     * @throws XmlPullParserException
     *             DOCUMENT ME!
     * /
    public Document read(char[] text) throws DocumentException, IOException, XmlPullParserException
    {
        getXPPParser().setInput(new CharArrayReader(text));

        return parseDocument();
    }

    /**
     * <p>
     * Reads a Document from the given stream
     * </p>
     * 
     * @param in
     *            <code>InputStream</code> to read from.
     * @param systemID
     *            is the URI for the input
     * 
     * @return the newly created Document instance
     * 
     * @throws DocumentException
     *             if an error occurs during parsing.
     * @throws IOException
     *             DOCUMENT ME!
     * @throws XmlPullParserException
     *             DOCUMENT ME!
     * /
    public Document read(InputStream in, String systemID) throws DocumentException, IOException,
        XmlPullParserException
    {
        return read(createReader(in), systemID);
    }

    /**
     * <p>
     * Reads a Document from the given <code>Reader</code>
     * </p>
     * 
     * @param reader
     *            is the reader for the input
     * @param systemID
     *            is the URI for the input
     * 
     * @return the newly created Document instance
     * 
     * @throws DocumentException
     *             if an error occurs during parsing.
     * @throws IOException
     *             DOCUMENT ME!
     * @throws XmlPullParserException
     *             DOCUMENT ME!
     * /
    public Document read(Reader reader, String systemID) throws DocumentException, IOException,
        XmlPullParserException
    {
        Document document = read(reader);
        document.setName(systemID);

        return document;
    }

    // Properties
    // -------------------------------------------------------------------------
    public XmlPullParser getXPPParser() throws XmlPullParserException
    {
        if (xppParser == null)
        {
            xppParser = getXPPFactory().newPullParser();
        }

        return xppParser;
    }

    public XmlPullParserFactory getXPPFactory() throws XmlPullParserException
    {
        if (xppFactory == null)
        {
            xppFactory = XmlPullParserFactory.newInstance();
        }

        xppFactory.setNamespaceAware(true);

        return xppFactory;
    }

    public void setXPPFactory(XmlPullParserFactory xPPfactory)
    {
        this.xppFactory = xPPfactory;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return the <code>DocumentFactory</code> used to create document
     *         objects
     */
    public DocumentFactory getDocumentFactory()
    {
        if (factory == null)
        {
            factory = DocumentFactory.getInstance();
        }

        return factory;
    }

    /**
     * <p>
     * This sets the <code>DocumentFactory</code> used to create new
     * documents. This method allows the building of custom DOM4J tree objects
     * to be implemented easily using a custom derivation of
     * {@link DocumentFactory}
     * </p>
     * 
     * @param documentFactory
     *            <code>DocumentFactory</code> used to create DOM4J objects
     */
    public void setDocumentFactory(DocumentFactory documentFactory)
    {
        this.factory = documentFactory;
    }

    /**
     * Adds the <code>ElementHandler</code> to be called when the specified
     * path is encounted.
     * 
     * @param path
     *            is the path to be handled
     * @param handler
     *            is the <code>ElementHandler</code> to be called by the event
     *            based processor.
     */
    public void addHandler(String path, ElementHandler handler)
    {
        getDispatchHandler().addHandler(path, handler);
    }

    /**
     * Removes the <code>ElementHandler</code> from the event based processor,
     * for the specified path.
     * 
     * @param path
     *            is the path to remove the <code>ElementHandler</code> for.
     */
    public void removeHandler(String path)
    {
        getDispatchHandler().removeHandler(path);
    }

    /**
     * When multiple <code>ElementHandler</code> instances have been
     * registered, this will set a default <code>ElementHandler</code> to be
     * called for any path which does <b>NOT </b> have a handler registered.
     * 
     * @param handler
     *            is the <code>ElementHandler</code> to be called by the event
     *            based processor.
     */
    public void setDefaultHandler(ElementHandler handler)
    {
        getDispatchHandler().setDefaultHandler(handler);
    }

    /*protected Document parseDocument() throws DocumentException, IOException,
        XmlPullParserException
    {
        DocumentFactory df = getDocumentFactory();
        Document document = df.createDocument();
        Element parent = null;
        XmlPullParser pp = getXPPParser();
        pp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);

        while (true)
        {
            int type = pp.nextToken();

            switch (type)
            {
                case XmlPullParser.PROCESSING_INSTRUCTION:
                {
                    String text = pp.getText();
                    int loc = text.indexOf(" ");

                    if (loc >= 0)
                    {
                        String target = text.substring(0, loc);
                        String txt = text.substring(loc + 1);
                        document.addProcessingInstruction(target, txt);
                    }
                    else
                    {
                        document.addProcessingInstruction(text, "");
                    }

                    break;
                }

                case XmlPullParser.COMMENT:
                {
                    if (parent != null)
                    {
                        parent.addComment(pp.getText());
                    }
                    else
                    {
                        document.addComment(pp.getText());
                    }

                    break;
                }

                case XmlPullParser.CDSECT:
                {
                    if (parent != null)
                    {
                        parent.addCDATA(pp.getText());
                    }
                    else
                    {
                        String msg = "Cannot have text content outside of the " + "root document";
                        throw new DocumentException(msg);
                    }

                    break;
                }

                case XmlPullParser.ENTITY_REF:
                    break;

                case XmlPullParser.END_DOCUMENT:
                    return document;

                case XmlPullParser.START_TAG:
                {
                    QName qname = (pp.getPrefix() == null) ? df.createQName(pp.getName(),
                        pp.getNamespace()) : df.createQName(pp.getName(), pp.getPrefix(),
                        pp.getNamespace());
                    Element newElement = df.createElement(qname);
                    int nsStart = pp.getNamespaceCount(pp.getDepth() - 1);
                    int nsEnd = pp.getNamespaceCount(pp.getDepth());

                    for (int i = nsStart; i < nsEnd; i++)
                    {
                        if (pp.getNamespacePrefix(i) != null)
                        {
                            newElement
                                .addNamespace(pp.getNamespacePrefix(i), pp.getNamespaceUri(i));
                        }
                    }

                    for (int i = 0; i < pp.getAttributeCount(); i++)
                    {
                        QName qa = (pp.getAttributePrefix(i) == null) ? df.createQName(pp
                            .getAttributeName(i)) : df.createQName(pp.getAttributeName(i),
                            pp.getAttributePrefix(i), pp.getAttributeNamespace(i));
                        newElement.addAttribute(qa, pp.getAttributeValue(i));
                    }

                    if (parent != null)
                    {
                        parent.add(newElement);
                    }
                    else
                    {
                        document.add(newElement);
                    }

                    parent = newElement;

                    break;
                }

                case XmlPullParser.END_TAG:
                {
                    if (parent != null)
                    {
                        parent = parent.getParent();
                    }

                    break;
                }

                case XmlPullParser.TEXT:
                {
                    String text = pp.getText();

                    if (parent != null)
                    {
                        parent.addText(text);
                    }
                    else
                    {
                        String msg = "Cannot have text content outside of the " + "root document";
                        throw new DocumentException(msg);
                    }

                    break;
                }

                default:
                    break;
            }
        }
    }*/

    protected DispatchHandler getDispatchHandler()
    {
        if (dispatchHandler == null)
        {
            dispatchHandler = new DispatchHandler();
        }

        return dispatchHandler;
    }

    protected void setDispatchHandler(DispatchHandler dispatchHandler)
    {
        this.dispatchHandler = dispatchHandler;
    }

    /**
     * Factory method to create a Reader from the given InputStream.
     * 
     * @param in
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws IOException
     *             DOCUMENT ME!
     */
    protected Reader createReader(InputStream in) throws IOException
    {
        return new BufferedReader(new InputStreamReader(in));
    }
}

/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * 3. The name "DOM4J" must not be used to endorse or promote products derived
 * from this Software without prior written permission of MetaStuff, Ltd. For
 * written permission, please contact dom4j-info@metastuff.com.
 * 
 * 4. Products derived from this Software may not be called "DOM4J" nor may
 * "DOM4J" appear in their names without prior written permission of MetaStuff,
 * Ltd. DOM4J is a registered trademark of MetaStuff, Ltd.
 * 
 * 5. Due credit should be given to the DOM4J Project - http://www.dom4j.org
 * 
 * THIS SOFTWARE IS PROVIDED BY METASTUFF, LTD. AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL METASTUFF, LTD. OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 */
