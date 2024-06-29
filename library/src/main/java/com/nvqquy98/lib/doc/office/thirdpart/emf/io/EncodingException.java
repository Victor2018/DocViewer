// Copyright 2001, FreeHEP.
package com.nvqquy98.lib.doc.office.thirdpart.emf.io;

import java.io.IOException;

/**
 * Encoding Exception for any of the encoding streams.
 * 
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/util/io/EncodingException.java
 *          96b41b903496 2005/11/21 19:50:18 duns $
 */
public class EncodingException extends IOException {

	/**
     * 
     */
	private static final long serialVersionUID = 8496816190751796701L;

	/**
	 * Creates an Encoding Exception
	 * 
	 * @param msg
	 *            message
	 */
	public EncodingException(String msg) {
		super(msg);
	}
}
