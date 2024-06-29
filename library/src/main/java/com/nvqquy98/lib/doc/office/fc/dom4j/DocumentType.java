/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package com.nvqquy98.lib.doc.office.fc.dom4j;

import java.util.List;

/**
 * <p>
 * <code>DocumentType</code> defines an XML DOCTYPE declaration.
 * </p>
 * 
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision: 1.10 $
 */
public interface DocumentType extends Node {
    /**
     * This method is the equivalent to the {@link #getName}method. It is added
     * for clarity.
     * 
     * @return the root element name for the document type.
     */
    String getElementName();

    /**
     * This method is the equivalent to the {@link #setName}method. It is added
     * for clarity.
     * 
     * @param elementName
     *            DOCUMENT ME!
     */
    void setElementName(String elementName);

    String getPublicID();

    void setPublicID(String publicID);

    String getSystemID();

    void setSystemID(String systemID);

    /**
     * Returns a list of internal DTD declaration objects, defined in the
     * {@link org.dom4j.dtd}package
     * 
     * @return DOCUMENT ME!
     */
    List getInternalDeclarations();

    /**
     * Sets the list of internal DTD declaration objects, defined in the
     * {@link org.dom4j.dtd}package
     * 
     * @param declarations
     *            DOCUMENT ME!
     */
    void setInternalDeclarations(List declarations);

    /**
     * Returns a list of internal DTD declaration objects, defined in the
     * {@link org.dom4j.dtd}package
     * 
     * @return DOCUMENT ME!
     */
    List getExternalDeclarations();

    /**
     * Sets the list of internal DTD declaration objects, defined in the
     * {@link org.dom4j.dtd}package
     * 
     * @param declarations
     *            DOCUMENT ME!
     */
    void setExternalDeclarations(List declarations);
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
