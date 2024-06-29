// Copyright 2003-2009, FreeHEP.
package com.nvqquy98.lib.doc.office.thirdpart.emf.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Reads images from a JPEG Stream, but only images.
 * 
 * @author Mark Donszelmann
 */
public class DCTInputStream extends FilterInputStream {

	/**
	 * Creates a DCT input stream from the given input stream
	 * 
	 * @param input
	 *            stream to read from
	 */
	public DCTInputStream(InputStream input) {
		super(input);
	}

	/**
	 * Read is not supported, only readImage.
	 * 
	 * @see java.io.FilterInputStream#read()
	 */
	public int read() throws IOException {
		throw new IOException(getClass()
				+ ": read() not implemented, use readImage().");
	}
}
