/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package com.nvqquy98.lib.doc.office.fc.dom4j.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

import com.nvqquy98.lib.doc.office.fc.dom4j.tree.DefaultDocumentType;


/**
 * <p>
 * <code>DOMDocumentType</code> implements a DocumentType node which supports
 * the W3C DOM API.
 * </p>
 * 
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision: 1.11 $
 */
public class DOMDocumentType extends DefaultDocumentType implements org.w3c.dom.DocumentType
{
    public DOMDocumentType()
    {
    }

    public DOMDocumentType(String elementName, String systemID)
    {
        super(elementName, systemID);
    }

    public DOMDocumentType(String name, String publicID, String systemID)
    {
        super(name, publicID, systemID);
    }

    // org.w3c.dom.Node interface
    // -------------------------------------------------------------------------
    public boolean supports(String feature, String version)
    {
        return DOMNodeHelper.supports(this, feature, version);
    }

    public String getNamespaceURI()
    {
        return DOMNodeHelper.getNamespaceURI(this);
    }

    public String getPrefix()
    {
        return DOMNodeHelper.getPrefix(this);
    }

    public void setPrefix(String prefix) throws DOMException
    {
        DOMNodeHelper.setPrefix(this, prefix);
    }

    public String getLocalName()
    {
        return DOMNodeHelper.getLocalName(this);
    }

    public String getNodeName()
    {
        return getName();
    }

    // already part of API
    //
    // public short getNodeType();
    public String getNodeValue() throws DOMException
    {
        return null;
    }

    public void setNodeValue(String nodeValue) throws DOMException
    {
    }

    public org.w3c.dom.Node getParentNode()
    {
        return DOMNodeHelper.getParentNode(this);
    }

    public NodeList getChildNodes()
    {
        return DOMNodeHelper.getChildNodes(this);
    }

    public org.w3c.dom.Node getFirstChild()
    {
        return DOMNodeHelper.getFirstChild(this);
    }

    public org.w3c.dom.Node getLastChild()
    {
        return DOMNodeHelper.getLastChild(this);
    }

    public org.w3c.dom.Node getPreviousSibling()
    {
        return DOMNodeHelper.getPreviousSibling(this);
    }

    public org.w3c.dom.Node getNextSibling()
    {
        return DOMNodeHelper.getNextSibling(this);
    }

    public NamedNodeMap getAttributes()
    {
        return null;
    }

    public Document getOwnerDocument()
    {
        return DOMNodeHelper.getOwnerDocument(this);
    }

    public org.w3c.dom.Node insertBefore(org.w3c.dom.Node newChild, org.w3c.dom.Node refChild)
        throws DOMException
    {
        checkNewChildNode(newChild);

        return DOMNodeHelper.insertBefore(this, newChild, refChild);
    }

    public org.w3c.dom.Node replaceChild(org.w3c.dom.Node newChild, org.w3c.dom.Node oldChild)
        throws DOMException
    {
        checkNewChildNode(newChild);

        return DOMNodeHelper.replaceChild(this, newChild, oldChild);
    }

    public org.w3c.dom.Node removeChild(org.w3c.dom.Node oldChild) throws DOMException
    {
        return DOMNodeHelper.removeChild(this, oldChild);
    }

    public org.w3c.dom.Node appendChild(org.w3c.dom.Node newChild) throws DOMException
    {
        checkNewChildNode(newChild);

        return DOMNodeHelper.appendChild(this, newChild);
    }

    private void checkNewChildNode(org.w3c.dom.Node newChild) throws DOMException
    {
        throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR,
            "DocumentType nodes cannot have children");
    }

    public boolean hasChildNodes()
    {
        return DOMNodeHelper.hasChildNodes(this);
    }

    public org.w3c.dom.Node cloneNode(boolean deep)
    {
        return DOMNodeHelper.cloneNode(this, deep);
    }

    public void normalize()
    {
        DOMNodeHelper.normalize(this);
    }

    public boolean isSupported(String feature, String version)
    {
        return DOMNodeHelper.isSupported(this, feature, version);
    }

    public boolean hasAttributes()
    {
        return DOMNodeHelper.hasAttributes(this);
    }

    // org.w3c.dom.DocumentType interface
    // -------------------------------------------------------------------------
    public NamedNodeMap getEntities()
    {
        return null;
    }

    public NamedNodeMap getNotations()
    {
        return null;
    }

    public String getPublicId()
    {
        return getPublicID();
    }

    public String getSystemId()
    {
        return getSystemID();
    }

    public String getInternalSubset()
    {
        return getElementName();
    }

    @ Override
    public String getBaseURI()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public short compareDocumentPosition(Node other) throws DOMException
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @ Override
    public String getTextContent() throws DOMException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public void setTextContent(String textContent) throws DOMException
    {
        // TODO Auto-generated method stub

    }

    @ Override
    public boolean isSameNode(Node other)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @ Override
    public String lookupPrefix(String namespaceURI)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public boolean isDefaultNamespace(String namespaceURI)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @ Override
    public String lookupNamespaceURI(String prefix)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public boolean isEqualNode(Node arg)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @ Override
    public Object getFeature(String feature, String version)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public Object setUserData(String key, Object data, UserDataHandler handler)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public Object getUserData(String key)
    {
        // TODO Auto-generated method stub
        return null;
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
