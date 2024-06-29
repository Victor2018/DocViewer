/*
 * %W% %E%
 *
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.nvqquy98.lib.doc.office.java.awt;

//import com.nvqquy98.lib.doc.office.java.awt.image.ImageProducer;
//import com.nvqquy98.lib.doc.office.java.awt.image.ImageObserver;
//import com.nvqquy98.lib.doc.office.java.awt.image.ImageFilter;
//import com.nvqquy98.lib.doc.office.java.awt.image.FilteredImageSource;
//import com.nvqquy98.lib.doc.office.java.awt.image.AreaAveragingScaleFilter;
//import com.nvqquy98.lib.doc.office.java.awt.image.ReplicateScaleFilter;
//
//import sun.awt.image.SurfaceManager;

/**
 * The abstract class <code>Image</code> is the superclass of all 
 * classes that represent graphical images. The image must be 
 * obtained in a platform-specific manner.
 *
 * @version 	%I%, %G%
 * @author 	Sami Shaio
 * @author 	Arthur van Hoff
 * @since       JDK1.0
 */
public abstract class Image {
    /**
     * Use the default image-scaling algorithm.
     * @since JDK1.1
     */
    public static final int SCALE_DEFAULT = 1;

    /**
     * Choose an image-scaling algorithm that gives higher priority
     * to scaling speed than smoothness of the scaled image.
     * @since JDK1.1
     */
    public static final int SCALE_FAST = 2;

    /**
     * Choose an image-scaling algorithm that gives higher priority
     * to image smoothness than scaling speed.
     * @since JDK1.1
     */
    public static final int SCALE_SMOOTH = 4;

    /**
     * Use the image scaling algorithm embodied in the 
     * <code>ReplicateScaleFilter</code> class.  
     * The <code>Image</code> object is free to substitute a different filter 
     * that performs the same algorithm yet integrates more efficiently
     * into the imaging infrastructure supplied by the toolkit.
     * @see        java.awt.image.ReplicateScaleFilter
     * @since      JDK1.1
     */
    public static final int SCALE_REPLICATE = 8;

    /**
     * Use the Area Averaging image scaling algorithm.  The
     * image object is free to substitute a different filter that
     * performs the same algorithm yet integrates more efficiently
     * into the image infrastructure supplied by the toolkit.
     * @see java.awt.image.AreaAveragingScaleFilter
     * @since JDK1.1
     */
    public static final int SCALE_AREA_AVERAGING = 16;
}
