/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package com.nvqquy98.lib.doc.office.fc.dom4j.xpath;

import java.io.Serializable;
import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;

import com.nvqquy98.lib.doc.office.fc.dom4j.Document;
import com.nvqquy98.lib.doc.office.fc.dom4j.Element;
import com.nvqquy98.lib.doc.office.fc.dom4j.Namespace;
import com.nvqquy98.lib.doc.office.fc.dom4j.Node;


/**
 * <p>
 * <code>DefaultNamespaceContext</code> implements a Jaxen NamespaceContext
 * such that a context node is used to determine the current XPath namespace
 * prefixes and namespace URIs available.
 * </p>
 * 
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 */
public class DefaultNamespaceContext implements NamespaceContext, Serializable
{
    private final Element element;

    public DefaultNamespaceContext(Element element)
    {
        this.element = element;
    }

    public static DefaultNamespaceContext create(Object node)
    {
        Element element = null;

        if (node instanceof Element)
        {
            element = (Element)node;
        }
        else if (node instanceof Document)
        {
            Document doc = (Document)node;
            element = doc.getRootElement();
        }
        else if (node instanceof Node)
        {
            element = ((Node)node).getParent();
        }

        if (element != null)
        {
            return new DefaultNamespaceContext(element);
        }

        return null;
    }

    public String translateNamespacePrefixToUri(String prefix)
    {
        if ((prefix != null) && (prefix.length() > 0))
        {
            Namespace ns = element.getNamespaceForPrefix(prefix);

            if (ns != null)
            {
                return ns.getURI();
            }
        }

        return null;
    }

    @ Override
    public String getNamespaceURI(String prefix)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public String getPrefix(String namespaceURI)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @ Override
    public Iterator getPrefixes(String namespaceURI)
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
