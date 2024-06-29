/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package com.nvqquy98.lib.doc.office.fc.dom4j;

/**
 * <p>
 * <code>Attribute</code> defines an XML attribute. An attribute may have a
 * name, an optional namespace and a value.
 * </p>
 * 
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision: 1.9 $
 */
public interface Attribute extends Node {
    /**
     * <p>
     * Returns the <code>QName</code> of this attribute which represents the
     * local name, the qualified name and the <code>Namespace</code>.
     * </p>
     * 
     * @return the <code>QName</code> associated with this attribute
     */
    QName getQName();

    /**
     * <p>
     * Returns the <code>Namespace</code> of this element if one exists
     * otherwise null is returned returned.
     * </p>
     * 
     * @return the <code>Namespace</code> associated with this node
     */
    Namespace getNamespace();

    /**
     * <p>
     * Sets the <code>Namespace</code> of this element or if this element is
     * read only then an <code>UnsupportedOperationException</code> is thrown.
     * </p>
     * 
     * @param namespace
     *            is the <code>Namespace</code> to associate with this element
     */
    void setNamespace(Namespace namespace);

    /**
     * <p>
     * Returns the namespace prefix of this element if one exists otherwise an
     * empty <code>String</code> is returned.
     * </p>
     * 
     * @return the prefix of the <code>Namespace</code> of this element or an
     *         empty <code>String</code>
     */
    String getNamespacePrefix();

    /**
     * <p>
     * Returns the URI mapped to the namespace of this element if one exists
     * otherwise an empty <code>String</code> is returned.
     * </p>
     * 
     * @return the URI for the <code>Namespace</code> of this element or an
     *         empty <code>String</code>
     */
    String getNamespaceURI();

    /**
     * <p>
     * Returns the fully qualified name of this element.
     * </p>
     * 
     * <p>
     * This will be the same as the value returned from {@link Node#getName()}
     * if this element has no namespace attached to this element or an
     * expression of the form
     * 
     * <pre>
     * getNamespacePrefix() + &quot;:&quot; + getName()
     * </pre>
     * 
     * will be returned.
     * </p>
     * 
     * @return the fully qualified name of the element
     */
    String getQualifiedName();

    /**
     * <p>
     * Returns the value of the attribute. This method returns the same value as
     * the {@link Node#getText()}method.
     * </p>
     * 
     * @return the value of the attribute
     */
    String getValue();

    /**
     * <p>
     * Sets the value of this attribute or this method will throw an
     * <code>UnsupportedOperationException</code> if it is read-only.
     * </p>
     * 
     * @param value
     *            is the new value of this attribute
     */
    void setValue(String value);

    /**
     * <p>
     * Accesses the data of this attribute which may implement data typing
     * bindings such as <code>XML Schema</code> or <code>Java Bean</code>
     * bindings or will return the same value as {@link Node#getText()}.
     * </p>
     * 
     * @return the attribute data
     */
    Object getData();

    /**
     * <p>
     * Sets the data value of this attribute if this element supports data
     * binding or calls {@link Node#setText(String)}if it doesn't.
     * </p>
     * 
     * @param data
     *            the attribute data
     */
    void setData(Object data);
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
