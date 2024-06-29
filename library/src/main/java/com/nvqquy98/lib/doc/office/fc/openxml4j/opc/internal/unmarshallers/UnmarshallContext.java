/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package com.nvqquy98.lib.doc.office.fc.openxml4j.opc.internal.unmarshallers;

import java.util.zip.ZipEntry;

import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.PackagePartName;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.ZipPackage;


/**
 * Context needed for the unmarshall process of a part. This class is immutable.
 * 
 * @author Julien Chable
 * @version 1.0
 */
public final class UnmarshallContext
{

    private ZipPackage _package;

    private PackagePartName partName;

    private ZipEntry zipEntry;

    /**
     * Constructor.
     * 
     * @param targetPackage
     *            Container.f
     * @param partName
     *            Name of the part to unmarshall.
     */
    public UnmarshallContext(ZipPackage targetPackage, PackagePartName partName)
    {
        this._package = targetPackage;
        this.partName = partName;
    }

    /**
     * @return the container
     */
    public ZipPackage getPackage()
    {
        return _package;
    }

    /**
     * @param container
     *            the container to set
     */
    public void setPackage(ZipPackage container)
    {
        this._package = container;
    }

    /**
     * @return the partName
     */
    PackagePartName getPartName()
    {
        return partName;
    }

    /**
     * @param partName
     *            the partName to set
     */
    public void setPartName(PackagePartName partName)
    {
        this.partName = partName;
    }

    /**
     * @return the zipEntry
     */
    ZipEntry getZipEntry()
    {
        return zipEntry;
    }

    /**
     * @param zipEntry
     *            the zipEntry to set
     */
    public void setZipEntry(ZipEntry zipEntry)
    {
        this.zipEntry = zipEntry;
    }
}
