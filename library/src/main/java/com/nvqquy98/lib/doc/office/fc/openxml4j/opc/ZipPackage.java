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

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.nvqquy98.lib.doc.office.fc.EncryptedDocumentException;
import com.nvqquy98.lib.doc.office.fc.fs.storage.HeaderBlock;
import com.nvqquy98.lib.doc.office.fc.fs.storage.LittleEndian;
import com.nvqquy98.lib.doc.office.fc.openxml4j.exceptions.InvalidFormatException;
import com.nvqquy98.lib.doc.office.fc.openxml4j.exceptions.InvalidOperationException;
import com.nvqquy98.lib.doc.office.fc.openxml4j.exceptions.OpenXML4JException;
import com.nvqquy98.lib.doc.office.fc.openxml4j.exceptions.OpenXML4JRuntimeException;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.internal.ContentType;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.internal.ContentTypeManager;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.internal.FileHelper;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.internal.PackagePropertiesPart;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.internal.PartMarshaller;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.internal.PartUnmarshaller;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.internal.ZipHelper;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.internal.marshallers.DefaultMarshaller;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.internal.marshallers.ZipPackagePropertiesMarshaller;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.internal.marshallers.ZipPartMarshaller;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.internal.unmarshallers.PackagePropertiesUnmarshaller;
import com.nvqquy98.lib.doc.office.fc.openxml4j.opc.internal.unmarshallers.UnmarshallContext;
import com.nvqquy98.lib.doc.office.fc.openxml4j.util.ZipEntrySource;
import com.nvqquy98.lib.doc.office.fc.openxml4j.util.ZipFileZipEntrySource;
import com.nvqquy98.lib.doc.office.fc.openxml4j.util.ZipInputStreamZipEntrySource;


/**
 * Represents a container that can store multiple data objects.
 *
 * @author Julien Chable, CDubet
 * @version 0.1
 */
public class ZipPackage implements RelationshipSource, Closeable
{    
    /**
     * Constructor. Opens a Zip based Open XML document.
     *
     * @param path The path of the file to open or create.
     */
    public ZipPackage(String path)
    {
        if (path == null || "".equals(path.trim())
            || (new File(path).exists() && new File(path).isDirectory()))
        {
            throw new IllegalArgumentException("path");
        }
        init();
        try
        {
            ZipFile zipFile = new ZipFile(new File(path));
            zipArchive = new ZipFileZipEntrySource(zipFile);
            getParts();
            originalPackagePath = new File(path).getAbsolutePath();
        }
        catch (Exception e)
        {
            File file = new File(path);
            if (file.length() == 0)
            {
                throw new EncryptedDocumentException("Format error");
            }
            try
            {
                FileInputStream in = new FileInputStream(file);
                byte[] b = new byte[16];
                in.read(b);
                // verify signature
                long signature = LittleEndian.getLong(b, 0);    
                if (signature == HeaderBlock._signature)
                {
                    throw new EncryptedDocumentException("Cannot process encrypted office files!");
                }
            }
            catch (IOException ioe)
            {
            }
            throw new EncryptedDocumentException("Invalid header signature");
            //e.printStackTrace();
        }
    }
    

    /**
     * Constructor. <b>Operation not supported.</b>
     *
     * @param in
     *            Zip input stream to load.     
     * @throws IllegalArgumentException
     *             If the specified input stream not an instance of
     *             ZipInputStream.
     */
    public ZipPackage(InputStream in) throws IOException
    {
        try
        {
            init();
            zipArchive = new ZipInputStreamZipEntrySource(new ZipInputStream(in));
            getParts();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    /**
     * Initialize the package instance.
     */
    private void init()
    {
        this.partMarshallers = new Hashtable<ContentType, PartMarshaller>(5);
        this.partUnmarshallers = new Hashtable<ContentType, PartUnmarshaller>(2);
        try
        {
            // Add 'default' unmarshaller
            this.partUnmarshallers.put(new ContentType(ContentTypes.CORE_PROPERTIES_PART),
                new PackagePropertiesUnmarshaller());
            // Add default marshaller
            this.defaultPartMarshaller = new DefaultMarshaller();
            // 
            this.partMarshallers.put(new ContentType(ContentTypes.CORE_PROPERTIES_PART),
                new ZipPackagePropertiesMarshaller());
        }
        catch(InvalidFormatException e)
        {
            // Should never happen
            throw new OpenXML4JRuntimeException(
                "Package.init() : this exception should never happen, "
                    + "if you read this message please send a mail to the developers team. : "
                    + e.getMessage());
        }
    }
 

    /**
     * Flush the package : save all.
     *
     * @see #close()
     */
    public void flush()
    {
        if (this.packageProperties != null)
        {
            this.packageProperties.flush();
        }
    }

    /**
     * Close the open, writable package and save its content.
     * 
     * If your package is open read only, then you should call {@link #revert()}
     *  when finished with the package.
     *
     * @throws IOException
     *             If an IO exception occur during the saving process.
     */
    public void close() throws IOException
    {
        // Save the content
        ReentrantReadWriteLock l = new ReentrantReadWriteLock();
        try
        {
            l.writeLock().lock();
            if (this.originalPackagePath != null && !"".equals(this.originalPackagePath.trim()))
            {
                File targetFile = new File(originalPackagePath);
                if (!targetFile.exists() || !(originalPackagePath.equalsIgnoreCase(targetFile.getAbsolutePath())))
                {
                    // Case of a package created from scratch
                    save(targetFile);
                }
                else
                {
                    closeImpl();
                }
            }
            else if (this.output != null)
            {
                save(this.output);
                output.close();
            }
        }
        finally
        {
            l.writeLock().unlock();
        }

        // Clear
        this.contentTypeManager.clearAll();
    }

    /**
     * Close the package WITHOUT saving its content. Reinitialize this package
     * and cancel all changes done to it.
     */
    public void revert()
    {
        revertImpl();
    }

    /**
     * Add a thumbnail to the package. This method is provided to make easier
     * the addition of a thumbnail in a package. You can do the same work by
     * using the traditionnal relationship and part mechanism.
     *
     * @param path
     *            The full path to the image file.
     */
    /*public void addThumbnail(String path) throws IOException
    {
        // Check parameter
        if ("".equals(path))
        {
            throw new IllegalArgumentException("path");
        }

        // Get the filename from the path
        String filename = path.substring(path.lastIndexOf(File.separatorChar) + 1);

        // Create the thumbnail part name
        String contentType = ContentTypes.getContentTypeFromFileExtension(filename);
        PackagePartName thumbnailPartName = null;
        try
        {
            thumbnailPartName = PackagingURIHelper.createPartName("/docProps/" + filename);
        }
        catch(InvalidFormatException e)
        {
            try
            {
                thumbnailPartName = PackagingURIHelper.createPartName("/docProps/thumbnail"
                    + path.substring(path.lastIndexOf(".") + 1));
            }
            catch(InvalidFormatException e2)
            {
                throw new InvalidOperationException("Can't add a thumbnail file named '" + filename
                    + "'");
            }
        }

        // Check if part already exist
        if (this.getPart(thumbnailPartName) != null)
        {
            throw new InvalidOperationException("You already add a thumbnail named '" 
                + filename + "'");
        }

        // Add the thumbnail part to this package.
        PackagePart thumbnailPart = this.createPart(thumbnailPartName, contentType, false);

        // Add the relationship between the package and the thumbnail part
        this.addRelationship(thumbnailPartName, TargetMode.INTERNAL,
            PackageRelationshipTypes.THUMBNAIL);

        // Copy file data to the newly created part
        FileInputStream is = new FileInputStream(path);
        StreamHelper.copyStream(is, thumbnailPart.getOutputStream());
        is.close();
    }*/
    /**
     * Retrieves or creates if none exists, core package property part.
     *
     * @return The PackageProperties part of this package.
     */
    public PackageProperties getPackageProperties() throws InvalidFormatException
    {
        // If no properties part has been found then we create one
        if (this.packageProperties == null)
        {
            this.packageProperties = new PackagePropertiesPart(this,
                PackagingURIHelper.CORE_PROPERTIES_PART_NAME);
        }
        return this.packageProperties;
    }

    /**
     * Retrieve a part identified by its name.
     *
     * @param partName
     *            Part name of the part to retrieve.
     * @return The part with the specified name, else <code>null</code>.
     */
    public PackagePart getPart(URI partName)
    {
        if (partName == null)
        {
            throw new IllegalArgumentException("partName");
        }        
        try
        {
            if (partList == null)
            {
                getParts();
            }
            return getPartImpl(PackagingURIHelper.createPartName(partName));
        }
        catch(InvalidFormatException e)
        {
            return null;
        }
    }
    /**
     * Retrieve a part identified by its name.
     *
     * @param partName
     *            Part name of the part to retrieve.
     * @return The part with the specified name, else <code>null</code>.
     */
    public PackagePart getPart(PackagePartName partName)
    {
        if (partName == null)
        {
            throw new IllegalArgumentException("partName");
        }
        if (partList == null)
        {
            try
            {
                getParts();
            }
            catch(InvalidFormatException e)
            {
                return null;
            }
        }
        return getPartImpl(partName);
    }

    /**
     * Retrieve parts by content type.
     *
     * @param contentType
     *            The content type criteria.
     * @return All part associated to the specified content type.
     */
    public ArrayList<PackagePart> getPartsByContentType(String contentType)
    {
        ArrayList<PackagePart> retArr = new ArrayList<PackagePart>();
        for (PackagePart part : partList.values())
        {
            if (part.getContentType().equals(contentType))
                retArr.add(part);
        }
        return retArr;
    }

    /**
     * Retrieve parts by relationship type.
     *
     * @param relationshipType
     *            Relationship type.
     * @return All parts which are the target of a relationship with the
     *         specified type, if the method can't retrieve relationships from
     *         the package, then return <code>null</code>.
     */
    public ArrayList<PackagePart> getPartsByRelationshipType(String relationshipType)
    {
        if (relationshipType == null)
            throw new IllegalArgumentException("relationshipType");
        ArrayList<PackagePart> retArr = new ArrayList<PackagePart>();
        for (PackageRelationship rel : getRelationshipsByType(relationshipType))
        {
            retArr.add(getPart(rel));
        }
        return retArr;
    }

    public List<PackagePart> getPartsByName(final Pattern namePattern)
    {
        if (namePattern == null)
        {
            throw new IllegalArgumentException("name pattern must not be null");
        }
        ArrayList<PackagePart> result = new ArrayList<PackagePart>();
        for (PackagePart part : partList.values())
        {
            PackagePartName partName = part.getPartName();
            String name = partName.getName();
            Matcher matcher = namePattern.matcher(name);
            if (matcher.matches())
            {
                result.add(part);
            }
        }
        return result;
    }

    /**
     * Get the target part from the specified relationship.
     *
     * @param partRel
     *            The part relationship uses to retrieve the part.
     */
    public PackagePart getPart(PackageRelationship partRel)
    {
        PackagePart retPart = null;
        ensureRelationships();
        for (PackageRelationship rel : relationships)
        {
            if (rel.getRelationshipType().equals(partRel.getRelationshipType()))
            {
                try
                {
                    retPart = getPart(PackagingURIHelper.createPartName(rel.getTargetURI()));
                }
                catch(InvalidFormatException e)
                {
                    continue;
                }
                break;
            }
        }
        return retPart;
    }

    /**
     * Load the parts of the archive if it has not been done yet The
     * relationships of each part are not loaded
     *
     * @return All this package's parts.
     */
    public ArrayList<PackagePart> getParts() throws InvalidFormatException
    {       
        // If the part list is null, we parse the package to retrieve all parts.
        if (partList == null)
        {
            try
            {
                partList = new PackagePartCollection();                
                Enumeration< ? extends ZipEntry> entries = this.zipArchive.getEntries();
                
                // First we need to parse the content type part
                while (entries.hasMoreElements())
                {
                    ZipEntry entry = entries.nextElement();
                    if (entry.getName().equalsIgnoreCase(ContentTypeManager.CONTENT_TYPES_PART_NAME))
                    {
                        InputStream in = zipArchive.getInputStream(entry);
                        contentTypeManager = new ContentTypeManager(in, this);
                        in.close();
                        break;
                    }
                }
                // 解析part
                entries = this.zipArchive.getEntries();
                while (entries.hasMoreElements())
                {
                    ZipEntry entry = entries.nextElement();
                    PackagePartName partName = buildPartName(entry);
                    if (partName == null)
                    {
                        continue;
                    }                    
                    // Only proceed for Relationships at this stage
                    String contentType = contentTypeManager.getContentType(partName);
                    if (contentType != null)
                    {
                        PackagePart part = new ZipPackagePart(this, entry, partName, contentType);
                        // 先把应用属性解析
                        if (contentType.equals(ContentTypes.CORE_PROPERTIES_PART))
                        {
                            PartUnmarshaller partUnmarshaller = partUnmarshallers.get(contentType);
                            if (partUnmarshaller != null)
                            {
                                UnmarshallContext context = new UnmarshallContext(this, part._partName);
                                PackagePart unmarshallPart = partUnmarshaller.unmarshall(context, part.getInputStream());
                                partList.put(unmarshallPart._partName, unmarshallPart);
                                // Core properties case
                                if (unmarshallPart instanceof PackagePropertiesPart)
                                {
                                    packageProperties = (PackagePropertiesPart)unmarshallPart;
                                }
                            }
                        }
                        else
                        {
                            partList.put(partName, part);
                        }
                    }
                }
            }
            catch (Exception e)
            {   
                e.printStackTrace();
            }
        }
        ArrayList<PackagePart> list = new ArrayList<PackagePart>(partList.values());
        for(PackagePart part : list)
        {
            part.loadRelationships();
        }        
        return list;
    }

    /**
     * Create and add a part, with the specified name and content type, to the
     * package.
     *
     * @param partName
     *            Part name.
     * @param contentType
     *            Part content type.
     * @return The newly created part.
     * @throws InvalidFormatException
     *             If rule M1.12 is not verified : Packages shall not contain
     *             equivalent part names and package implementers shall neither
     *             create nor recognize packages with equivalent part names.
     * @see #createPartImpl(PackagePartName, String, boolean)
     */
    public PackagePart createPart(PackagePartName partName, String contentType)
    {
        return createPart(partName, contentType, true);
    }

    /**
     * Create and add a part, with the specified name and content type, to the
     * package. For general purpose, prefer the overload version of this method
     * without the 'loadRelationships' parameter.
     *
     * @param partName
     *            Part name.
     * @param contentType
     *            Part content type.
     * @param loadRelationships
     *            Specify if the existing relationship part, if any, logically
     *            associated to the newly created part will be loaded.
     * @return The newly created part.
     * @throws InvalidFormatException
     *             If rule M1.12 is not verified : Packages shall not contain
     *             equivalent part names and package implementers shall neither
     *             create nor recognize packages with equivalent part names.
     * @see {@link#createPartImpl(URI, String)}
     */
    protected PackagePart createPart(PackagePartName partName, String contentType, boolean loadRelationships)
    {
        if (partName == null)
        {
            throw new IllegalArgumentException("partName");
        }
        if (contentType == null || contentType.equals(""))
        {
            throw new IllegalArgumentException("contentType");
        }

        // Check if the specified part name already exists
        if (partList.containsKey(partName) && !partList.get(partName).isDeleted())
        {
            throw new InvalidOperationException(
                "A part with the name '"
                    + partName.getName()
                    + "' already exists : Packages shall not contain equivalent part names and package implementers shall neither create nor recognize packages with equivalent part names. [M1.12]");
        }

        /* Check OPC compliance */

        // Rule [M4.1]: The format designer shall specify and the format
        // producer
        // shall create at most one core properties relationship for a package.
        // A format consumer shall consider more than one core properties
        // relationship for a package to be an error. If present, the
        // relationship shall target the Core Properties part.
        if (contentType.equals(ContentTypes.CORE_PROPERTIES_PART))
        {
            if (this.packageProperties != null)
                throw new InvalidOperationException(
                    "OPC Compliance error [M4.1]: you try to add more than one core properties relationship in the package !");
        }

        /* End check OPC compliance */

        PackagePart part = this.createPartImpl(partName, contentType, loadRelationships);
        this.contentTypeManager.addContentType(partName, contentType);
        this.partList.put(partName, part);
        this.isDirty = true;
        return part;
    }

    /**
     * Add a part to the package.
     *
     * @param partName
     *            Part name of the part to create.
     * @param contentType
     *            type associated with the file
     * @param content
     *            the contents to add. In order to have faster operation in
     *            document merge, the data are stored in memory not on a hard
     *            disk
     *
     * @return The new part.
     * @see #createPart(PackagePartName, String)
     */
    public PackagePart createPart(PackagePartName partName, String contentType,
        ByteArrayOutputStream content)
    {
        PackagePart addedPart = this.createPart(partName, contentType);
        if (addedPart == null)
        {
            return null;
        }
        // Extract the zip entry content to put it in the part content
        if (content != null)
        {
            try
            {
                OutputStream partOutput = addedPart.getOutputStream();
                if (partOutput == null)
                {
                    return null;
                }

                partOutput.write(content.toByteArray(), 0, content.size());
                partOutput.close();

            }
            catch(IOException ioe)
            {
                return null;
            }
        }
        else
        {
            return null;
        }
        return addedPart;
    }

    /**
     * Add the specified part to the package. If a part already exists in the
     * package with the same name as the one specified, then we replace the old
     * part by the specified part.
     *
     * @param part
     *            The part to add (or replace).
     * @return The part added to the package, the same as the one specified.
     * @throws InvalidFormatException
     *             If rule M1.12 is not verified : Packages shall not contain
     *             equivalent part names and package implementers shall neither
     *             create nor recognize packages with equivalent part names.
     */
    protected PackagePart addPackagePart(PackagePart part)
    {
        if (part == null)
        {
            throw new IllegalArgumentException("part");
        }

        if (partList.containsKey(part._partName))
        {
            if (!partList.get(part._partName).isDeleted())
            {
                throw new InvalidOperationException(
                    "A part with the name '"
                        + part._partName.getName()
                        + "' already exists : Packages shall not contain equivalent part names and package implementers shall neither create nor recognize packages with equivalent part names. [M1.12]");
            }
            // If the specified partis flagged as deleted, we make it
            // available
            part.setDeleted(false);
            // and delete the old part to replace it thereafeter
            this.partList.remove(part._partName);
        }
        this.partList.put(part._partName, part);
        this.isDirty = true;
        return part;
    }

    /**
     * Remove the specified part in this package. If this part is relationship
     * part, then delete all relationships in the source part.
     *
     * @param part
     *            The part to remove. If <code>null</code>, skip the action.
     * @see #removePart(PackagePartName)
     */
    public void removePart(PackagePart part)
    {
        if (part != null)
        {
            removePart(part.getPartName());
        }
    }

    /**
     * Remove a part in this package. If this part is relationship part, then
     * delete all relationships in the source part.
     *
     * @param partName
     *            The part name of the part to remove.
     */
    public void removePart(PackagePartName partName)
    {
        if (partName == null || !this.containPart(partName))
            throw new IllegalArgumentException("partName");

        // Delete the specified part from the package.
        if (this.partList.containsKey(partName))
        {
            this.partList.get(partName).setDeleted(true);
            this.partList.remove(partName);
        }
        // Delete content type
        this.contentTypeManager.removeContentType(partName);

        // If this part is a relationship part, then delete all relationships of
        // the source part.
        if (partName.isRelationshipPartURI())
        {
            URI sourceURI = PackagingURIHelper.getSourcePartUriFromRelationshipPartUri(partName
                .getURI());
            PackagePartName sourcePartName;
            try
            {
                sourcePartName = PackagingURIHelper.createPartName(sourceURI);
            }
            catch(InvalidFormatException e)
            {
                return;
            }
            if (sourcePartName.getURI().equals(PackagingURIHelper.PACKAGE_ROOT_URI))
            {
                clearRelationships();
            }
            else if (containPart(sourcePartName))
            {
                PackagePart part = getPart(sourcePartName);
                if (part != null)
                    part.clearRelationships();
            }
        }

        this.isDirty = true;
    }

    /**
     * Remove a part from this package as well as its relationship part, if one
     * exists, and all parts listed in the relationship part. Be aware that this
     * do not delete relationships which target the specified part.
     *
     * @param partName
     *            The name of the part to delete.
     * @throws InvalidFormatException
     *             Throws if the associated relationship part of the specified
     *             part is not valid.
     */
    public void removePartRecursive(PackagePartName partName) throws InvalidFormatException
    {
        // Retrieves relationship part, if one exists
        PackagePart relPart = this.partList.get(PackagingURIHelper.getRelationshipPartName(partName));
        // Retrieves PackagePart object from the package
        PackagePart partToRemove = this.partList.get(partName);
        if (relPart != null)
        {
            PackageRelationshipCollection partRels = new PackageRelationshipCollection(partToRemove);
            for (PackageRelationship rel : partRels)
            {
                PackagePartName partNameToRemove = PackagingURIHelper
                    .createPartName(PackagingURIHelper.resolvePartUri(rel.getSourceURI(),
                        rel.getTargetURI()));
                removePart(partNameToRemove);
            }

            // Finally delete its relationship part if one exists
            this.removePart(relPart._partName);
        }

        // Delete the specified part
        this.removePart(partToRemove._partName);
    }

    /**
     * Delete the part with the specified name and its associated relationships
     * part if one exists. Prefer the use of this method to delete a part in the
     * package, compare to the remove() methods that don't remove associated
     * relationships part.
     *
     * @param partName
     *            Name of the part to delete
     */
    public void deletePart(PackagePartName partName)
    {
        if (partName == null)
            throw new IllegalArgumentException("partName");

        // Remove the part
        this.removePart(partName);
        // Remove the relationships part
        this.removePart(PackagingURIHelper.getRelationshipPartName(partName));
    }

    /**
     * Delete the part with the specified name and all part listed in its
     * associated relationships part if one exists. This process is recursively
     * apply to all parts in the relationships part of the specified part.
     * Prefer the use of this method to delete a part in the package, compare to
     * the remove() methods that don't remove associated relationships part.
     *
     * @param partName
     *            Name of the part to delete
     */
    public void deletePartRecursive(PackagePartName partName)
    {
        if (partName == null || !this.containPart(partName))
        {
            throw new IllegalArgumentException("partName");
        }
        PackagePart partToDelete = this.getPart(partName);
        // Remove the part
        this.removePart(partName);
        // Remove all relationship parts associated
        try
        {
            for (PackageRelationship relationship : partToDelete.getRelationships())
            {
                PackagePartName targetPartName = PackagingURIHelper
                    .createPartName(PackagingURIHelper.resolvePartUri(partName.getURI(),
                        relationship.getTargetURI()));
                this.deletePartRecursive(targetPartName);
            }
        }
        catch(InvalidFormatException e)
        {
            return;
        }
        // Remove the relationships part
        PackagePartName relationshipPartName = PackagingURIHelper.getRelationshipPartName(partName);
        if (relationshipPartName != null && containPart(relationshipPartName))
            this.removePart(relationshipPartName);
    }

    /**
     * Check if a part already exists in this package from its name.
     *
     * @param partName
     *            Part name to check.
     * @return <i>true</i> if the part is logically added to this package, else
     *         <i>false</i>.
     */
    public boolean containPart(PackagePartName partName)
    {
        return (this.getPart(partName) != null);
    }

    /**
     * Add a relationship to the package (except relationships part).
     *
     * Check rule M4.1 : The format designer shall specify and the format
     * producer shall create at most one core properties relationship for a
     * package. A format consumer shall consider more than one core properties
     * relationship for a package to be an error. If present, the relationship
     * shall target the Core Properties part.
     *
     * Check rule M1.25: The Relationships part shall not have relationships to
     * any other part. Package implementers shall enforce this requirement upon
     * the attempt to create such a relationship and shall treat any such
     * relationship as invalid.
     *
     * @param targetPartName
     *            Target part name.
     * @param targetMode
     *            Target mode, either Internal or External.
     * @param relationshipType
     *            Relationship type.
     * @param relID
     *            ID of the relationship.
     * @see PackageRelationshipTypes
     */
    public PackageRelationship addRelationship(PackagePartName targetPartName,
        TargetMode targetMode, String relationshipType, String relID)
    {
        /* Check OPC compliance */

        // Check rule M4.1 : The format designer shall specify and the format
        // producer
        // shall create at most one core properties relationship for a package.
        // A format consumer shall consider more than one core properties
        // relationship for a package to be an error. If present, the
        // relationship shall target the Core Properties part.
        if (relationshipType.equals(PackageRelationshipTypes.CORE_PROPERTIES)
            && this.packageProperties != null)
            throw new InvalidOperationException(
                "OPC Compliance error [M4.1]: can't add another core properties part ! Use the built-in package method instead.");

        /*
         * Check rule M1.25: The Relationships part shall not have relationships
         * to any other part. Package implementers shall enforce this
         * requirement upon the attempt to create such a relationship and shall
         * treat any such relationship as invalid.
         */
        if (targetPartName.isRelationshipPartURI())
        {
            throw new InvalidOperationException(
                "Rule M1.25: The Relationships part shall not have relationships to any other part.");
        }

        /* End OPC compliance */

        ensureRelationships();
        PackageRelationship retRel = relationships.addRelationship(targetPartName.getURI(),
            targetMode, relationshipType, relID);
        this.isDirty = true;
        return retRel;
    }

    /**
     * Add a package relationship.
     *
     * @param targetPartName
     *            Target part name.
     * @param targetMode
     *            Target mode, either Internal or External.
     * @param relationshipType
     *            Relationship type.
     * @see PackageRelationshipTypes
     */
    public PackageRelationship addRelationship(PackagePartName targetPartName,
        TargetMode targetMode, String relationshipType)
    {
        return this.addRelationship(targetPartName, targetMode, relationshipType, null);
    }

    /**
     * Adds an external relationship to a part (except relationships part).
     *
     * The targets of external relationships are not subject to the same
     * validity checks that internal ones are, as the contents is potentially
     * any file, URL or similar.
     *
     * @param target
     *            External target of the relationship
     * @param relationshipType
     *            Type of relationship.
     * @return The newly created and added relationship
     * @see org.apache.poi.openxml4j.opc.RelationshipSource#addExternalRelationship(java.lang.String,
     *      java.lang.String)
     */
    public PackageRelationship addExternalRelationship(String target, String relationshipType)
    {
        return addExternalRelationship(target, relationshipType, null);
    }

    /**
     * Adds an external relationship to a part (except relationships part).
     *
     * The targets of external relationships are not subject to the same
     * validity checks that internal ones are, as the contents is potentially
     * any file, URL or similar.
     *
     * @param target
     *            External target of the relationship
     * @param relationshipType
     *            Type of relationship.
     * @param id
     *            Relationship unique id.
     * @return The newly created and added relationship
     * @see org.apache.poi.openxml4j.opc.RelationshipSource#addExternalRelationship(java.lang.String,
     *      java.lang.String)
     */
    public PackageRelationship addExternalRelationship(String target, String relationshipType,
        String id)
    {
        if (target == null)
        {
            throw new IllegalArgumentException("target");
        }
        if (relationshipType == null)
        {
            throw new IllegalArgumentException("relationshipType");
        }
        URI targetURI;
        try
        {
            targetURI = new URI(target);
        }
        catch(URISyntaxException e)
        {
            throw new IllegalArgumentException("Invalid target - " + e);
        }
        ensureRelationships();
        PackageRelationship retRel = relationships.addRelationship(targetURI, TargetMode.EXTERNAL,
            relationshipType, id);
        this.isDirty = true;
        return retRel;
    }

    /**
     * Delete a relationship from this package.
     *
     * @param id
     *            Id of the relationship to delete.
     */
    public void removeRelationship(String id)
    {
        if (relationships != null)
        {
            relationships.removeRelationship(id);
            this.isDirty = true;
        }
    }

    /**
     * Retrieves all package relationships.
     *
     * @return All package relationships of this package.
     * @throws OpenXML4JException
     * @see #getRelationshipsHelper(String)
     */
    public PackageRelationshipCollection getRelationships()
    {
        return getRelationshipsHelper(null);
    }

    /**
     * Retrieves all relationships with the specified type.
     *
     * @param relationshipType
     *            The filter specifying the relationship type.
     * @return All relationships with the specified relationship type.
     */
    public PackageRelationshipCollection getRelationshipsByType(String relationshipType)
    {
        if (relationshipType == null)
        {
            throw new IllegalArgumentException("relationshipType");
        }
        return getRelationshipsHelper(relationshipType);
    }

    /**
     * Retrieves all relationships with specified id (normally just ine because
     * a relationship id is supposed to be unique).
     *
     * @param id
     *            Id of the wanted relationship.
     */
    private PackageRelationshipCollection getRelationshipsHelper(String id)
    {
        ensureRelationships();
        return this.relationships.getRelationships(id);
    }

    /**
     * Clear package relationships.
     */
    public void clearRelationships()
    {
        if (relationships != null)
        {
            relationships.clear();
            this.isDirty = true;
        }
    }

    /**
     * Ensure that the relationships collection is not null.
     */
    public void ensureRelationships()
    {
        if (this.relationships == null)
        {
            try
            {
                this.relationships = new PackageRelationshipCollection(this);
            }
            catch(InvalidFormatException e)
            {
                this.relationships = new PackageRelationshipCollection();
            }
        }
    }

    /**
     * @see org.apache.poi.openxml4j.opc.RelationshipSource#getRelationship(java.lang.String)
     */
    public PackageRelationship getRelationship(String id)
    {
        return this.relationships.getRelationshipByID(id);
    }

    /**
     * @see org.apache.poi.openxml4j.opc.RelationshipSource#hasRelationships()
     */
    public boolean hasRelationships()
    {
        return (relationships.size() > 0);
    }

    /**
     * @see org.apache.poi.openxml4j.opc.RelationshipSource#isRelationshipExists(org.apache.poi.openxml4j.opc.PackageRelationship)
     */
    public boolean isRelationshipExists(PackageRelationship rel)
    {
        for (PackageRelationship r : this.getRelationships())
        {
            if (r == rel)
                return true;
        }
        return false;
    }

    /**
     * Add a marshaller.
     *
     * @param contentType
     *            The content type to bind to the specified marshaller.
     * @param marshaller
     *            The marshaller to register with the specified content type.
     */
    public void addMarshaller(String contentType, PartMarshaller marshaller)
    {
        try
        {
            partMarshallers.put(new ContentType(contentType), marshaller);
        }
        catch(InvalidFormatException e)
        {
        }
    }

    /**
     * Add an unmarshaller.
     *
     * @param contentType
     *            The content type to bind to the specified unmarshaller.
     * @param unmarshaller
     *            The unmarshaller to register with the specified content type.
     */
    public void addUnmarshaller(String contentType, PartUnmarshaller unmarshaller)
    {
        try
        {
            partUnmarshallers.put(new ContentType(contentType), unmarshaller);
        }
        catch(InvalidFormatException e)
        {
        }
    }

    /**
     * Remove a marshaller by its content type.
     *
     * @param contentType
     *            The content type associated with the marshaller to remove.
     */
    public void removeMarshaller(String contentType)
    {
        partMarshallers.remove(contentType);
    }

    /**
     * Remove an unmarshaller by its content type.
     *
     * @param contentType
     *            The content type associated with the unmarshaller to remove.
     */
    public void removeUnmarshaller(String contentType)
    {
        partUnmarshallers.remove(contentType);
    }

    /**
     * Validates the package compliance with the OPC specifications.
     *
     * @return <b>true</b> if the package is valid else <b>false</b>
     */
    public boolean validatePackage(ZipPackage pkg) throws InvalidFormatException
    {
        throw new InvalidOperationException("Not implemented yet !!!");
    }

    /**
     * Save the document in the specified file.
     *
     * @param targetFile
     *            Destination file.
     * @throws IOException
     *             Throws if an IO exception occur.
     * @see #save(OutputStream)
     */
    public void save(File targetFile) throws IOException
    {
        if (targetFile == null)
        {
            throw new IllegalArgumentException("targetFile");
        }

        // You shouldn't save the the same file, do a close instead
        if (targetFile.exists() && targetFile.getAbsolutePath().equals(this.originalPackagePath))
        {
            throw new InvalidOperationException(
                "You can't call save(File) to save to the currently open "
                    + "file. To save to the current file, please just call close()");
        }

        // Do the save
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(targetFile);
        }
        catch(FileNotFoundException e)
        {
            throw new IOException(e.getLocalizedMessage());
        }
        this.save(fos);
        fos.close();
    }

    /**
     * Save the document in the specified output stream.
     *
     * @param outputStream
     *            The stream to save the package.
     * @see #saveImpl(OutputStream)
     */
    public void save(OutputStream outputStream) throws IOException
    {
        this.saveImpl(outputStream);
    }


    /**
     * Builds a PackagePartName for the given ZipEntry,
     *  or null if it's the content types / invalid part
     */
    private PackagePartName buildPartName(ZipEntry entry)
    {
        try
        {
            // We get an error when we parse [Content_Types].xml
            // because it's not a valid URI.
            if (entry.getName().equalsIgnoreCase(ContentTypeManager.CONTENT_TYPES_PART_NAME))
            {
                return null;
            }
            return PackagingURIHelper.createPartName(ZipHelper.getOPCNameFromZipItemName(entry
                .getName()));
        }
        catch(Exception e)
        {
            return null;
        }
    }

    /**
     * Create a new MemoryPackagePart from the specified URI and content type
     *
     *
     * aram partName The part URI.
     *
     * @param contentType
     *            The part content type.
     * @return The newly created zip package part, else <b>null</b>.
     */
    protected PackagePart createPartImpl(PackagePartName partName, String contentType,
        boolean loadRelationships)
    {
        /*if (contentType == null)
        {
            throw new IllegalArgumentException("contentType");
        }

        if (partName == null)
        {
            throw new IllegalArgumentException("partName");
        }
        try
        {
            return new MemoryPackagePart(this, partName, contentType, loadRelationships);
        }
        catch(InvalidFormatException e)
        {
            return null;
        }*/
        return null;
    }

    /**
     * Close and save the package.
     *
     * @see #close()
     */
    protected void closeImpl() throws IOException
    {
        // Flush the package
        flush();

        // Save the content
        if (this.originalPackagePath != null && !"".equals(this.originalPackagePath))
        {
            File targetFile = new File(this.originalPackagePath);
            if (targetFile.exists())
            {
                // Case of a package previously open

                File tempFile = File.createTempFile(
                    generateTempFileName(FileHelper.getDirectory(targetFile)), ".tmp");

                // Save the final package to a temporary file
                try
                {
                    save(tempFile);

                    // Close the current zip file, so we can
                    //  overwrite it on all platforms
                    this.zipArchive.close();
                    // Copy the new file over the old one
                    FileHelper.copyFile(tempFile, targetFile);
                }
                finally
                {
                    // Either the save operation succeed or not, we delete the
                    // temporary file
                    if (!tempFile.delete())
                    {
                    }
                }
            }
            else
            {
                throw new InvalidOperationException(
                    "Can't close a package not previously open with the open() method !");
            }
        }
    }

    /**
     * Create a unique identifier to be use as a temp file name.
     *
     * @return A unique identifier use to be use as a temp file name.
     */
    private synchronized String generateTempFileName(File directory)
    {
        File tmpFilename;
        do
        {
            tmpFilename = new File(directory.getAbsoluteFile() + File.separator + "OpenXML4J"
                + System.nanoTime());
        }
        while (tmpFilename.exists());
        return FileHelper.getFilename(tmpFilename.getAbsoluteFile());
    }

    /**
     * Close the package without saving the document. Discard all the changes
     * made to this package.
     */
    protected void revertImpl()
    {
        try
        {
            if (this.zipArchive != null)
                this.zipArchive.close();
        }
        catch(IOException e)
        {
            // Do nothing, user dont have to know
        }
    }

    /**
     * Implement the getPart() method to retrieve a part from its URI in the
     * current package
     *
     *
     * @see #getPart(PackageRelationship)
     */
    protected PackagePart getPartImpl(PackagePartName partName)
    {
        if (partList.containsKey(partName))
        {
            return partList.get(partName);
        }
        return null;
    }

    /**
     * Save this package into the specified stream
     *
     *
     * @param outputStream
     *            The stream use to save this package.
     *
     * @see #save(OutputStream)
     */
    public void saveImpl(OutputStream outputStream)
    {
        // Check that the document was open in write mode
        ZipOutputStream zos = null;

        try
        {
            if (!(outputStream instanceof ZipOutputStream))
                zos = new ZipOutputStream(outputStream);
            else
                zos = (ZipOutputStream)outputStream;

            // If the core properties part does not exist in the part list,
            // we save it as well
            if (this.getPartsByRelationshipType(PackageRelationshipTypes.CORE_PROPERTIES).size() == 0
                && getPartsByRelationshipType(PackageRelationshipTypes.CORE_PROPERTIES_ECMA376).size() == 0)
            {

                // We have to save the core properties part ...
                new ZipPackagePropertiesMarshaller().marshall(this.packageProperties, zos);
                // ... and to add its relationship ...
                this.relationships.addRelationship(this.packageProperties.getPartName().getURI(),
                    TargetMode.INTERNAL, PackageRelationshipTypes.CORE_PROPERTIES, null);
                // ... and the content if it has not been added yet.
                if (!this.contentTypeManager
                    .isContentTypeRegister(ContentTypes.CORE_PROPERTIES_PART))
                {
                    this.contentTypeManager.addContentType(this.packageProperties.getPartName(),
                        ContentTypes.CORE_PROPERTIES_PART);
                }
            }

            // Save package relationships part.
            ZipPartMarshaller.marshallRelationshipPart(this.getRelationships(),
                PackagingURIHelper.PACKAGE_RELATIONSHIPS_ROOT_PART_NAME, zos);
            // Save content type part.
            contentTypeManager.save(zos);

            // Save parts.
            for (PackagePart part : getParts())
            {
                // If the part is a relationship part, we don't save it, it's
                // the source part that will do the job.
                if (part.isRelationshipPart())
                {
                    continue;
                }
                PartMarshaller marshaller = partMarshallers.get(part._contentType);
                if (marshaller != null)
                {
                    if (!marshaller.marshall(part, zos))
                    {
                        throw new OpenXML4JException("The part " + part.getPartName().getURI()
                            + " fail to be saved in the stream with marshaller " + marshaller);
                    }
                }
                else
                {
                    if (!defaultPartMarshaller.marshall(part, zos))
                        throw new OpenXML4JException("The part " + part.getPartName().getURI()
                            + " fail to be saved in the stream with marshaller "
                            + defaultPartMarshaller);
                }
            }
            zos.close();
        }
        catch(Exception e)
        {
            throw new OpenXML4JRuntimeException(
                "Fail to save: an error occurs while saving the package : " + e.getMessage(), e);
        }
    }

    /**
     * Get the zip archive
     *
     * @return The zip archive.
     */
    public ZipEntrySource getZipArchive()
    {
        return zipArchive;
    }
    /**
     * Zip archive, as either a file on disk,
     *  or a stream
     */
    private ZipEntrySource zipArchive;
    /**
     * Package parts collection.
     */
    protected PackagePartCollection partList;

    /**
     * Package relationships.
     */
    protected PackageRelationshipCollection relationships;

    /**
     * Part marshallers by content type.
     */
    protected Hashtable<ContentType, PartMarshaller> partMarshallers;

    /**
     * Default part marshaller.
     */
    protected PartMarshaller defaultPartMarshaller;

    /**
     * Part unmarshallers by content type.
     */
    protected Hashtable<ContentType, PartUnmarshaller> partUnmarshallers;

    /**
     * Core package properties.
     */
    protected PackagePropertiesPart packageProperties;

    /**
     * Manage parts content types of this package.
     */
    protected ContentTypeManager contentTypeManager;

    /**
     * Flag if a modification is done to the document.
     */
    protected boolean isDirty = false;

    /**
     * File path of this package.
     */
    protected String originalPackagePath;

    /**
     * Output stream for writing this package.
     */
    protected OutputStream output;
}
