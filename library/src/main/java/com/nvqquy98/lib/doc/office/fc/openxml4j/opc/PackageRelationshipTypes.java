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

package com.nvqquy98.lib.doc.office.fc.openxml4j.opc;

/**
 * Relationship types.
 *
 * @author Julien Chable
 * @version 0.2
 */
public interface PackageRelationshipTypes
{

    /**
     * Core properties relationship type.
     *
     *  <p>
     *  The standard specifies a source relations ship for the Core File Properties part as follows:
     *  <code>http://schemas.openxmlformats.org/officedocument/2006/relationships/metadata/core-properties.</code>
     *  </p>
     *  <p>
     *   Office uses the following source relationship for the Core File Properties part:
     *   <code>http://schemas.openxmlformats.org/package/2006/relationships/metadata/core-properties.</code>
     * </p>
     * See 2.1.33 Part 1 Section 15.2.11.1, Core File Properties Part in [MS-OE376].pdf
     */
    String CORE_PROPERTIES = "http://schemas.openxmlformats.org/package/2006/relationships/metadata/core-properties";

    /**
     * Core properties relationship type as defiend in ECMA 376.
      */
    String CORE_PROPERTIES_ECMA376 = "http://schemas.openxmlformats.org/officedocument/2006/relationships/metadata/core-properties";

    /**
     * Digital signature relationship type.
     */
    String DIGITAL_SIGNATURE = "http://schemas.openxmlformats.org/package/2006/relationships/digital-signature/signature";

    /**
     * Digital signature certificate relationship type.
     */
    String DIGITAL_SIGNATURE_CERTIFICATE = "http://schemas.openxmlformats.org/package/2006/relationships/digital-signature/certificate";

    /**
     * Digital signature origin relationship type.
     */
    String DIGITAL_SIGNATURE_ORIGIN = "http://schemas.openxmlformats.org/package/2006/relationships/digital-signature/origin";

    /**
     * Thumbnail relationship type.
     */
    String THUMBNAIL = "http://schemas.openxmlformats.org/package/2006/relationships/metadata/thumbnail";

    /**
     * Extended properties relationship type.
     */
    String EXTENDED_PROPERTIES = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/extended-properties";

    /**
     * Custom properties relationship type.
     */
    String CUSTOM_PROPERTIES = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/custom-properties";

    /**
     * Core properties relationship type.
     */
    String CORE_DOCUMENT = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument";

    /**
     * Custom XML relationship type.
     */
    String CUSTOM_XML = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/customXml";

    /**
     * Image type.
     */
    String IMAGE_PART = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image";
    
    /**
     * vmlDrawing type
     */
    String VMLDRAWING_PART = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/vmlDrawing";

    /**
     * Hyperlink type.
     */
    String HYPERLINK_PART = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/hyperlink";

    /**
     * Style type.
     */
    String STYLE_PART = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles";
    /**
     * bullet and number
     */
    String BULLET_NUMBER_PART = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/numbering";
    /**
     * 
     */
    String HEADER_PART = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/header";
    /**
     * 
     */
    String FOOTER_PART = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/footer";
    
    
    /////////////////////////////////////////xlsx parts///////////////////////////////////////////////
    
    /**
     * worksheet
     */
    String WORKSHEET_PART = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet";
    
    /**
     * chartsheet
     */
    String CHARTSHEET_PART = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/chartsheet";
    
    /**
     * shared strings of workbook
     */
    String SHAREDSTRINGS_PART = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/sharedStrings";
    
    /**
     * comment
     */
    String COMMENT_PART = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/comments";
    
    /**
     * theme
     */
    String THEME_PART = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/theme";
    /**
     * drawing
     */
    String DRAWING_PART = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/drawing";
    
    /**
     * chart
     */
    String CHART_PART = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/chart";
    
    /**
     * table
     */
    String TABLE_PART = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/table";
    
    /////////////////////////////////////////pptx parts///////////////////////////////////////////////    
    /**
     * slide type
     */
    String SLIDE_PART = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/slide";
    
    /**
     * slide layout type
     */
    String LAYOUT_PART = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/slideLayout";
    
    /**
     * slide master type
     */
    String SLIDE_MASTER = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/slideMaster";
    
    /**
     * notes
     */
    String NOTES_SLIDE = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/notesSlide";
    
    /**
     * table style
     */
    String TABLE_STYLE = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/tableStyles";
    
    // OLE
    String OLE_TYPE = "http://schemas.openxmlformats.org/presentationml/2006/ole";
    // chart
    String CHART_TYPE = "http://schemas.openxmlformats.org/drawingml/2006/chart";
    // table
    String TABLE_TYPE = "http://schemas.openxmlformats.org/drawingml/2006/table";
    // smartart
    String DIAGRAM_TYPE = "http://schemas.openxmlformats.org/drawingml/2006/diagram";    //
    String DIAGRAM_DRAWING = "http://schemas.microsoft.com/office/2007/relationships/diagramDrawing";
    String DIAGRAM_DATA = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/diagramData";
}
