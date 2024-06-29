/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package com.nvqquy98.lib.doc.office.fc.dom4j.tree;

import java.util.Map;

import com.nvqquy98.lib.doc.office.fc.dom4j.Element;


/**
 * <p>
 * <code>DefaultProcessingInstruction</code> is the default Processing
 * Instruction implementation. It is a doubly linked node which supports the
 * parent relationship and can be modified in place.
 * </p>
 * 
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @version $Revision: 1.13 $
 */
public class DefaultProcessingInstruction extends FlyweightProcessingInstruction
{
    /** The parent of this node */
    private Element parent;

    /**
     * <p>
     * This will create a new PI with the given target and values
     * </p>
     * 
     * @param target
     *            is the name of the PI
     * @param values
     *            is the <code>Map</code> values for the PI
     */
    public DefaultProcessingInstruction(String target, Map values)
    {
        super(target, values);
    }

    /**
     * <p>
     * This will create a new PI with the given target and values
     * </p>
     * 
     * @param target
     *            is the name of the PI
     * @param values
     *            is the values for the PI
     */
    public DefaultProcessingInstruction(String target, String values)
    {
        super(target, values);
    }

    /**
     * <p>
     * This will create a new PI with the given target and values
     * </p>
     * 
     * @param parent
     *            is the parent element
     * @param target
     *            is the name of the PI
     * @param values
     *            is the values for the PI
     */
    public DefaultProcessingInstruction(Element parent, String target, String values)
    {
        super(target, values);
        this.parent = parent;
    }

    public void setTarget(String target)
    {
        this.target = target;
    }

    public void setText(String text)
    {
        this.text = text;
        this.values = parseValues(text);
    }

    public void setValues(Map values)
    {
        this.values = values;
        this.text = toString(values);
    }

    public void setValue(String name, String value)
    {
        values.put(name, value);
    }

    public Element getParent()
    {
        return parent;
    }

    public void setParent(Element parent)
    {
        this.parent = parent;
    }

    public boolean supportsParent()
    {
        return true;
    }

    public boolean isReadOnly()
    {
        return false;
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
